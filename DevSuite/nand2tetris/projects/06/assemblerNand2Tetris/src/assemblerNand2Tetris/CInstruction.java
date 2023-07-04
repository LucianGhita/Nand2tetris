package assemblerNand2Tetris;

import java.util.HashMap;
import java.util.Map;

public class CInstruction {

	public static Map<String, String> dest = new HashMap<>();
	public static Map<String, String> jump = new HashMap<>();
	public static Map<String, String> compATrue = new HashMap<>();
	public static Map<String, String> compAFalse = new HashMap<>();
	
	
	
	static {
		dest.put("NULL", "000");
		dest.put("M",    "001");
		dest.put("D",    "010");
		dest.put("MD",   "011");
		dest.put("A",    "100");
		dest.put("AM",   "101");
		dest.put("AD",   "110");
		dest.put("AMD",  "111");
		
		jump.put("NULL", "000");
		jump.put("JGT",  "001");
		jump.put("JEQ",  "010");
		jump.put("JGE",  "011");
		jump.put("JLT",  "100");
		jump.put("JNE",  "101");
		jump.put("JLE",  "110");
		jump.put("JMP",  "111");
		
		compAFalse.put("0",    "101010");
		compAFalse.put("1",    "111111");
		compAFalse.put("-1",   "111010");
		compAFalse.put("D",    "001100");
		compAFalse.put("A",    "110000");
		compAFalse.put("!D",   "001101");
		compAFalse.put("!A",   "110001");
		compAFalse.put("-D",   "001111");
		compAFalse.put("-A",   "110011");
		compAFalse.put("D+1",  "011111");
		compAFalse.put("A+1",  "110111");
		compAFalse.put("D-1",  "001110");
		compAFalse.put("A-1",  "110010");
		compAFalse.put("D+A",  "000010");
		compAFalse.put("D-A",  "010011");
		compAFalse.put("A-D",  "000111");
		compAFalse.put("D&A",  "000000");
		compAFalse.put("D|A",  "010101");
		 
		compATrue.put("M",   "110000");
		compATrue.put("!M",  "110001");
		compATrue.put("-M",  "110011");
		compATrue.put("M+1", "110111");
		compATrue.put("M-1", "110010");
		compATrue.put("D+M", "000010");
		compATrue.put("D-M", "010011");
		compATrue.put("M-D", "000111");
		compATrue.put("D&M", "000000");
		compATrue.put("D|M", "010101");
	}
	
}
