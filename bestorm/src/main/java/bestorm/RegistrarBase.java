package bestorm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import bestorm.impl.util.Destructable;

/**
 * Позволяет при необходимости создать отношения соответствующие объектам и использовать их для
 * хранения данных об объектах
 */
public abstract class RegistrarBase extends Destructable {

  protected final Connection connection;

  /**
   * Метод должен проверить наличие и свойства таблиц и триггеров, связанных с данным классом.
   * 
   * @param classObject Класс для регистрации
   */
  protected abstract void registerClass(Class<? extends Identifiable> classObject) throws Exception;

  /**
   * при создании мы должны зарегистрировать обрабатываемые классы
   * 
   * @param classes список классов которые регистрируются для хранения в БД
   * @throws Exception
   */
  protected RegistrarBase(Iterable<Class<? extends Identifiable>> classes, Connection connection)
      throws Exception {
    super(() -> {
      try {
        if (!connection.isClosed())
          connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    this.connection = Objects.requireNonNull(connection);
    for (Class<? extends Identifiable> classObject : classes) {
      registerClass(classObject);
    }
  }

  /**
   * Создать транзакцию для последующего выполнения
   * 
   * @return объект транзакции
   */
  public abstract Transaction createTransaction() throws Exception;

  /**
   * Получить фабрику, предоставляющую доступ к объектам, полученным из отношений БД
   * 
   * @param <T> тип получаемых объектов
   */
  public abstract <T extends Identifiable> ContainableObjectFactory<T> createFactory() throws Exception;

}
