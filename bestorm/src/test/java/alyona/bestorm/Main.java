package alyona.bestorm;

import alyona.bestorm.storage.IStorage;
import alyona.bestorm.storage.Storage;
import static alyona.bestorm.storage.filter.Filter.*;


public class Main {
  public static void main(String[] args) {
    IStorage<StoredData> someStorage = new Storage<>(StoredData.class, null);

    someStorage.getStreamAsync(
      OR(
        GREATER(
          NUMBER(10), 
          someStorage.getValueByOriginalName("SomeInt")
        ),
        LESS(
          NUMBER(10), 
          someStorage.getValueByOriginalName("SomeInt")
        )
      )
    ).thenAccept(stream->stream.map(Object::toString).forEach(System.out::println));
  }
}
