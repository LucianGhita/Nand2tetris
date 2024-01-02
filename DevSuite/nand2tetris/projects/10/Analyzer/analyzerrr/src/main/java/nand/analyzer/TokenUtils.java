package nand.analyzer;

import java.util.List;

public class TokenUtils {
	public static final List<String> KEYWORDS  = List.of("class", "constructor", "function", "method", "field",
            "static", "var", "int", "char", "boolean", "void", "true", "false",
            "null", "this", "let", "do", "if", "else", "while", "return");
	public static final List<String> SYMBOLS = List.of("{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~");


}
