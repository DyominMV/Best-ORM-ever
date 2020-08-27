package alyona.bestorm.exception;

import java.lang.reflect.Field;

public class FieldCannotBeStoredException extends RuntimeException {
  public FieldCannotBeStoredException(Field field) {
    super("Field " + field + " of type " + field.getType()
        + " cannot be stored. type is not primitive or not marked as Stored");
  }

}
