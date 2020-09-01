package alyona.bestorm.storage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import alyona.bestorm.storage.result.Result;

public interface IRedisStorage<DataClass, KeyClass> {

  CompletableFuture<Result<Optional<DataClass>>> findByKeyAsync(KeyClass key);

  <IndexClass> CompletableFuture<Result<Iterable<DataClass>>> findByIndexAsync(String indexName,
      IndexClass indexValue);

  default <IndexClass> CompletableFuture<Result<Stream<DataClass>>> findByIndexAsStreamAsync(
      String indexName, IndexClass indexValue) {
    return findByIndexAsync(indexName, indexValue).thenApply(iterableResult -> iterableResult
        .map(iterable -> StreamSupport.stream(iterable.spliterator(), false)));
  }

  CompletableFuture<Result<Iterable<DataClass>>> getAllAsync();

  default CompletableFuture<Result<Stream<DataClass>>> getAllAsStreamAsync() {
    return getAllAsync().thenApply(iterableResult -> iterableResult
        .map(iterable -> StreamSupport.stream(iterable.spliterator(), false)));
  }

  CompletableFuture<Result<Boolean>> removeByKeyAsync(KeyClass key);

  CompletableFuture<Result<Boolean>> removeAsync(DataClass data);

  CompletableFuture<Result<Boolean>> putAsync(DataClass data);

  CompletableFuture<Result<DataClass>> mergeAsync(DataClass data);

}
