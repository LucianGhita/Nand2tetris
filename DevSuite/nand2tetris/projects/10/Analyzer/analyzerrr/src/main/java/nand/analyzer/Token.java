package nand.analyzer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {

    
    private String content;
    private TokenType type;
    
    private List<Token> compoundTokenList;
    
    

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
            content = input;
        }  else if (isStringConstant(input)) {
            type = TokenType.STRING_CONST;
            content = input;
        } else {
        	type = TokenType.COMPOUND;
            System.out.println("compount token: " + input);
            String tokenString = "";
            compoundTokenList = new ArrayList<>();;
            for (char c : input.toCharArray()) {
            	if (!isSymbol("" + c)) {
            		tokenString += c;
            	} else {
            		if (!tokenString.isEmpty()) {
            			Token token = new Token(tokenString);
            			compoundTokenList.add(token);
            		}
            		Token nextToken = new Token("" + c);
            		compoundTokenList.add(nextToken);
            		tokenString = "";
            	}
            }
            
            System.out.println("Extracted in: ");
            compoundTokenList.stream().forEach(x -> System.out.println("\t" + x.getContent() + " of type " + x.getType()));
        }
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
        final String regex = "[a-zA-Z_][a-zA-Z0-9_]*";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    private boolean isSymbol(String input) {
        return TokenUtils.SYMBOLS.contains(input);
    }

    private boolean isKeyword(String input) {
        return TokenUtils.KEYWORDS.contains(input);
    }

    private boolean isStringConstant (String input) {
        return (input.startsWith("\"") && input.endsWith("\""));
    }
    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
    
    public List<Token> getCompoundTokenList() {
		return compoundTokenList;
	}

}
