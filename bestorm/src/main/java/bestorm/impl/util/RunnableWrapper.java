package bestorm.impl.util;

/**
 * Поскольку в классе Destructable поле destructor является final, нужна возможность изменить его
 * поведение не меняя ссылки на сам объект (например, когда параметры для деструктора ещё не
 * получены) Такая возможность предоставляется данным классом
 * 
 * @author Михаил Дёмин
 */
public class RunnableWrapper implements Runnable {

  private Runnable action = null;

  /**
   * Создать новый экземпляр RunnableWrapper с заданным действием
   * 
   * @param action - дейтсвие,
   */
  public RunnableWrapper(Runnable action) {
    this.action = action;
  }

  /**
   * Создать пустой экземпляр RunnableWrapper
   */
  public RunnableWrapper() {
  }

  @Override
  public void run() {
    if (action != null) {
      action.run();
    }
  }

  /**
   * Задать действие, которое будет выполнено при вызове run()
   * 
   * @param action Новое действие, <tt>null</tt> будет обработан как <tt>()->{}</tt>
   */
  public void setAction(Runnable action) {
    this.action = action;
  }

}
