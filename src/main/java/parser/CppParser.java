package parser;

import static token.TokenKind.*;

import expr.AddExpr;
import expr.Expr;
import expr.IntLiteral;
import expr.SubExpr;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import token.Token;
import token.TokenKind;

public class CppParser {
  protected List<Token> tokens;
  protected int current;

  public Expr parse(List<Token> tokenList) {
    tokens = tokenList;
    current = 0;

    Expr expr = parseExpr();
    if (!isAtEnd())
      throw new RuntimeException("Parse Error in parse(): unexpected token: " + peek());

    return expr;
  }

  protected Expr parseExpr() {
    // expr ::= INT | expr op expr | '(' expr ')'
    // op   ::= '+' | '-'
    // INT  ::= [0-9]+
    // Cint ::= expr EOF
    if (check(LPAREN)) {
      return parseExprParen();
    }

    if (peek(1).kind() == PLUS || peek(1).kind() == MINUS) {
      return switch (peek(1).kind()) {
        case PLUS -> parseAddExpr();
        case MINUS -> parseSubExpr();
        default ->
            throw new RuntimeException("Parse Error in parseExpr(): unexpected token: " + peek());
      };
    }

    return parseInt();
  }

  protected Expr parseExprParen() {
    match(LPAREN);
    List<Expr> ops = new ArrayList<>();
    ops.add(parseExpr());
    while (check(PLUS) || check(MINUS)) {
      if (check(PLUS)) match(PLUS);
      if (check(MINUS)) match(MINUS);
      ops.add(parseExpr());
    }
    match(RPAREN);

    //            hier muss Add oder Sub rein! wie? Vllt anderer Stream
    //                           |
    return ops.stream().reduce(AddExpr::new).orElseThrow();
  }

  protected Expr parseInt() {
    Token t = match(INT);
    return new IntLiteral(Integer.parseInt(t.lexeme()));
  }

  protected Expr parseAddExpr() {
    List<Expr> ops = new ArrayList<>();
    ops.add(parseInt());

    while (check(PLUS)) {
      match(PLUS);
      ops.add(parseExpr());
    }

    return ops.stream().reduce(AddExpr::new).orElseThrow();
  }

  protected Expr parseSubExpr() {
    List<Expr> ops = new ArrayList<>();
    ops.add(parseInt());

    while (check(MINUS)) {
      match(MINUS);
      ops.add(parseExpr());
    }

    return ops.stream().reduce(SubExpr::new).orElseThrow();
  }

  protected boolean isAtEnd() {
    return peek().kind() == EOF;
  }

  protected Token peek() {
    return peek(0);
  }

  protected Token peek(int la) {
    if (la < 0 || current + la >= tokens.size()) return tokens.getLast(); // EOF
    return tokens.get(current + la);
  }

  protected Token advance() {
    return isAtEnd() ? tokens.getLast() : tokens.get(current++);
  }

  protected boolean check(TokenKind... expected) {
    return Arrays.stream(expected).anyMatch(e -> e == peek().kind());
  }

  protected Token match(TokenKind... expected) {
    if (check(expected)) return advance();
    throw new RuntimeException("Parse Error in match(): unexpected token: " + peek());
  }
}
