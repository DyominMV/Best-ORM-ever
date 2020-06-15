package bestorm;

import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * @author Alyonka
 * 
 * @param <T> Тип данных, который мапится в отношение
 */
public interface Containable<T extends Identifiable> {

  /**
   * Изменить строку соответствующую данному объекту в зависимости от текущего состояния объекта
   * 
   * @return <tt>true</tt> в случае успеха, иначе <tt>false</tt>
   * @throws IllegalStateException Если объект удалён
   * @throws SQLException
   */
  public boolean update() throws IllegalStateException, SQLException;

  /**
   * Если объект получен из транзакции, модификация выполнится в момент выполнения транзакции. Иначе
   * модификация выполнится сразу после вызова данного метода
   * 
   * @param modifier модификатор
   * @return Очевидно
   * @throws IllegalStateException 
   * @throws SQLException
   */
  public boolean update(Consumer<T> modifier) throws IllegalStateException, SQLException;


  /**
   * Удалить строку соответствующую данному объекту
   * 
   * @return <tt>true</tt> в случае удаления, <tt>false</tt> если строка уже удалена
   * @throws SQLException
   */
  public boolean remove() throws SQLException;

  /**
   * Получить объект, в который мапится строка
   * 
   * @return
   * @throws IllegalStateException Когда объект получен из невыполненной транзакции
   * @throws SQLException
   */
  public T getValue() throws IllegalStateException, SQLException;

}
