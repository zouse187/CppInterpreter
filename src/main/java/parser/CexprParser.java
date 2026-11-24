package parser;

import static token.TokenKind.*;

import expr.*;
import java.util.Arrays;
import java.util.List;
import token.Token;
import token.TokenKind;

public class CexprParser {
  protected List<Token> tokens;
  protected int current;

  // Cint ::= expr EOF
  public Expr parse(List<Token> tokenList) {
    tokens = tokenList;
    current = 0;

    Expr expr = parseEform();
    if (!isAtEnd())
      throw new RuntimeException("Parse Error in parse(): unexpected token: " + peek());

    return expr;
  }

  // eform ::= term (('+' | '-')) term)*
  protected Expr parseEform() {
    Expr expr = parseTerm();

    while (check(PLUS, MINUS)) {
      Token op = match(PLUS, MINUS);
      Expr right = parseTerm();
      switch (op.kind()) {
        case PLUS -> expr = new AddExpr(expr, right);
        case MINUS -> expr = new SubExpr(expr, right);
        default ->
            throw new RuntimeException("Parse Error in parseExpr(): unexpected token: " + peek());
      }
    }

    return expr;
  }

  // term ::= primary (('*' | '/') primary)*
  protected Expr parseTerm() {
    Expr expr = parsePrimary();

    while (check(STAR, SLASH)) {
      Token op = match(STAR, SLASH);
      Expr right = parsePrimary();
      switch (op.kind()) {
        case STAR -> expr = new MulExpr(expr, right);
        case SLASH -> expr = new DivExpr(expr, right);
        default ->
            throw new RuntimeException("Parse Error in parseTerm(): unexpected token: " + peek());
      }
    }
    return expr;
  }

  // primary ::= '-' INT | '-' '(' eform ')' | INT | '(' eform ')'
  protected Expr parsePrimary() {
    // '-' INT | '-' '(' expr ')'
    if (check(MINUS)) {
      match(MINUS);
      if (check(LPAREN)) {
        match(LPAREN);
        Expr expr = parseEform();
        match(RPAREN);
        return new SubExpr(new IntLiteral(0), expr);
      }
      Token t = match(INT);
      return new IntLiteral(Integer.parseInt("-" + t.lexeme()));
    }
    /*if (check(MINUS)) {
      match(MINUS);
      return parsePrimary();
    }*/

    // '(' eform ')'
    if (check(LPAREN)) {
      match(LPAREN);
      Expr expr = parseEform();
      match(RPAREN);
      return expr;
    }

    // INT
    if (check(INT)) {
      Token t = match(INT);
      return new IntLiteral(Integer.parseInt(t.lexeme()));
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
