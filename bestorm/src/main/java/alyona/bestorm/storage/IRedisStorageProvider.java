package alyona.bestorm.storage;

public interface IRedisStorageProvider {
  <Data, Key> IRedisStorage<Data, Key> getStorage(Class<Data> dataClass, Class<Key> keyClass);
}