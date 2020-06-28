package bestorm.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import bestorm.Containable;
import bestorm.ContainableCollection;
import bestorm.ContainableObjectFactory;
import bestorm.Primary;
import bestorm.Registrar;
import bestorm.filters.ValueFilter;

public class Table<T> implements ContainableObjectFactory<T> {

  private static final String SURROGATE_KEY_NAME = "ID";

  public class TableField {
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

    /**
     * Для обычного поля
     * 
     * @param field
     */
    public TableField(Field field) {
      // TODO add some reflection and foreign-key stuff
      name = null;
      type = null;
      referent = null;
    }

    /**
     * Для суррогатного ключа
     * 
     * @param name название
     * @param type тип
     */
    public TableField(String name, FieldType type) {
      this.name = name;
      this.type = type;
      this.referent = null;
    }
  }

  private final Class<T> classObject;
  private final Registrar registrar;
  private final Map<Field, TableField> declaredFields = new HashMap<>();
  private final ArrayList<TableField> primaryKeys = new ArrayList<>();
  private final String name;

  @Override
  public Containable<T> get() throws SQLException {
    // TODO create prepared statement and return Row<T> (statement.getResultSet, this)
    return null;
  }

  @Override
  public Containable<T> get(ValueFilter<T> filter) throws SQLException {
    // TODO create prepared statement and return Row<T> (statement.getResultSet, this)
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
    this.name = classObject.getName();
    for (Field field : classObject.getDeclaredFields()) {
      TableField tableField =  this.declaredFields.put(field, new TableField(field));
      if (field.isAnnotationPresent(Primary.class)){
        this.primaryKeys.add(tableField);
      }
    }
    if (primaryKeys.isEmpty()){
      primaryKeys.add(new TableField(SURROGATE_KEY_NAME, FieldType.INT));
    }
  }

}
