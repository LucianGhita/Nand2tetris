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
    }

    private void tokenizeFile(String[] fileLines) {

    }


    private static String[] getFileLines(String filePath) {

        String [] fileLines = null;
        File file = new File(filePath);
        try {
            String fileContents = Files.readString(file.toPath());
            fileLines = fileContents.split(System.lineSeparator());
            for (var line : fileLines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileLines;
    }
}
