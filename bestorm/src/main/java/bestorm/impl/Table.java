package bestorm.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import bestorm.Containable;
import bestorm.ContainableCollection;
import bestorm.ContainableObjectFactory;
import bestorm.Registrar;
import bestorm.filters.ValueFilter;

public class Table<T> implements ContainableObjectFactory<T> {

  public class TableField {
    private final Field field;
    private final String name;
    private final FieldType type;
    private final TableField referent;

    public Table<T> getTable() {
      return Table.this;
    }

    public TableField getReferent() {
      return referent;
    }

    public FieldType getType() {
      return type;
    }

    public String getName() {
      return name;
    }

    public TableField(Field field) {
      this.field = field;
      // TODO add some reflection and foreign-key stuff
      name = null;
      type = null;
      referent = null;
    }
  }

  private final Class<T> classObject;
  private final Registrar registrar;
  private final ArrayList<TableField> fields = new ArrayList<>();
  private final boolean hasSurrogateKey;

  @Override
  public Containable<T> get() throws SQLException {
    // TODO create prepared statement and return Row<T> (statement.getResultSet, this)
    return null;
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

  public Table(Registrar registrar, Class<T> classObject) {
    this.registrar = registrar;
    this.classObject = classObject;
    // TODO fill fields (some reflection stuff) (c)Alena
    this.hasSurrogateKey = false; // CHANGE
  }

}
