package expr;

public record SubExpr(Expr left, Expr right) implements Expr {}
