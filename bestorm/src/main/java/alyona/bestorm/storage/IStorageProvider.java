package alyona.bestorm.storage;

public interface IStorageProvider {
  public <T> IStorage<T> getStorage(Class<? extends T> storedClass);
}