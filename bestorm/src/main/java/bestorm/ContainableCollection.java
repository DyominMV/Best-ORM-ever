package bestorm;

import java.util.Collection;
import java.util.function.Consumer;

public interface ContainableCollection<T extends Identifiable> extends Collection<Containable<T>> {
  /**
   * Для отложенной обработки коллекции полученной из транзакции
   * 
   * @param processor
   */
  public void process(Consumer<ContainableCollection<T>> processor);
}