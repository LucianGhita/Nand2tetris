package nand.analyzer;
import java.util.ArrayList;
import java.util.List;

public class FileTokens {
    private List<Token> tokens;


    public void addToken(Token token) {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }
        tokens.add(token);
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
