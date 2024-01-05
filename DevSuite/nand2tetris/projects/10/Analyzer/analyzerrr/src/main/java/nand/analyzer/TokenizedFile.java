package nand.analyzer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TokenizedFile {
	
	private Path filePath;
	
	private List<Token> tokens;
    
    private Token currentToken;

    public TokenizedFile(Path filePath) {
    	this.filePath = filePath;
    }

	public void addToken(Token token) {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }
        tokens.add(token);
    }
    
    public boolean hasMoreTokens() {
    	return !tokens.isEmpty();
    }
    
    public Token advance() throws Exception {
    	if (hasMoreTokens()) {
    		currentToken = tokens.get(0);
    		tokens.remove(0);
    		return currentToken;
    	}
    	throw new Exception ("No more tokens in the list");
    }
    
    public TokenType tokenType() {
    	return currentToken.getType();
    }
    
    public String keyword () throws Exception {
    	if (TokenUtils.KEYWORDS.contains(currentToken.getContent())) {
    		return currentToken.getContent();
    	}
    	throw new Exception ("Current token is not a keyword!");
    }
    
    public String identifier () {
    	return currentToken.getContent();
    }
    
    public String symbol() throws Exception {
    	if (TokenUtils.SYMBOLS.contains(currentToken.getContent())) {
    		return currentToken.getContent();
    	}
    	throw new Exception ("Current token is not a symbol!");
    }
    
    public int intVal () throws Exception {
    	int val;
    	try {
    		val = Integer.parseInt(currentToken.getContent());
    	} catch (NumberFormatException e) {
    		throw new Exception ("Current token is not an integer!");
    	}
    	return val;
    }
    
    public String stringVal () {
    	return currentToken.getContent();
    }
    
    public List<Token> getTokens() {
        return tokens;
    }
    
    public Path getFilePath() {
		return filePath;
	}
}
