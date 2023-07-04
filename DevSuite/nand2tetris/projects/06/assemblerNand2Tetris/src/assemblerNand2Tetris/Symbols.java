package assemblerNand2Tetris;

import java.util.HashMap;
import java.util.Map;

public class Symbols {

	public static Map<String, String> symbolMap = new HashMap<>();
	
	static {
		symbolMap.put("R0",  	"0000000000000000");
		symbolMap.put("R1",  	"0000000000000001");
		symbolMap.put("R2",  	"0000000000000010");
		symbolMap.put("R3",  	"0000000000000011");
		symbolMap.put("R4",  	"0000000000000100");
		symbolMap.put("R5",  	"0000000000000101");
		symbolMap.put("R6",  	"0000000000000110");
		symbolMap.put("R7",  	"0000000000000111");
		symbolMap.put("R8",  	"0000000000001000");
		symbolMap.put("R9",  	"0000000000001001");
		symbolMap.put("R10", 	"0000000000001010");
		symbolMap.put("R11", 	"0000000000001011");
		symbolMap.put("R12", 	"0000000000001100");
		symbolMap.put("R13",	"0000000000001101");
		symbolMap.put("R14", 	"0000000000001110");
		symbolMap.put("R15", 	"0000000000001111");
		symbolMap.put("SCREEN", "0100000000000000");
		symbolMap.put("KBD",    "0110000000000000");
		symbolMap.put("SP",     "0000000000000000");
		symbolMap.put("LCL",    "0000000000000001");
		symbolMap.put("ARG",    "0000000000000010");
		symbolMap.put("THIS",   "0000000000000011");
		symbolMap.put("THAT",   "0000000000000100");
		
		
	}
	
	public static void populateSymbolTable(String[] lines) {
		int startAddress = 16;
		int validLineNumber = 0;
		
		for (String line : lines) {
			line = handleInlineComments(line).stripLeading();
			if (!line.isBlank() && !line.startsWith("//")) {
				if (line.startsWith("(")) {
					String labelName = line.substring(1, line.length() - 1);
					if (!Symbols.symbolMap.containsKey(labelName)) {
						Symbols.symbolMap.put(labelName, getBinaryString("" + (validLineNumber)));
					}
				}
				
				if (!line.startsWith("(")) {
					validLineNumber++;
				}
			}
		}
		for (String line : lines) {
			line = handleInlineComments(line).stripLeading();
			if (!line.isBlank() && !line.startsWith("//")) {
				 if (line.startsWith("@")){
					boolean matchesInteger = line.substring(1).matches("-?\\d+");
					if (!Symbols.symbolMap.containsKey(line.substring(1)) && !matchesInteger) {
						Symbols.symbolMap.put(line.substring(1), "" + getBinaryString("" + startAddress));
						startAddress++;
					}
				}  
				
				
			}
		}
	}
	
	public static String handleInlineComments(String line) {
		if(line.contains("//") && !line.startsWith("//")) {
			line = line.substring(0, line.indexOf("/"));
		}
		return line;
	}
	
	public static String getBinaryString(String decimalString) {
		String binaryString = Integer.toBinaryString(Integer.valueOf(decimalString));
		int length = binaryString.length();
		binaryString = appendZeroes(binaryString, length);
		return binaryString;
	}
	

	public static String appendZeroes(String binaryString, int length) {
		int zeroesToAppend = 16 - length;
		for(int i = 0; i < zeroesToAppend; i++) {
			binaryString = '0' + binaryString;
		}
		return binaryString;
	}
}
