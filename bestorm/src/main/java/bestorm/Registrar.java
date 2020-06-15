package bestorm;

import java.util.List;

public abstract class Registrar {
  Registrar(List<Class<? extends Identifiable>> classes){
    // TODO register classes and check thier tables in database
  }

  public abstract Transaction getTransaction();

  public abstract <T extends Identifiable> ContainableObjectFactory<T> getFactory();

}