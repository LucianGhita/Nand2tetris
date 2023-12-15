import java.util.List;

public class Token {

    private List<String> keywords  = List.of("class", "constructor", "function", "method", "field",
                                                       "static", "var", "int", "char", "boolean", "void", "true", "false",
                                                       "null", "this", "let", "do", "if", "else", "while", "return");
    private List<String> symbols = List.of("{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~");
    private String content;
    private TokenType type;

    public Token(){}

    public Token(String input) {
        parseToken(input);
    }

    private void parseToken(String input) {
        if (isKeyword(input)) {
            type = TokenType.KEYWORD;
            content = input;
        } else if (isSymbol(input)) {
            type = TokenType.SYMBOL;
            content = input;
        } else if (isIntVal(input)) {
            type = TokenType.INT_CONST;
            content = input;
        } else if (isIdentifier(input)) {
            type = TokenType.IDENTIFIER;
        }  else if (isStringVal(input)) {
            type = TokenType.STRING_CONST;
        } else {
            System.out.println("Could not parse the token: " + input);
        }
    }

    private boolean isStringVal(String input) {
        return false;
    }

    private boolean isIntVal(String input) {
        try  {
            int integerValue = Integer.parseInt(input);
            if (integerValue >= 0 && integerValue <= 32767) {
                return true;
            }
        } catch(NumberFormatException e) {
            return false;
        }
        return false;
    }

    private boolean isIdentifier(String input) {
        char c = input.stripTrailing().charAt(0);
        try {
            int charIntegerVal = Integer.parseInt("" + c);
            return false;
       } catch(NumberFormatException e) {
            // do nothing
        }
        // do some regex here for a sequence of letters digits and underscore
        return false;
    }

    private boolean isSymbol(String input) {
        return symbols.contains(input);
    }

    private boolean isKeyword(String input) {
        return keywords.contains(input);
    }

    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }


}
