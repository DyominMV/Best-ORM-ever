package bestorm;

/**
 * Позволяет при необходимости создать отношения соответствующие объектам и использовать их для
 * хранения данных об объектах
 */
public abstract class Registrar {

  /**
   * при создании мы должны зарегистрировать обрабатываемые классы
   * 
   * @param classes список классов которые регистрируются для хранения в БД
   */
  Registrar(Iterable<Class<? extends Identifiable>> classes) {
    // TODO register classes and check thier tables in database
  }

  /**
   * Создать транзакцию для последующего выполнения
   * 
   * @return объект транзакции
   */
  public abstract Transaction createTransaction();

  /**
   * Получить фабрику, предоставляющую доступ к объектам, полученным из отношений БД
   * 
   * @param <T> тип получаемых объектов
   */
  public abstract <T extends Identifiable> ContainableObjectFactory<T> createFactory();

}
