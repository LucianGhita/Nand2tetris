package nand.analyzer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JackTokenizer {

    private List<FileTokens> tokens = new ArrayList<FileTokens>();
    public JackTokenizer(String path) {
        Path inputPath = Path.of(path);
        if (inputPath.toFile().isFile()) {
                        tokenizeFile(getFileLines(path));
            } else if (inputPath.toFile().isDirectory()) {
            // handle directories
        }
        
        System.out.println("Finish");;
    }

    private void tokenizeFile(String[] fileLines) {
        FileTokens fileTokens = new FileTokens();
        for (var line : fileLines) {
            if (!line.startsWith("//") && !line.startsWith("/**") && !line.startsWith("/*") && (!line.endsWith("*/"))) {
                	if (!line.isEmpty() && !line.isBlank()) {
                		String[] lines = line.split("\\s+");
                		System.out.println(line.stripLeading().stripTrailing());
                		for (var l : lines) {
                			if (!l.isEmpty()) {
                				Token token = new Token(l.stripLeading().stripTrailing());
                				if (token.getType().equals(TokenType.COMPOUND)) {
                					token.getCompoundTokenList().stream().forEach(x -> fileTokens.addToken(x));
                					token.getCompoundTokenList().forEach(x-> System.out.println("\t" + x.getContent() + " " + x.getType()));
                				} else {
                					fileTokens.addToken(token);
                					System.out.println("\t" + token.getContent() + " "  + token.getType());
                					
                				}
                			}
                		}
                }
            }

        }
        tokens.add(fileTokens);
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
}
