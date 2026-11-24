package interpreter;

import expr.DivExpr;
import expr.Expr;
import expr.MulExpr;
import values.IntVal;
import values.Value;

public class InterpreterCexpr extends InterpreterCint {
  public Value eval(Expr expr) {
    return switch (expr) {
      case MulExpr(Expr l, Expr r) -> new IntVal(Value.asInt(eval(l)) * Value.asInt(eval(r)));
      case DivExpr(Expr l, Expr r) -> new IntVal(Value.asInt(eval(l)) / Value.asInt(eval(r)));
      default -> super.eval(expr);
    };
  }
}
