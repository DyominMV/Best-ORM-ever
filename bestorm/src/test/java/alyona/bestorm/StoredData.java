package alyona.bestorm;

import alyona.bestorm.annotations.classes.StoredClass;
import alyona.bestorm.annotations.fields.IndexField;
import alyona.bestorm.annotations.fields.NamedField;
import alyona.bestorm.annotations.fields.PrimaryKey;
import alyona.bestorm.storage.IRedisStorage;
import alyona.bestorm.storage.RedisStorageProvider;
import redis.clients.jedis.Jedis;

@StoredClass(tableName = "StoredData")
public class StoredData {
  @PrimaryKey
  @NamedField(name = "SomeInt")
  private Integer someInt;

  @NamedField(name = "SomeOtherInt")
  @IndexField
  private Integer otherInt;

  @NamedField(name = "Name")
  @IndexField
  private String name;

  @NamedField(name = "Laugh")
  private String laugh;

  public StoredData(){}

  public StoredData(Integer someInt, Integer otherInt, String name, String laugh) {
    this.someInt = someInt;
    this.otherInt = otherInt;
    this.name = name;
    this.laugh = laugh;
  }

  @Override
  public String toString() {
    return "SomeInt: " + someInt + "; SomeOtherInt: " + otherInt + "; Name: " + name + "; Laugh: " + laugh;
  }

  public static void main(String[] args) {
    IRedisStorage<StoredData, Integer> storage = new RedisStorageProvider(new Jedis()).getStorage(StoredData.class, Integer.class);
    
    storage.putAsync(new StoredData(10, 11, "Hello", "LOL")).join().getOrThrow();
    storage.putAsync(new StoredData(20, 11, "World", "haha")).join();
    storage.putAsync(new StoredData(30, 13, "AAAA", "hehe")).join();
    
    storage.getAllAsStreamAsync().join().getOrThrow().forEach(System.out::println);

    System.out.println();

    storage.mergeAsync(new StoredData(10, 13, "World", null)).join();

    storage.getAllAsStreamAsync().join().getOrThrow().forEach(System.out::println);

    System.out.println();

    storage.findByIndexAsStreamAsync("Name", "World").join().getOrThrow().forEach(System.out::println);
    
  }
}
