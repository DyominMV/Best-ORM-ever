package bestorm;

import java.lang.reflect.Field;

/**
 * Контракт ПОЛЬЗОВАТЕЛЬСКОГО КЛАССА
 */
public interface Identifiable {
  public Iterable<Field> getPrimaryKeys();
  
}