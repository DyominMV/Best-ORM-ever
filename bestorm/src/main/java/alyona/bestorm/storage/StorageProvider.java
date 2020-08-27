package alyona.bestorm.storage;

import java.sql.Connection;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class StorageProvider implements IStorageProvider {

  private final ConcurrentHashMap<Class<?>, IStorage<?>> storages = new ConcurrentHashMap<>();

  private final Connection connection;

  @Override
  @SuppressWarnings("unchecked")
  public <T> IStorage<T> getStorage(Class<? extends T> storedClass) {
    return (IStorage<T>) storages.putIfAbsent(storedClass, new Storage<>(storedClass, connection));
  }

  public Collection<IStorage<?>> getAllStorages(){
    return storages.values();
  }

  public StorageProvider(Connection connection) {
    this.connection = connection;
  }
  
}