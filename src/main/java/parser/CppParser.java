package parser;

import static token.TokenKind.*;

import expr.AddExpr;
import expr.Expr;
import expr.IntLiteral;
import expr.SubExpr;
import java.util.Arrays;
import java.util.List;
import token.Token;
import token.TokenKind;

public class CppParser {
  protected List<Token> tokens;
  protected int current;

  // Cint ::= expr EOF
  public Expr parse(List<Token> tokenList) {
    tokens = tokenList;
    current = 0;

    Expr expr = parseExpr();
    if (!isAtEnd())
      throw new RuntimeException("Parse Error in parse(): unexpected token: " + peek());

    return expr;
  }

  // expr ::= primary ((op) primary)*
  protected Expr parseExpr() {
    // expr ::= INT | expr op expr | '(' expr ')'
    // op   ::= '+' | '-'
    // INT  ::= [0-9]+
    // Cint ::= expr EOF

    // expr    ::= primary ((op) primary)*
    // primary ::= INT | '(' expr ')'
    // op      ::= '+' | '-'
    // INT     ::= [0-9]+
    // Cint    ::= expr EOF

    Expr expr = parsePrimary();

    while (check(PLUS, MINUS)) {
      Token op = match(PLUS, MINUS);
      Expr right = parsePrimary();
      switch (op.kind()) {
        case PLUS -> expr = new AddExpr(expr, right);
        case MINUS -> expr = new SubExpr(expr, right);
        default ->
            throw new RuntimeException("Parse Error in parseExpr(): unexpected token: " + peek());
      }
    }

    return expr;
  }

  // primary ::= INT | '(' expr ')'
  protected Expr parsePrimary() {
    if (check(INT)) {
      Token t = match(INT);
      return new IntLiteral(Integer.parseInt(t.lexeme()));
    }

    if (check(LPAREN)) {
      match(LPAREN);
      Expr expr = parseExpr();
      match(RPAREN);
      return expr;
    }

    throw new RuntimeException("Parse Error in parsePrimary(): unexpected token: " + peek());
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
