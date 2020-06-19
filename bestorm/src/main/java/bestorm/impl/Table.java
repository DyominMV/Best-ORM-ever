package bestorm.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import bestorm.Containable;
import bestorm.ContainableCollection;
import bestorm.ContainableObjectFactory;
import bestorm.filters.ValueFilter;

public class Table<T> implements ContainableObjectFactory<T> {

  private final Connection connection;
  private final ArrayList<TableField> fields = new ArrayList<>();
  private final boolean hasSurrogateKey;

  @Override
  public Containable<T> get() throws SQLException {
    // TODO create prepared statement and return Row<T> (statement.getResultSet, this)
    return null;
  }

  public Connection getConnection() {
    return connection;
  }

  @Override
  public Containable<T> get(ValueFilter<T> filter) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ContainableCollection<T> select(ValueFilter<T> filter) throws SQLException {
    // TODO create prepared statement and return RowSet<T> (statement, this)
    return null;
  }

  public Table(Connection connection, Class<T> classObject) {
    this.connection = connection;
    // TODO fill fields (some reflection stuff) (c)Alena
    this.hasSurrogateKey = false; // CHANGE
  }

}
