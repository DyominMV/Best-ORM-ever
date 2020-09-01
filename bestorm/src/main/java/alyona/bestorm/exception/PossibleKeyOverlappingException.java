package alyona.bestorm.exception;

public class PossibleKeyOverlappingException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  public PossibleKeyOverlappingException(String message){
    super(message);
  }

}