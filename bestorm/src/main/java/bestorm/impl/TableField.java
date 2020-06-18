package bestorm.impl;

public class TableField {
  public static enum FieldType {
    STRING, INT, SHORT // ...
  }

  private final FieldType type;
  private final String name;

  public TableField(FieldType type, String name) {
    this.type = type;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public FieldType getType() {
    return type;
  } 

}