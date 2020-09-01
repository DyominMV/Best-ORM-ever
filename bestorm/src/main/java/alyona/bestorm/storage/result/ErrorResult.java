package alyona.bestorm.storage.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ErrorResult<ResultType> implements Result<ResultType> {

  private final Throwable error;

  public ErrorResult(Throwable error) {
    this.error = error;
  }

  @Override
  public boolean isError() {
    return true;
  }

  @Override
  public <U> Result<U> map(Function<ResultType, U> mapper) {
    return new ErrorResult<>(error);
  }

  @Override
  public <U> Result<U> flatMap(Function<ResultType, Result<U>> mapper) {
    return new ErrorResult<>(error);
  }

  @Override
  public void accept(Consumer<ResultType> successConsumer, Consumer<Throwable> errorConsumer) {
    errorConsumer.accept(error);
  }

  @Override
  public ResultType getOrNull() {
    return null;
  }

  @Override
  public Throwable errorOrNull() {
    return error;
  }

  @Override
  public ResultType getOrThrow() {
    throw new RuntimeException(error);
  }

  @Override
  public Optional<ResultType> toOptional() {
    return Optional.empty();
  }

}