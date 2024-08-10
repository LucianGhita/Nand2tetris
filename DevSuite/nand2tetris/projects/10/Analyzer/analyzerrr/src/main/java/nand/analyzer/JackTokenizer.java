package nand.analyzer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JackTokenizer {

    private List<TokenizedFile> tokenizedFiles = new ArrayList<TokenizedFile>();
    

	public JackTokenizer(String path) {
        Path inputPath = Path.of(path);
        if (inputPath.toFile().isFile()) {
                        tokenizeFile(getFileLines(path), inputPath);
            } else if (inputPath.toFile().isDirectory()) {
            // handle directories
        }
        
        System.out.println("Finish");
    }

	private void tokenizeFile(String[] fileLines, Path inputPath) {
		TokenizedFile fileTokens = new TokenizedFile(inputPath);
		for (var line : fileLines) {

			final String regexNormalComment = "\\/\\/(?!.*\\\".*\\/\\/).*$";

			final String subst = "";
			final Pattern pattern = Pattern.compile(regexNormalComment, Pattern.MULTILINE);
			final Matcher matcher = pattern.matcher(line);
			line = matcher.replaceAll(subst);

			if (!line.startsWith("//") && !line.startsWith("/**") && !line.startsWith("/*") && (!line.endsWith("*/"))) {
				if (!line.isEmpty() && !line.isBlank()) {
					StringBuffer l = new StringBuffer();
					boolean isString = false;
					for (var c : line.toCharArray()) {
						
						// handle string constants
						if (isString && c == '"') {
							isString = false;
							l.append(c);
							createToken(fileTokens, l);
							continue;
						} else if (c == '"') {
							isString = true;
							l.append(c);
							continue;
						} else if (isString) {
							l.append(c);
							continue;
						}						
						
						// handle everything else
						if (!Character.isWhitespace(c)) {
							l.append(c);
	 					} else {
							createToken(fileTokens, l);
	 					}
						
					}
					System.out.println(line.stripLeading().stripTrailing());
				}
			}

		}
		tokenizedFiles.add(fileTokens);
	}

	private void createToken(TokenizedFile fileTokens, StringBuffer l) {
		if (!l.isEmpty()) {
			Token token = new Token(l.toString());
			if (token.getType().equals(TokenType.COMPOUND)) {
				token.getCompoundTokenList().stream().forEach(x -> fileTokens.addToken(x));
				token.getCompoundTokenList()
						.forEach(x -> System.out.println("\t" + x.getContent() + " " + x.getType()));
			} else {
				fileTokens.addToken(token);
				System.out.println("\t" + token.getContent() + " " + token.getType());
				
			}
			l.setLength(0);
		}
	}
    
    public void writeXml() {
    	for (var tokenFile : tokenizedFiles) {
    		List<Token> tokens2 = tokenFile.getTokens();
    		try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.xml", false))) {
				writer.append("<tokens>");
				writer.append(System.lineSeparator());
				tokens2.forEach(x -> {
						try {
							writer.append("<"  + x.getType().toString()+ ">");
							writer.append(" " + x.getContent() +" ");
							writer.append("</" +  x.getType().toString()+ ">" + System.lineSeparator());
						} catch (IOException e) {
							e.printStackTrace();
						}
				});
				writer.append("</tokens>");
				writer.append(System.lineSeparator());
				writer.close();
    		} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    private static String[] getFileLines(String filePath) {

        String [] fileLines = null;
        File file = new File(filePath);
        try {
            String fileContents = Files.readString(file.toPath());
            fileLines = fileContents.split(System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileLines;
    }
    
    public List<TokenizedFile> getTokenizedFiles() {
		return tokenizedFiles;
	}
}
