package bestorm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import bestorm.impl.Table;
import bestorm.impl.util.Destructable;

/**
 * Позволяет при необходимости создать отношения соответствующие объектам и использовать их для
 * хранения данных об объектах
 */
public class Registrar extends Destructable {

  private final Connection connection;
  private final Map<Class<? extends Identifiable>, Table<? extends Identifiable>> classMap;

  public Registrar(Iterable<Class<? extends Identifiable>> classes, Connection connection)
      throws Exception {
    super(() -> {
      try {
        if (!connection.isClosed())
          connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    this.connection = Objects.requireNonNull(connection);
    classMap = new HashMap<>();
    for (Class<? extends Identifiable> classObject : classes) {
      classMap.put(classObject, new Table(connection, classObject));
    }
    checkIntegrity();
  }

  private void checkIntegrity() {
    // TODO triggers
  }

  public Connection getConnection() {
    return connection;
  }

  public Transaction createTransaction() {
    // TODO Auto-generated method stub
    return null;
  }

  public <T extends Identifiable> ContainableObjectFactory<T> createFactory() {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("unchecked")
  public <T extends Identifiable> Table<T> getTable(Class<T> classObject) {
    return (Table<T>)classMap.get(classObject);
  }

}
