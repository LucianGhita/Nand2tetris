package assemblerNand2Tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Assembler {
	

	boolean isLastLine = false;
	public static void main(String[] args) {
		String fileContent = null;
		Path filePath = Path.of("../pong/Pong.asm");
		try {
			fileContent = Files.readString(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		generateMachineCode(fileContent);
	}

	private static void generateMachineCode(String fileContent) {
		
		StringBuffer machineCode = new StringBuffer();
		if (fileContent != null) {
			String[] lines = fileContent.split(System.lineSeparator());
			Symbols.populateSymbolTable(lines);
			
			for (String line : lines) {
				line = Symbols.handleInlineComments(line).stripLeading().stripTrailing();
				if (!line.isBlank() && !line.startsWith("//") && !line.contains("(")) {
					if (line.startsWith("@")) {
						appendAInstruction(machineCode, line);
					} else {
						handleCInstruction(line, machineCode);
					}
					machineCode.append(System.lineSeparator());
				}
			}
		}
		try {
			Files.write(Path.of("PongJava.hack"), machineCode.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static void appendAInstruction(StringBuffer machineCode, String line) {
		if (Symbols.symbolMap.get(line.substring(1)) != null) {
			machineCode.append(Symbols.symbolMap.get(line.substring(1)));
		} else {
			handleAInstruction(line, machineCode);
		}
	}

	private static void handleCInstruction(String line, StringBuffer machineCode) {
		String dest = null;
		String comp = null;
		String jump = null; 
		
		String destBinary = "000";
		String compBinary = "";
		String jumpBinary = "000";
		
		String a = "0";
		
		dest = extractDest(line);
		
		comp = extractComp(line, comp);
		
		jump = extractJump(line);
		
		
		if (comp != null && CInstruction.compATrue.get(comp) != null) {
			a = "1";
			compBinary = CInstruction.compATrue.get(comp);
		} else {
			compBinary = CInstruction.compAFalse.get(comp);
		}
		
		if (dest != null && CInstruction.dest.get(dest) != null) {
			destBinary = CInstruction.dest.get(dest);
		}
		
		if (jump != null && CInstruction.jump.get(jump) != null) {
			jumpBinary = CInstruction.jump.get(jump);
		}
		
		String cInstruction = "111" + a + compBinary + destBinary + jumpBinary;  
//		System.out.println("CInstruction: " + cInstruction); 
		machineCode.append(cInstruction);
	}

	private static String extractJump(String line) {
		if (line.contains(";")) {
			return line.substring(line.indexOf(";") + 1).trim();
		}
		return null;
	}

	private static String extractDest(String line) {
		if (line.contains("=")) {
			return line.substring(0, line.indexOf("=")).trim();
		}
		return null;
	}

	private static String extractComp(String line, String comp) {
		if (line.contains(";") && line.contains("=")) {
			comp = line.substring(line.indexOf("=") + 1, line.indexOf(";")).trim();
		} else if (!line.contains(";") && line.contains("=")) {
			comp = line.substring(line.indexOf("=") + 1).trim();
		} else if (line.contains(";")) {
			comp = line.substring(0, line.indexOf(";"));
		}
		return comp;
	}

	private static void handleAInstruction(String line, StringBuffer machineCode) {
		String decimalString = line.substring(1, line.length());
		String binaryString = Symbols.getBinaryString(decimalString);
		machineCode.append(binaryString);
	}
}
