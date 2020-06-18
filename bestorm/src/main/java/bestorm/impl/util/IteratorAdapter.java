package bestorm.impl.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * Позволяет построить итератор на основе существующего итератора не прибегая к использованию
 * анонимных классов
 * 
 * @param <From> тип - параметр исходного итератора
 * @param <To>   тип - параметр итератора - результата
 */
public class IteratorAdapter<From, To> implements Iterator<To> {

  private final Iterator<From> iterator;
  private final Function<From, To> transformer;

  /**
   * Создать итератор на основе существующего
   * 
   * @param iterator    итератор - источник
   * @param transformer функция преобразования результата метода <tt>next</tt>
   */
  public IteratorAdapter(Iterator<From> iterator, Function<From, To> transformer) {
    this.iterator = Objects.requireNonNull(iterator);
    this.transformer = Objects.requireNonNull(transformer);
  }

  /**
   * @see java.util.Iterator#hasNext()
   */
  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  /**
   * @see java.util.Iterator#next()
   */
  @Override
  public To next() {
    return transformer.apply(iterator.next());
  }

  /**
   * @see java.util.Iterator#remove()
   */
  @Override
  public void remove() {
    iterator.remove();
  }

}
