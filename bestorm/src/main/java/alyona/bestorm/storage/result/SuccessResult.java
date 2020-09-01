package alyona.bestorm.storage.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SuccessResult<ResultType> implements Result<ResultType> {

  private final ResultType result;

  public SuccessResult(ResultType result) {
    this.result = result;
  }

  @Override
  public <U> Result<U> map(Function<ResultType, U> mapper) {
    return new SuccessResult<U>(mapper.apply(result));
  }

  @Override
  public <U> Result<U> flatMap(Function<ResultType, Result<U>> mapper) {
    Result<U> primaryResult = mapper.apply(result);

    if (null == primaryResult) {
      return new ErrorResult<U>(new NullPointerException("got null as mapper result"));
    } else {
      return primaryResult;
    }
  }

  @Override
  public boolean isError() {
    return false;
  }

  @Override
  public void accept(Consumer<ResultType> successConsumer, Consumer<Throwable> errorConsumer) {
    successConsumer.accept(result);
  }

  @Override
  public ResultType getOrNull() {
    return result;
  }

  @Override
  public Throwable errorOrNull() {
    return null;
  }

  @Override
  public ResultType getOrThrow() {
    return result;
  }

  @Override
  public Optional<ResultType> toOptional() {
    return Optional.ofNullable(result);
  }
  
}