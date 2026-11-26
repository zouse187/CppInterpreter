import expr.*;

import java.util.List;

public class CppAstBuilderCexpr extends CppBaseVisitor<Object> {
  @Override
  public Expr visitCexpr(CppParser.CexprContext ctx) {
    return asExpr(visit(ctx.eform()));
  }

  @Override
  public Expr visitIntLit(CppParser.IntLitContext ctx) {
    int value = Integer.parseInt(ctx.INT().getText());
    return new IntLiteral(value);
  }

  @Override
  public Expr visitAddExpr(CppParser.AddExprContext ctx) {
    List<Expr> ops = ctx.eform().stream().map(this::visit).map(this::asExpr).toList();
    return ops.stream().reduce(AddExpr::new).orElseThrow();
  }

  @Override
  public Expr visitSubExpr(CppParser.SubExprContext ctx) {
    List<Expr> ops = ctx.eform().stream().map(this::visit).map(this::asExpr).toList();
    return ops.stream().reduce(SubExpr::new).orElseThrow();
  }

  @Override
  public Expr visitMulExpr(CppParser.MulExprContext ctx) {
    List<Expr> ops = ctx.eform().stream().map(this::visit).map(this::asExpr).toList();
    return ops.stream().reduce(MulExpr::new).orElseThrow();
  }

  @Override
  public Expr visitDivExpr(CppParser.DivExprContext ctx) {
    List<Expr> ops = ctx.eform().stream().map(this::visit).map(this::asExpr).toList();
    return ops.stream().reduce(DivExpr::new).orElseThrow();
  }

  @Override
  public Expr visitParenExpr(CppParser.ParenExprContext ctx) {
    return asExpr(visit(ctx.eform()));
  }

  @Override
  public Expr visitUnaryMinusExpr(CppParser.UnaryMinusExprContext ctx) {
    return new SubExpr(new IntLiteral(0), asExpr(visit(ctx.atom())));
  }

  public Expr asExpr(Object o) {
    return (Expr) o;
  }
}

