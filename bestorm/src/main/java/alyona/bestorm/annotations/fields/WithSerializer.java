package alyona.bestorm.annotations.fields;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import alyona.bestorm.storage.supporters.Serializer;

@Retention(RetentionPolicy.RUNTIME)
public @interface WithSerializer{
  Class<? extends Serializer<?>> serializer();
}