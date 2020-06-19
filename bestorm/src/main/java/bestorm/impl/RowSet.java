package bestorm.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.function.Consumer;
import bestorm.Containable;
import bestorm.ContainableCollection;
import bestorm.impl.exceptions.WrongTableException;
import bestorm.impl.util.Destructable;
import bestorm.impl.util.RunnableWrapper;

public class RowSet<T> extends Destructable
    implements ContainableCollection<T> {

  private final ResultSet resultSet;
  private final Table<T> table;

  public RowSet(final PreparedStatement statement, Table<T> table) throws SQLException {
    super(new RunnableWrapper());
    this.table = table;
    statement.execute();
    final ResultSet resultSet = statement.getResultSet();
    ((RunnableWrapper) this.getDestructor()).setAction(() -> {
      try {
        if (!resultSet.isClosed()) {
          resultSet.close();
        }
        if (!statement.isClosed()) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    this.resultSet = resultSet;
  }

  @Override
  public Iterator<Containable<T>> iterator() {
    return new Iterator<Containable<T>>() {

      private final ResultSet rSet = resultSet;

      {
        next();
      }

      private Row<T> next = null;

      @Override
      public boolean hasNext() {
        return (next != null);
      }

      @Override
      public Containable<T> next() {
        Containable<T> current = next;
        try {
          if (resultSet.next()) {
            next = new Row<T>(rSet, table);
          } else {
            next = null;
          }
        } catch (SQLException | WrongTableException e) {
          next = null;
        }
        return current;
      }
    };
  }

  @Override
  public void process(Consumer<ContainableCollection<T>> processor) {
    processor.accept(this);
  }

}
