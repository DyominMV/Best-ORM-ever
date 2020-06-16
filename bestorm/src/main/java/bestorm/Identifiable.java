package bestorm;

import java.lang.reflect.Field;

/**
 * Контракт ПОЛЬЗОВАТЕЛЬСКОГО КЛАССА
 * 
 * Служит для корректной генерации внутренних и внешних ключей в БД
 */
public interface Identifiable {
  public Iterable<Field> getPrimaryKeys();
  
}