package bestorm.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import bestorm.Containable;
import bestorm.Identifiable;
import bestorm.impl.exceptions.WrongTableException;

public class Row<T extends Identifiable> implements Containable<T> {

  public Row(ResultSet resultSet) throws WrongTableException{
    // TODO сконструировать объект строки таблицы из resultSet
    // НЕ СДВИГАТЬ КУРСОР ResultSet-а
  }

  @Override
  public boolean update() throws IllegalStateException, SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean update(Consumer<T> modifier) throws IllegalStateException, SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean remove() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public T getValue() throws IllegalStateException, SQLException {
    // TODO Auto-generated method stub
    return null;
  }
  
}