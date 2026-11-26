import expr.*;
import token.Token;
import token.TokenKind;

import static token.TokenKind.MINUS;
import static token.TokenKind.PLUS;

public class CppAstBuilderCexpr extends CppBaseVisitor<Object> {
  @Override
  public Expr visitCexpr(CppParser.CexprContext ctx) {
    return asExpr(visit(ctx.eform()));
  }

  @Override
  public Expr visitEform(CppParser.EformContext ctx) {
    Expr expr = asExpr(visit(ctx.term(0)));

    for (int i = 1; i < ctx.term().size(); i++) {
      // 2*i-1 -> 0,3,5,7...
      String op = ctx.getChild(2*i-1).getText();
      Expr right = asExpr(visit(ctx.term(i)));
      switch (op) {
        case "+" -> expr = new AddExpr(expr, right);
        case "-" -> expr = new SubExpr(expr, right);
        default -> throw new RuntimeException("Expected ['+', '-'] actual [" + op + "]");
      }
    }

    return expr;
  }

  public Expr visitTerm(CppParser.TermContext ctx) {
    Expr expr = asExpr(visit(ctx.primary(0)));

    for (int i = 1; i < ctx.primary().size(); i++) {
      // 2*i-1 -> 0,3,5,7...
      String op = ctx.getChild(2*i-1).getText();
      Expr right = asExpr(visit(ctx.primary(i)));
      switch (op) {
        case "*" -> expr = new MulExpr(expr, right);
        case "/" -> expr = new DivExpr(expr, right);
        default -> throw new RuntimeException("Expected ['*', '/'] actual [" + op + "]");
      }
    }

    return expr;
  }

  @Override
  public Expr visitIntLit(CppParser.IntLitContext ctx) {
    int value = Integer.parseInt(ctx.INT().getText());
    return new IntLiteral(value);
  }

  @Override
  public Expr visitParen(CppParser.ParenContext ctx) {
    return asExpr(visit(ctx.eform()));
  }

  @Override
  public Expr visitUnaryMinus(CppParser.UnaryMinusContext ctx) {
    return new SubExpr(new IntLiteral(0), asExpr(visit(ctx.primary())));
  }

  public Expr asExpr(Object o) {
    return (Expr) o;
  }
}
