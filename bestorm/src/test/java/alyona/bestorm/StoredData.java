package alyona.bestorm;

import alyona.bestorm.annotations.classes.StoredTable;
import alyona.bestorm.annotations.fields.NamedField;
import alyona.bestorm.annotations.fields.PrimaryKey;

@StoredTable(tableName = "StoredData")
public class StoredData {
  @PrimaryKey
  @NamedField(name = "SomeInt")
  Integer someInt;

  @NamedField(name = "SomeOtherInt")
  Integer otherInt;
}