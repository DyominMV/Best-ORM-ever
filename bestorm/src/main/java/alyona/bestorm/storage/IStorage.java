package alyona.bestorm.storage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import alyona.bestorm.storage.filter.Filter;

public interface IStorage<T> {

  CompletableFuture<Iterable<T>> getAllAsync(Filter<T, Boolean> filter);

  default CompletableFuture<Iterable<T>> getAllAsync() {
    return getAllAsync(null);
  }

  default CompletableFuture<Stream<T>> getStreamAsync(boolean parallel, Filter<T, Boolean> filter) {
    return getAllAsync(filter)
        .thenApply(iterable -> StreamSupport.stream(iterable.spliterator(), parallel));
  }

  default CompletableFuture<Stream<T>> getParallelStreamAsync(Filter<T, Boolean> filter) {
    return getStreamAsync(true, filter);
  }

  default CompletableFuture<Stream<T>> getStreamAsync(Filter<T, Boolean> filter) {
    return getStreamAsync(false, filter);
  }

  default CompletableFuture<Stream<T>> getParallelStreamAsync() {
    return getStreamAsync(true, null);
  }

  default CompletableFuture<Stream<T>> getStreamAsync() {
    return getStreamAsync(false, null);
  }

  CompletableFuture<Optional<Exception>> persistAsync(T object);

  CompletableFuture<Optional<Exception>> mergeAsync(T object);

  CompletableFuture<Optional<Exception>> removeAsync(T object);

  CompletableFuture<Optional<Exception>> removeAsync(Filter<T, Boolean> filter);

  <V> IFieldWithName<V> getFieldByDbName(String name);

  <V> IFieldWithName<V> getFieldByOriginalName(String name);

  default <V> Filter<T, V> getValueByDbName(String name) {
    return Filter.of(getFieldByDbName(name));
  }

  default <V> Filter<T, V> getValueByOriginalName(String name) {
    return Filter.of(getFieldByOriginalName(name));
  }

}
