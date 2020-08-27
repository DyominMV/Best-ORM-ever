package alyona.bestorm.storage;

import java.sql.Connection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import alyona.bestorm.storage.filter.Filter;

public class Storage<T> implements IStorage<T> {

  private final Class<T> storedClass;
  private final Connection connection;

  public Storage(Class<T> storedClass, Connection connection) {
    this.storedClass = storedClass;
    this.connection = connection;
  }

  @Override
  public CompletableFuture<Iterable<T>> getAllAsync(Filter<T, Boolean> filter) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<Optional<Exception>> persistAsync(T object) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<Optional<Exception>> mergeAsync(T object) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<Optional<Exception>> removeAsync(T object) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<Optional<Exception>> removeAsync(Filter<T, Boolean> filter) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <V> IFieldWithName<V> getFieldByDbName(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <V> IFieldWithName<V> getFieldByOriginalName(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  
}