package alyona.bestorm.storage.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Result<ResultType> {
  boolean isError();

  default boolean isSuccess(){
    return !isError();
  }

  <U> Result<U> map(Function<ResultType, U> mapper);

  <U> Result<U> flatMap(Function<ResultType, Result<U>> mapper);

  void accept(Consumer<ResultType> successConsumer, Consumer<Throwable> errorConsumer);

  ResultType getOrNull();

  Throwable errorOrNull();

  ResultType getOrThrow();

  Optional<ResultType> toOptional();
}