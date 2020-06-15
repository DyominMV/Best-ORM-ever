package bestorm;

/**
 * Всё то же самое но действия выполняются в транзации
 */
public interface Transaction {
  /**
   * Получить фабрику 
   * 
   * @param <T> 
   * @return
   */
  public <T extends Identifiable> ContainableObjectFactory<T> getFactory();

  /**
   * Выполнить транзакцию
   */
  public void execute();

  /**
   * откатиться к предыдущему состоянию
   */
  public void rollback();
}
