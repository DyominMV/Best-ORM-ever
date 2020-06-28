package bestorm;

import org.junit.Test;

class A{
  int b;
}

public class OrmTest {

  @Test
  public void goodTest(){
    System.out.println(A.class.getDeclaredFields()[0].getType() == int.class);

  }
  
}