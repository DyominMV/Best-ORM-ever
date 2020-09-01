package alyona.bestorm.storage.supporters;

public interface FieldSupporter<FieldType> extends Serializer<FieldType> {
  String getName();
  
  static <T> FieldSupporter<T> of(String name, Serializer<T> serializer) {
    return new FieldSupporter<T>() {

      @Override
      public String serialize(T t) {
        return serializer.serialize(t);
      }

      @Override
      public T deserialize(String string) {
        return serializer.deserialize(string);
      }

      @Override
      public String getName() {
        return name;
      }

      @Override
      public Class<T> getSerializedClass() {
        return serializer.getSerializedClass();
      }

    };
  }
}