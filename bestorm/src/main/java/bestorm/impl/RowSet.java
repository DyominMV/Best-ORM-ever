package bestorm.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import bestorm.Identifiable;
import bestorm.impl.util.Destructable;

public class RowSet<T extends Identifiable> extends Destructable implements Collection<Row<T>> {

  private static String ERROR_MESSAGE = "This collection is unmodifiable";

  @Override
  public boolean add(Row<T> arg0) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public boolean addAll(Collection<? extends Row<T>> arg0) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public boolean remove(Object arg0) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public boolean removeAll(Collection<?> arg0) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  @Override
  public boolean retainAll(Collection<?> arg0) {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  private final ResultSet resultSet;
  private final Collection<Row<T>> readyRows;

  @Override
  public boolean contains(Object arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Iterator<Row<T>> iterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int size() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Object[] toArray() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T[] toArray(T[] arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  public RowSet(final ResultSet resultSet) {
    super(() -> {
      try {
        if (!resultSet.isClosed()) {
          resultSet.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }, true);
    this.resultSet = resultSet;
    readyRows = new ArrayList<Row<T>>();
  }

}
