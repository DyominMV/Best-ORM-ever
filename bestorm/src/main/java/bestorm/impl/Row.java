package bestorm.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import bestorm.Containable;
import bestorm.Identifiable;
import bestorm.impl.exceptions.WrongTableException;

public class Row<T extends Identifiable> implements Containable<T> {

  private final Table<T> table;

  public Row(ResultSet resultSet, Table<T> table) throws WrongTableException{
    this.table = table;
    // TODO сконструировать объект строки таблицы из resultSet
    // НЕ СДВИГАТЬ КУРСОР ResultSet-а
  }

  @Override
  public boolean update() throws IllegalStateException, SQLException {
    // Update Table.getName SET ( table.field(1).toString() = object.field(1).value)
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