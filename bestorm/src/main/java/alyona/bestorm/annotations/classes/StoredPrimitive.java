package alyona.bestorm.annotations.classes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import alyona.bestorm.Serializer;

@Retention(RetentionPolicy.RUNTIME)
public @interface StoredPrimitive{
  Class<? extends Serializer<?>> serializer();
}