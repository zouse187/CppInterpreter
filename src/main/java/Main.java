import expr.Expr;
import interpreter.InterpreterCexpr;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import lexer.CexprLexer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.CexprParser;
import token.Token;
import values.Value;

public class Main {
  static void main(String... args) throws IOException, URISyntaxException {
    String source = "4 / 2 + 5 * 5";

    List<Token> tokens = new CexprLexer().scan(source);
    Expr ast = new CexprParser().parse(tokens);

    Value result = new InterpreterCexpr().eval(ast);
    IO.println(result);



    String antlrSource = "1 + 5";

    var input = CharStreams.fromString(antlrSource);
    var lexer = new CppLexer(input);
    var antlrTokens = new CommonTokenStream(lexer);

    var parser = new CppParser(antlrTokens);
    var tree = parser.cexpr();
  }
}
