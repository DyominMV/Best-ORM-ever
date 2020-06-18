package bestorm.impl.exceptions;

public class WrongTableException extends Exception {

  private static final long serialVersionUID = 1L;

  public WrongTableException(String tableName, String typeName) {
    super("cannot construct " + typeName + " object from the row of " + tableName + " table.");
  }

}
