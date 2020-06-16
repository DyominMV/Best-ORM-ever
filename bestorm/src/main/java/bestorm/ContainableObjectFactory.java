package bestorm;

import java.sql.SQLException;
import bestorm.filters.ValueFilter;

/**
 * Служит для получения объектов из БД
 * 
 * @param <T> тип объектов которые мапятся в отношения
 */
public interface ContainableObjectFactory<T extends Identifiable> {

  /**
   * Создать запись в таблице и получить экземпляр Containable
   * 
   * @return соответствующий новой записи экземпляр Containable 
   * @throws SQLException
   */
  public Containable<T> get() throws SQLException;
  
  /**
   * Выбрать первую подходящую запись из таблицы
   * 
   * @param filter фильтр для выбора записей
   * @return соответствующий полученной записи экземпляр Containable
   * @throws SQLException
   */
  public Containable<T> get(ValueFilter<T> filter) throws SQLException;

  /**
   * Выбрать набор подходящих записей из таблицы
   * 
   * @param filter фильтр для выбора записей
   * @return набор Containable
   * @throws SQLException
   */
  public ContainableCollection<T> select(ValueFilter<T> filter) throws SQLException;
}