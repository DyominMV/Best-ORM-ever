package bestorm;

import org.junit.Test;

class A{
  String b;
}

public class OrmTest {

  @Test
  public void goodTest(){
    System.out.println(A.class.getDeclaredFields()[0].getType().isPrimitive());

  }
  
}