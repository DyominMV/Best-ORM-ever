package alyona.bestorm.storage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import alyona.bestorm.annotations.classes.StoredClass;
import alyona.bestorm.annotations.fields.IndexField;
import alyona.bestorm.annotations.fields.NamedField;
import alyona.bestorm.annotations.fields.PrimaryKey;
import alyona.bestorm.annotations.fields.WithSerializer;
import alyona.bestorm.exception.BadlyAnnotatedException;
import alyona.bestorm.storage.result.ErrorResult;
import alyona.bestorm.storage.result.Result;
import alyona.bestorm.storage.result.SuccessResult;
import alyona.bestorm.storage.supporters.FieldSupporter;
import alyona.bestorm.storage.supporters.Serializer;
import alyona.bestorm.storage.supporters.Serializers;
import redis.clients.jedis.Jedis;

public class RedisStorage<DataClass, KeyClass> implements IRedisStorage<DataClass, KeyClass> {

  private static final String REDIS_TYPE_HASH = "hash";
  private static final String REDIS_TYPE_NONE = "none";
  private static final String DELIMITER = "::";
  private static final String TABLE_MARK = "table";

  private final Jedis jedis;

  private final Class<DataClass> dataClass;
  private final Class<KeyClass> keyClass;
  private final Field keyField;
  private final Serializer<KeyClass> keyFieldSerializer;

  private final String tableName;
  private final int keyPrefixLength;

  private final HashMap<Field, FieldSupporter<?>> supporters = new HashMap<>();
  private final HashMap<String, Field> indexFields = new HashMap<>();
  private final HashMap<Field, String> indexFieldNames = new HashMap<>();

  @SuppressWarnings("unchecked")
  RedisStorage(Jedis jedis, Class<DataClass> dataClass, Class<KeyClass> keyClass,
      IRedisStorageProvider storageProvider) {
    this.tableName = defineTableName(dataClass);

    try {
      dataClass.getDeclaredConstructor();
    } catch (NoSuchMethodException | SecurityException e) {
      throw new BadlyAnnotatedException(
          "Add default constructor to class " + dataClass + " or make it accessable.");
    }

    this.keyPrefixLength = (tableName + DELIMITER + TABLE_MARK + DELIMITER).length();

    this.jedis = jedis;
    this.keyClass = keyClass;
    this.dataClass = dataClass;

    for (Field field : dataClass.getDeclaredFields()) {
      registerNamedField(field);
      registerIndexField(field);
    }

    this.keyField = defineKeyField(dataClass);

    if (keyClass != keyField.getType()) {
      throw new BadlyAnnotatedException(
          "Class provided as keyClass to the constructor of a storage must be same to a class of field annotated with "
              + PrimaryKey.class);
    }

    this.keyFieldSerializer = (Serializer<KeyClass>) supporters.get(keyField);
  }

  private String defineTableName(Class<DataClass> dClass) {
    if (!dClass.isAnnotationPresent(StoredClass.class)) {
      throw new BadlyAnnotatedException("Cannot store class that is not annotated as StoredClass");
    }

    return dClass.getAnnotation(StoredClass.class).tableName();
  }

  private Field defineKeyField(Class<DataClass> dClass) {
    List<Field> annotatedFields = Arrays.stream(dClass.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(PrimaryKey.class)).collect(Collectors.toList());

    if (1 != annotatedFields.size()) {
      throw new BadlyAnnotatedException("Exactly one field of class " + dClass
          + " needs to be annotated with " + PrimaryKey.class);
    }

    Field keyField = annotatedFields.iterator().next();

    if (!keyField.isAnnotationPresent(NamedField.class)) {
      throw new BadlyAnnotatedException(
          "Primary key field " + keyField + " needs to be annotated with " + NamedField.class);
    }

    return keyField;
  }

  private void registerNamedField(Field field) {
    if (!field.isAnnotationPresent(NamedField.class)) {
      return;
    }

    Serializer<?> serializer = defineSerializer(field);
    field.setAccessible(true);
    supporters.put(field,
        FieldSupporter.of(field.getAnnotation(NamedField.class).name(), serializer));
  }

  private void registerIndexField(Field field) {
    if (!field.isAnnotationPresent(IndexField.class)) {
      return;
    }

    if (!field.isAnnotationPresent(NamedField.class)) {
      throw new BadlyAnnotatedException(
          "Unnamed field cannot be index field. Use " + NamedField.class);
    }

    String indexName = field.getAnnotation(NamedField.class).name();
    indexFields.put(indexName, field);
    indexFieldNames.put(field, indexName);
  }

  private Serializer<?> defineSerializer(Field field) {
    if (field.isAnnotationPresent(WithSerializer.class)) {
      try {
        Serializer<?> definedSerializer = field.getAnnotation(WithSerializer.class).serializer()
            .getDeclaredConstructor().newInstance();

        if (field.getType() != definedSerializer.getSerializedClass()) {
          throw new BadlyAnnotatedException("Invalid serializer is set to field " + field);
        }

        return definedSerializer;

      } catch (Exception e) {
        throw new BadlyAnnotatedException("Invalid serializer is set for field " + field);
      }
    } else {
      Serializer<?> serializer = Serializers.getDefaultSerializer(field.getType());

      if (null == serializer) {
        throw new BadlyAnnotatedException("No default serializer can be used with field " + field
            + ". Use " + WithSerializer.class + " annotation.");
      }

      return serializer;
    }
  }

  private KeyClass extractKey(DataClass data) {
    try {
      keyField.setAccessible(true);
      KeyClass key =keyClass.cast(keyField.get(data)); 
      
      if (null == key){
        throw new RuntimeException("key field is null in object " + data);
      }

      return key;
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private String getFullKey(KeyClass key) {
    return tableName + DELIMITER + keyFieldSerializer.serialize(key);
  }

  private String getFullKey(String key) {
    return tableName + DELIMITER + TABLE_MARK + DELIMITER + key;
  }

  @SuppressWarnings("unchecked")
  private <IndexClass> String getFullIndexKey(String indexName, IndexClass indexValue) {
    if (!indexFields.containsKey(indexName)) {
      throw new RuntimeException("wrong index name " + indexName + " for table " + tableName);
    }

    Field indexField = indexFields.get(indexName);
    Class<?> indexType = indexField.getType();

    if (!indexType.equals(indexValue.getClass())) {
      throw new RuntimeException("wrong value " + indexValue + " provided for index " + indexName
          + " in table " + tableName);
    }

    return tableName + DELIMITER + indexName + DELIMITER
        + ((FieldSupporter<IndexClass>) supporters.get(indexField)).serialize(indexValue);
  }

  private CompletableFuture<Result<Optional<DataClass>>> getFieldsFromRedis(
      String fullKeyForRedisHash) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        if (REDIS_TYPE_NONE.equalsIgnoreCase(jedis.type(fullKeyForRedisHash))) {
          return new SuccessResult<>(Optional.empty());
        }

        if (!REDIS_TYPE_HASH.equalsIgnoreCase(jedis.type(fullKeyForRedisHash))) {
          return new ErrorResult<>(
              new RuntimeException("value type at key " + fullKeyForRedisHash + " is not hash"));
        }

        DataClass dataObject = dataClass.getDeclaredConstructor().newInstance();

        for (Map.Entry<Field, FieldSupporter<?>> entry : supporters.entrySet()) {
          Field field = entry.getKey();
          FieldSupporter<?> supporter = entry.getValue();
          String redisValue = jedis.hget(fullKeyForRedisHash, supporter.getName());

          if (null != redisValue) {
            field.set(dataObject, supporter.deserialize(redisValue));
          }
        }

        return new SuccessResult<>(Optional.of(dataObject));
      } catch (Exception e) {
        return new ErrorResult<>(e);
      }
    });
  }

  public CompletableFuture<Result<Optional<DataClass>>> findBySerializedKeyAsync(String key) {
    return getFieldsFromRedis(getFullKey(key));
  }

  @Override
  public CompletableFuture<Result<Optional<DataClass>>> findByKeyAsync(KeyClass key) {
    return findBySerializedKeyAsync(keyFieldSerializer.serialize(key));
  }

  @Override
  public <IndexClass> CompletableFuture<Result<Iterable<DataClass>>> findByIndexAsync(
      String indexName, IndexClass indexValue) {
    return CompletableFuture.supplyAsync(() -> {
      String fullIndexKey = getFullIndexKey(indexName, indexValue);
      try {
        List<DataClass> dataObjects = new ArrayList<DataClass>();

        for (String objectKey : jedis.smembers(fullIndexKey)) {
          Result<Optional<DataClass>> result = findBySerializedKeyAsync(objectKey).join();

          if (result.isError()) {
            jedis.srem(fullIndexKey, objectKey);
          } else if (result.getOrNull().isPresent()) {
            dataObjects.add(result.getOrNull().get());
          }
        }

        return new SuccessResult<>(dataObjects);
      } catch (Exception e) {
        return new ErrorResult<>(e);
      }
    });
  }

  @Override
  public CompletableFuture<Result<Iterable<DataClass>>> getAllAsync() {
    return CompletableFuture.supplyAsync(() -> {
      return new SuccessResult<>(jedis.keys(tableName + DELIMITER + TABLE_MARK + DELIMITER + "*")
          .stream().map(fullKey -> fullKey.substring(this.keyPrefixLength))
          .map(this::findBySerializedKeyAsync).map(CompletableFuture::join)
          .filter(Result::isSuccess).map(Result::getOrNull).filter(Optional::isPresent)
          .map(Optional::get).collect(Collectors.toList()));
    });
  }

  private <IndexClass> void removeFromIndex(String serializedKey, String indexName,
      IndexClass indexValue) {
    if (null == indexValue) {
      return;
    }

    jedis.srem(getFullIndexKey(indexName, indexValue), serializedKey);
  }

  private <IndexClass> void addToIndex(String serializedKey, String indexName,
      IndexClass indexValue) {
    if (null == indexValue) {
      return;
    }

    jedis.sadd(getFullIndexKey(indexName, indexValue), serializedKey);
  }

  @SuppressWarnings("unchecked")
  private <ValueClass> boolean putField(String serializedKey, Field field, ValueClass oldValue,
      ValueClass newValue) {
    if (Objects.equals(oldValue, newValue)) {
      return false;
    }

    String indexName = indexFieldNames.get(field);

    if (null != indexName) {
      removeFromIndex(serializedKey, indexName, oldValue);
      addToIndex(serializedKey, indexName, newValue);
    }

    FieldSupporter<ValueClass> supporter = (FieldSupporter<ValueClass>) supporters.get(field);

    if (null != newValue) {
      jedis.hset(getFullKey(serializedKey), supporter.getName(), supporter.serialize(newValue));
    } else {
      jedis.hdel(getFullKey(serializedKey), supporter.getName());
    }

    return true;
  }

  @Override
  public CompletableFuture<Result<Boolean>> putAsync(DataClass data) {
    return findByKeyAsync(extractKey(data)).thenApply(result -> result.flatMap(optionalData -> {
      try {
        DataClass oldData;

        if (optionalData.isPresent()) {
          oldData = optionalData.get();
        } else {
          oldData = dataClass.getDeclaredConstructor().newInstance();
        }

        boolean fieldChanged = false;

        KeyClass key = extractKey(data);
        String serializedKey = keyFieldSerializer.serialize(key);

        for (Field field : supporters.keySet()) {
          Object oldValue = field.get(oldData);
          Object newValue = field.get(data);

          fieldChanged |= putField(serializedKey, field, oldValue, newValue);
        }

        return new SuccessResult<>(fieldChanged);
      } catch (Exception e) {
        return new ErrorResult<>(e);
      }
    }));
  }

  @Override
  public CompletableFuture<Result<DataClass>> mergeAsync(DataClass data) {
    return findByKeyAsync(extractKey(data)).thenApply(result -> result.flatMap(optionalData -> {
      try {
        DataClass oldData;

        if (optionalData.isPresent()) {
          oldData = optionalData.get();
        } else {
          oldData = dataClass.getDeclaredConstructor().newInstance();
        }

        KeyClass key = extractKey(data);
        String serializedKey = keyFieldSerializer.serialize(key);

        for (Field field : supporters.keySet()) {
          Object oldValue = field.get(oldData);
          Object newValue = field.get(data);

          if (null != newValue && putField(serializedKey, field, oldValue, newValue)) {
            field.set(oldData, newValue);
          }
        }

        return new SuccessResult<>(oldData);
      } catch (Exception e) {
        return new ErrorResult<>(e);
      }
    }));
  }

  private void removeFromAllIndices(DataClass data) throws Exception {
    for (Map.Entry<Field, String> entry : indexFieldNames.entrySet()) {
      Field field = entry.getKey();
      String indexName = entry.getValue();
      Object indexValue = field.get(data);
      String serializedKey = keyFieldSerializer.serialize(extractKey(data));

      removeFromIndex(serializedKey, indexName, indexValue);
    }
  }

  @Override
  public CompletableFuture<Result<Boolean>> removeByKeyAsync(KeyClass key) {
    return findByKeyAsync(key).thenApply(result -> result.flatMap(optionalData -> {
      if (optionalData.isPresent()) {
        return new SuccessResult<>(false);
      } else {
        DataClass data = optionalData.get();
        try {
          removeFromAllIndices(data);
          String fullKey = getFullKey(key);
          return new SuccessResult<>(1L == jedis.del(fullKey));
        } catch (Exception e) {
          return new ErrorResult<>(e);
        }
      }
    }));
  }

  @Override
  public CompletableFuture<Result<Boolean>> removeAsync(DataClass data) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        removeFromAllIndices(data);
        String fullKey = getFullKey(extractKey(data));
        return new SuccessResult<>(1L == jedis.del(fullKey));
      } catch (Exception e) {
        return new ErrorResult<>(e);
      }
    });
  }

}
