package alyona.bestorm.storage.supporters;

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Serializers {
  public static Serializer<Boolean> forBoolean() {
    return new Serializer<Boolean>() {

      @Override
      public String serialize(Boolean t) {
        if (null == t) {
          return null;
        } else if (true == t) {
          return "TRUE";
        } else if (false == t) {
          return "FALSE";
        } else {
          throw new RuntimeException("Unreachable code");
        }
      }

      @Override
      public Boolean deserialize(String string) {
        if (null == string) {
          return null;
        } else if ("TRUE".equals(string)) {
          return true;
        } else if ("FALSE".equals(string)) {
          return false;
        } else {
          throw new RuntimeException(
              "Error while parsing Boolean value. presented value: " + string);
        }
      }

      @Override
      public Class<Boolean> getSerializedClass() {
        return Boolean.class;
      }
    };
  }

  public static Serializer<Short> forShort() {
    return new Serializer<Short>() {

      @Override
      public String serialize(Short t) {
        return null == t ? null : t.toString();
      }

      @Override
      public Short deserialize(String string) {
        return null == string ? null : Short.parseShort(string);
      }

      @Override
      public Class<Short> getSerializedClass() {
        return Short.class;
      }

    };
  }

  public static Serializer<Integer> forInteger() {
    return new Serializer<Integer>() {

      @Override
      public String serialize(Integer t) {
        return null == t ? null : t.toString();
      }

      @Override
      public Integer deserialize(String string) {
        return null == string ? null : Integer.parseInt(string);
      }

      @Override
      public Class<Integer> getSerializedClass() {
        return Integer.class;
      }

    };
  }

  public static Serializer<Long> forLong() {
    return new Serializer<Long>() {

      @Override
      public String serialize(Long t) {
        return null == t ? null : t.toString();
      }

      @Override
      public Long deserialize(String string) {
        return null == string ? null : Long.parseLong(string);
      }

      @Override
      public Class<Long> getSerializedClass() {
        return Long.class;
      }

    };
  }

  public static Serializer<String> forString() {
    return new Serializer<String>() {

      @Override
      public String serialize(String t) {
        return t;
      }

      @Override
      public String deserialize(String string) {
        return string;
      }

      @Override
      public Class<String> getSerializedClass() {
        return String.class;
      }

    };
  }

  public static <E> Serializer<E> forEnum(Class<E> enumClass) {
    if (!enumClass.isEnum()) {
      throw new RuntimeException(
          "Cannot get enum-compatible serializer for non-enum type : " + enumClass);
    }

    return new Serializer<E>() {

      private final E[] enumConstants = enumClass.getEnumConstants();

      @Override
      public String serialize(E t) {
        return ((Enum<?>) t).name();
      }

      @Override
      public E deserialize(String string) {
        return null == string ? null
            : Arrays.stream(enumConstants).filter(c -> ((Enum<?>) c).name().equals(string))
                .findFirst().orElseThrow(() -> new RuntimeException(
                    "Error while parsing " + string + " as value of enum " + enumClass));
      }

      @Override
      public Class<E> getSerializedClass() {
        return enumClass;
      }

    };
  }

  private static ConcurrentMap<Class<?>, Serializer<?>> defaultSerializers =
      Stream.of(forBoolean(), forShort(), forInteger(), forLong(), forString())
          .collect(Collectors.toConcurrentMap(s -> s.getSerializedClass(), s -> s));

  public static Serializer<?> getDefaultSerializer(Class<?> aClass) {
    if (aClass.isEnum()) {
      return forEnum(aClass);
    }

    return defaultSerializers.get(aClass);
  }
}
