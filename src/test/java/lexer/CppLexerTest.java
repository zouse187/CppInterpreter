package lexer;

import static org.junit.jupiter.api.Assertions.*;
import static token.TokenKind.EOF;
import static token.TokenKind.INT;

import java.util.List;
import org.junit.jupiter.api.Test;
import token.Token;

class CppLexerTest {

  @Test
  void testScanWithoutWhitespace() {
    CexprLexer cppL = new CexprLexer();
    List<Token> tokens = cppL.scan("123");

    assertEquals(
        2, tokens.size(), "Es sollten genau zwei Tokens (INT und EOF) zurückgegeben werden.");
    assertEquals(INT, tokens.get(0).kind());
    assertEquals("123", tokens.get(0).lexeme());
    assertEquals(EOF, tokens.get(1).kind());
  }

  @Test
  void testScanWithWhitespaceBeforeToken() {
    CexprLexer cppL = new CexprLexer();
    List<Token> tokens = cppL.scan("   456");

    // Erwartet: Whitespace wird übersprungen, Token(INT, "456") gefolgt von EOF
    assertEquals(2, tokens.size(), "Whitespace vor dem Token sollte ignoriert werden.");
    assertEquals(INT, tokens.get(0).kind());
    assertEquals("456", tokens.get(0).lexeme());
    assertEquals(EOF, tokens.get(1).kind());
  }

  @Test
  void testScanWhitespaceFollowedByEof() {
    CexprLexer cppL = new CexprLexer();
    List<Token> tokens = cppL.scan("   ");

    // Erwartet: Nur Whitespace bis zum Dateiende führt direkt zu EOF
    assertEquals(1, tokens.size(), "Nur Whitespace sollte direkt ein EOF-Token erzeugen.");
    assertEquals(EOF, tokens.get(0).kind());
  }
}
