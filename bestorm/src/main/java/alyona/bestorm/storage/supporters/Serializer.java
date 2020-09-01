package alyona.bestorm.storage.supporters;

import java.util.function.Function;

public interface Serializer <T> {
  String serialize(T t);
  T deserialize(String string);
  Class<T> getSerializedClass();

  public static <U> Serializer<U> of(
      Function<U, String> serializer, 
      Function<String,U> deserializer,
      Class<U> serializedClass
    ){
      return new Serializer<U>(){

        @Override
        public String serialize(U t) {
          return serializer.apply(t);
        }

        @Override
        public U deserialize(String string) {
          return deserializer.apply(string);
        }

        @Override
        public Class<U> getSerializedClass() {
          return serializedClass;
        }
        
      };
    }
}