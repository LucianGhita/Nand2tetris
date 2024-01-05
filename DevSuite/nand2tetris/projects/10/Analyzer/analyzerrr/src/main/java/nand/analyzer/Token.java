package nand.analyzer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {

    
    private String content;
    private TokenType type;
    
    private List<Token> compoundTokenList = new ArrayList<>();
    
    

	public Token(){}

    public Token(String input) {
        parseToken(input.stripLeading().stripTrailing());
    }
    private void parseToken(String input) {
        if (isKeyword(input)) {
            type = TokenType.KEYWORD;
            content = input;
        } else if (isSymbol(input)) {
            type = TokenType.SYMBOL;
            if (input.equals(">")) {
            	content = "&gt;";
            } else if (input.equals("<")) {
            	content = "&lt;";
            } else if (input.equals("\"")) {
            	content = "&quot;";
            } else if (input.equals("&")) {
            	content = "&amp;";
            } else {
            	content = input;
            }
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
//        	System.out.println("compound: " + input);
        	content = input;
        	String tokenString = "";
        	for (var c : content.toCharArray()) {
        		if (isSymbol("" + c)) {
        			if (!tokenString.strip().isEmpty()) {
        				Token prevToken = new Token(tokenString);
        				compoundTokenList.add(prevToken);
        				tokenString = "";
        			}
        			Token token = new Token("" + c);
        			compoundTokenList.add(token);
        		} else {
        			tokenString += c;
        		}
        	}
        	
        	
            
//            compoundTokenList.stream().forEach(x -> System.out.println("\t" + x.getContent() + " of type " + x.getType()));
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
    
    public boolean isInteger() {
    	return type.equals(TokenType.INT_CONST);
    }

    
    private boolean isIdentifier(String input) {
        final String regex = "[a-zA-Z_][a-zA-Z0-9_]*";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }
    
    
    public boolean isIdentifier() {
    	return type.equals(TokenType.IDENTIFIER);
    }
    
    private boolean isSymbol(String input) {
        return TokenUtils.SYMBOLS.contains(input);
    }

    public boolean isSymbol() {
    	return type.equals(TokenType.SYMBOL);
    }
    
    private boolean isKeyword(String input) {
        return TokenUtils.KEYWORDS.contains(input);
    }

    public boolean isKeyword() {
    	return type.equals(TokenType.KEYWORD);
    }
    
    private boolean isStringConstant (String input) {
        return (input.startsWith("\"") && input.endsWith("\""));
    }
    
    public boolean isStringConstant() {
    	return type.equals(TokenType.STRING_CONST);
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
