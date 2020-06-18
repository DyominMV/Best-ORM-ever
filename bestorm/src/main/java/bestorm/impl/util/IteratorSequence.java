package bestorm.impl.util;

import java.util.Collections;
import java.util.Iterator;

/**
 * Позволяет построить итератор на основе нескольких итераторов с тем же
 * типом-параметром. Как только у очередного исходного итератора <tt>hasNext</tt>
 * возвращает <tt>false</tt>, начинается перебор значений следующего итератора
 * 
 * @param <T> тип - параметр исходных итераторов
 */
public class IteratorSequence<T> implements Iterator<T> {

  private final Iterator<Iterator<T>> iterators;
  private Iterator<T> currentIterator;
  private boolean knowHasNext;
  private boolean hasNext;

  /**
   * Создать итератор на основе нескольких существующих итераторов
   * 
   * @param iterators набор исходных итераторов
   */
  public IteratorSequence(final Iterable<Iterator<T>> iterators) {
    this.iterators = iterators.iterator();
    this.currentIterator = Collections.<T>emptyIterator();
    this.knowHasNext = false;
    this.hasNext();
  }

  /**
   * @see java.util.Iterator#hasNext()
   */
  @Override
  public boolean hasNext() {
    if (!knowHasNext) {
      knowHasNext = true;
      if (currentIterator.hasNext()) {
        hasNext = true;
      } else {
        while (iterators.hasNext()) {
          currentIterator = iterators.next();
          if (currentIterator.hasNext()) {
            hasNext = true;
            break;
          }
        }
        if (currentIterator.hasNext() == false) {
          hasNext = false;
        }
      }
    }
    return hasNext;
  }

  /**
   * @see java.util.Iterator#next()
   */
  @Override
  public T next() {
    knowHasNext = false;
    return currentIterator.next();
  }

  /**
   * @see java.util.Iterator#remove()
   */
  @Override
  public void remove() {
    currentIterator.remove();
  }

}