package lexer;

import static token.TokenKind.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import token.Token;

public class CexprLexer {

  protected String source;
  protected int current;

  public List<Token> scan(String input) {
    source = input;
    current = 0;
    List<Token> tokens = new ArrayList<>();

    do tokens.add(nextToken());
    while (tokens.getLast().kind() != EOF);

    return tokens;
  }

  protected Token nextToken() {
    skipIrrelevant();

    if (!isAtEnd()) {
      char c = advance();

      if (Character.isDigit(c)) {
        String lexeme = c + readWhile(Character::isDigit);
        return new Token(INT, lexeme);
      }
      return switch (c) {
        case '(' -> new Token(LPAREN, "(");
        case ')' -> new Token(RPAREN, ")");
        case '*' -> new Token(STAR, "*");
        case '/' -> new Token(SLASH, "/");
        case '+' -> new Token(PLUS, "+");
        case '-' -> new Token(MINUS, "-");
        default ->
            throw new IllegalStateException(
                "Lexer Error in nextToken(): Unexpected character: " + c);
      };
    }
    return new Token(EOF, "");
  }

  protected boolean isAtEnd() {
    return current >= source.length();
  }

  protected char peek() {
    return peek(0);
  }

  protected char peek(int la) {
    if (la < 0 || current + la >= source.length()) return '\0';
    return source.charAt(current + la);
  }

  protected char advance() {
    return isAtEnd() ? '\0' : source.charAt(current++);
  }

  protected void skipIrrelevant() {
    if (!isAtEnd()) {
      if (peek() == ' ') { // Ohne dieses if -> IndexOutOfBoundsException
        while (peek() == ' ') advance();
      }

      if (peek() == '/' && peek(1) == '/') {
        // Wenn ein kommentar kommt, wird der ganze Rest der Zeile geskippt.
        while (!isAtEnd()) advance();
      }
    }
  }

  protected String readWhile(Predicate<Character> pred) {
    StringBuilder sb = new StringBuilder();
    while (!isAtEnd() && pred.test(peek())) sb.append(advance());
    return sb.toString();
  }
}
