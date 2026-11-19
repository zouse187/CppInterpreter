package interpreter;

import expr.AddExpr;
import expr.Expr;
import expr.IntLiteral;
import expr.SubExpr;
import values.IntVal;
import values.Value;

public class CppInterpreter {

  public Value eval(Expr expr) {
    return switch (expr) {
      case IntLiteral(int v) -> new IntVal(v);

      case AddExpr(Expr l, Expr r) -> new IntVal(Value.asInt(eval(l)) + Value.asInt(eval(r)));

      case SubExpr(Expr l, Expr r) -> new IntVal(Value.asInt(eval(l)) - Value.asInt(eval(r)));

      default ->
          throw new IllegalStateException(
              "Interpreter Error in eval(): Unexpected expression: " + expr);
    };
  }
}
