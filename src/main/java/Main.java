import expr.Expr;
import interpreter.CppInterpreter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import lexer.CppLexer;
import parser.CppParser;
import token.Token;
import values.Value;

public class Main {
  static void main(String... args) throws IOException, URISyntaxException {

    // expr ::= INT | expr op expr | '(' expr ')'
    // op   ::= '+' | '-'
    // INT  ::= [0-9]+
    // Cint ::= expr EOF

    // Expr = IntLiteral(Int) | AddExpr(Expr, Expr) | SubExpr(Expr, Expr)
    // Lint = Expr

    // 10 - 5 + 2 = 7: Interpreter sagt 3
    // ((1 + 2) - (3 + 4)) = -4: Interpreter sagt 4
    // Bei einem ' ' am Ende kommt ein "index out of bounds" fehler

    String source3 = "((1 + 2) - (3 + 4))";

    List<Token> tokens = new CppLexer().scan(source3);
    Expr ast = new CppParser().parse(tokens);

    Value result = new CppInterpreter().eval(ast);
    IO.println(result);
  }
}
