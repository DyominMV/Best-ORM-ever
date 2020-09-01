package alyona.bestorm.exception;

public class BadlyAnnotatedException extends RuntimeException{
  private static final long serialVersionUID = 1L;

  public BadlyAnnotatedException(String message) {
    super(message);
  }
}