package bestorm.impl;

import java.sql.Connection;
import java.sql.SQLException;
import bestorm.Containable;
import bestorm.ContainableCollection;
import bestorm.ContainableObjectFactory;
import bestorm.Identifiable;
import bestorm.filters.ValueFilter;

public class Table<T extends Identifiable> implements ContainableObjectFactory<T> {

  @Override
  public Containable<T> get() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Containable<T> get(ValueFilter<T> filter) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ContainableCollection<T> select(ValueFilter<T> filter) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  public Table(Connection connection) {

  }

}
