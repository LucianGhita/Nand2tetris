package nand.analyzer;

public class JackAnalyzer {
    public static void main(String[] args) {
        JackTokenizer tokenizer = new JackTokenizer("/home/lgh/Programming//Nand2tetris/Nand2tetris/DevSuite/nand2tetris/projects/10/Square/");
        tokenizer.writeXml();
        CompilationEngine engine = new CompilationEngine(tokenizer);
        engine.compile();
    }


}