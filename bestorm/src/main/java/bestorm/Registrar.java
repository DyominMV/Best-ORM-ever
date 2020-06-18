package bestorm;

import java.sql.Connection;
import bestorm.impl.Table;

/**
 * Позволяет при необходимости создать отношения соответствующие объектам и использовать их для
 * хранения данных об объектах
 */
public class Registrar extends RegistrarBase {

  protected Registrar(Iterable<Class<? extends Identifiable>> classes, Connection connection)
      throws Exception {
    super(classes, connection);
    checkIntegrity();
  }

  public Connection getConnection(){
    return connection;
  }

  private void checkIntegrity(){
    // TODO triggers  
  }

  @Override
  protected void registerClass(Class<? extends Identifiable> classObject) {
    // TODO check tabe exists ? (Is okay ? OK : throw exception OR drop tabe) : create table

  }

  @Override
  public Transaction createTransaction() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T extends Identifiable> ContainableObjectFactory<T> createFactory() {
    // TODO Auto-generated method stub
    return null;
  }

  public <T extends Identifiable> Table<T> getTable(Class<T> classObject){
    // TODO : return name of the table
    return null;
  }

}
