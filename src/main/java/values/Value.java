package values;

public interface Value {

  static int asInt(Value v) {
    if (v instanceof IntVal(int val)) return val;
    throw new RuntimeException("number expected");
  }
}
