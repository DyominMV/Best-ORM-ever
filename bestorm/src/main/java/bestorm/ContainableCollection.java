package bestorm;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Позволяет просматривать результаты SELECT-запросов в виде объектов
 * 
 * @param <T> Объекты которые мапятся в отношения
 */
public interface ContainableCollection<T extends Identifiable> extends Collection<Containable<T>> {
  /**
   * Для отложенной обработки коллекции полученной из транзакции
   * 
   * @param processor обработчик, выполняемый сразу после выполнения транзакции
   */
  public void process(Consumer<ContainableCollection<T>> processor);
}