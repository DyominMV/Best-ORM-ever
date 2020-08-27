package alyona.bestorm.storage.filter;

import java.util.function.BiFunction;
import alyona.bestorm.storage.IFieldWithName;

public interface Filter<StoredType, ValueType> {
  String stringify();

  ValueType getValue(StoredType storedData);

  public static <StoredType> Filter<StoredType, Object> NULL() {
    return new Filter<StoredType, Object>() {

      @Override
      public String stringify() {
        return "NULL";
      }

      @Override
      public Object getValue(StoredType storedData) {
        return null;
      }

    };
  }

  public static <StoredType, ValueType> Filter<StoredType, ValueType> of(
      IFieldWithName<ValueType> namedField) {
    return new Filter<StoredType, ValueType>() {

      @Override
      public String stringify() {
        return namedField.getFullName();
      }

      @Override
      public ValueType getValue(StoredType t) {
        try {

          if (namedField.getField().trySetAccessible()) {
            ValueType result = namedField.getFieldClass().cast(namedField.getField().get(t));
            return result;
          }

        } catch (Exception e) {
          throw new RuntimeException(e);
        }

        throw new RuntimeException("Cannot access field " + namedField.getField());
      }

    };
  }

  public static <T, SourceType, ResultType> Filter<T, ResultType> infixOperator(
      String operatorString, BiFunction<SourceType, SourceType, ResultType> operator,
      Filter<T, SourceType> left, Filter<T, SourceType> right) {
    return new Filter<T, ResultType>() {

      @Override
      public String stringify() {
        return String.format("(%s %s %s)", left.stringify(), operatorString, right.stringify());
      }

      @Override
      public ResultType getValue(T storedData) {
        SourceType leftValue = left.getValue(storedData);

        if (null == leftValue) {
          return null;
        }

        SourceType rightValue = right.getValue(storedData);

        if (null == rightValue) {
          return null;
        }

        return operator.apply(leftValue, rightValue);
      }

    };
  }

  public static <T, SourceType, ResultType> Filter<T, ResultType> biFunction(
      String functionName, BiFunction<SourceType, SourceType, ResultType> operator,
      Filter<T, SourceType> left, Filter<T, SourceType> right) {
    return new Filter<T, ResultType>() {

      @Override
      public String stringify() {
        return String.format("%s (%s, %s)", functionName, left.stringify(), right.stringify());
      }

      @Override
      public ResultType getValue(T storedData) {
        SourceType leftValue = left.getValue(storedData);

        if (null == leftValue) {
          return null;
        }

        SourceType rightValue = right.getValue(storedData);

        if (null == rightValue) {
          return null;
        }

        return operator.apply(leftValue, rightValue);
      }

    };
  }

  public static <StoredType, ValueType> Filter<StoredType, Boolean> equal(
      Filter<StoredType, ValueType> left, Filter<StoredType, ValueType> right) {
    return infixOperator("=", (a, b) -> a.equals(b), left, right);
  }

  public static <T> Filter<T, Number> NUMBER(Number n) {
    return new Filter<T, Number>() {

      @Override
      public String stringify() {
        return n.toString();
      }

      @Override
      public Number getValue(T t) {
        return n;
      }
    };
  }

  public static <T> Filter<T, Boolean> BOOLEAN(Boolean b) {
    return new Filter<T, Boolean>() {

      @Override
      public String stringify() {
        return b.toString().toUpperCase();
      }

      @Override
      public Boolean getValue(T t) {
        return b;
      }
    };
  }

  public static <T> Filter<T, String> STRING(String string) {
    return new Filter<T, String>() {

      @Override
      public String stringify() {
        return String.format("'%s'", string);
      }

      @Override
      public String getValue(T t) {
        return string;
      }
    };
  }

  public static <T> Filter<T, Boolean> TRUE() {
    return BOOLEAN(true);
  }

  public static <T> Filter<T, Boolean> FALSE() {
    return BOOLEAN(false);
  }

  public static <T> Filter<T, Number> SUM(Filter<T, Number> left, Filter<T, Number> right) {
    return Filter.infixOperator("+", (a, b) -> a.doubleValue() + b.doubleValue(), left, right);
  }

  public static <T> Filter<T, Number> DIFF(Filter<T, Number> left, Filter<T, Number> right) {
    return infixOperator("-", (a, b) -> a.doubleValue() - b.doubleValue(), left, right);
  }

  public static <T> Filter<T, Number> MUL(Filter<T, Number> left, Filter<T, Number> right) {
    return infixOperator("*", (a, b) -> a.doubleValue() * b.doubleValue(), left, right);
  }

  public static <T> Filter<T, Number> DIV(Filter<T, Number> left, Filter<T, Number> right) {
    return infixOperator("/", (a, b) -> {
      try {
        return a.doubleValue() / b.doubleValue();
      } catch (Exception ignored) {
        return null;
      }
    }, left, right);
  }

  public static <T> Filter<T, Boolean> GREATER(Filter<T, Number> left, Filter<T, Number> right) {
    return infixOperator(">", (a, b) -> a.doubleValue() > b.doubleValue(), left, right);
  }

  public static <T> Filter<T, Boolean> LESS(Filter<T, Number> left, Filter<T, Number> right) {
    return infixOperator("<", (a, b) -> a.doubleValue() < b.doubleValue(), left, right);
  }

  public static <T> Filter<T, Boolean> AND(Filter<T, Boolean> left, Filter<T, Boolean> right) {
    return infixOperator("AND", (a, b) -> a && b, left, right);
  }

  public static <T> Filter<T, Boolean> OR(Filter<T, Boolean> left, Filter<T, Boolean> right) {
    return infixOperator("OR", (a, b) -> a || b, left, right);
  }

  public static <T> Filter<T, Boolean> NOT(Filter<T, Boolean> other){
    return new Filter<T, Boolean>(){

      @Override
      public String stringify() {
        return String.format("NOT( %s )", other.stringify());
      }

      @Override
      public Boolean getValue(T storedData) {
        Boolean otherValue = other.getValue(storedData);
        return null == otherValue ? null : !otherValue;
      }

    };
  }

  public static <T> Filter<T, String> CONCAT(Filter<T, String> left, Filter<T, String> right){
    return biFunction("CONCAT", (a,b)->a+b, left, right);
  }

  public static <T> Filter<T, Boolean> LIKE(Filter<T, String> left, Filter<T, String> right){
    return infixOperator("LIKE", (a,b) -> a.toLowerCase().matches(
          b.toLowerCase()
            .replaceAll(".", "\\.")
            .replaceAll("?", ".")
            .replaceAll("(?<!\\\\)%", ".*")
            .replaceAll("(?<!\\\\)_", ".")
        ),left, right);
  }

}
