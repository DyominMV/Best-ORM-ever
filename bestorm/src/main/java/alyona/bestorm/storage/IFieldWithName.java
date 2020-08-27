package alyona.bestorm.storage;

import java.lang.reflect.Field;

public interface IFieldWithName<T> {
  String getName();

  String getFullName();

  Field getField();

  Class<T> getFieldClass();
}
