package alyona.bestorm;

import java.util.function.Function;

public interface Serializer <T> {
  String serialize(T t);
  T deserialize(String string);

  public static <U> Serializer<U> of(
      Function<U, String> serializer, 
      Function<String,U> deserializer
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
        
      };
    }
}