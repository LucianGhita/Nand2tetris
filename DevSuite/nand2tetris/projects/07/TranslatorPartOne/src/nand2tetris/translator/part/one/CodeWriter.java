package nand2tetris.translator.part.one;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeWriter {
	private List<Command> commands;
	private Path filePath;
	private StringBuilder builder = new StringBuilder();
	private final static String SPACE = " ";
	
	private final static Map<String, Integer> hackRam = new HashMap<>();
	
	private final static String SP   = "SP";
	private final static String LCL  = "LCL";
	private final static String ARG  = "ARG";
	private final static String THIS = "THIS";
	private final static String THAT = "THAT";
	
	static {
		hackRam.put(SP,   0);
		hackRam.put(LCL,  1);
		hackRam.put(ARG,  2);
		hackRam.put(THIS, 3);
		hackRam.put(THAT, 4);
	}
	
	public CodeWriter(List<Command> commands, Path filePath) {
		this.commands = commands;
		this.filePath = filePath;
	}
	
	
	public void generate() {
		for (var command : commands) {
			String commandType = command.getType();
			if (commandType.equals(Parser.C_ARITHMETIC)) {
				generateArithmeticCommand(command);
			} else if (commandType.equals(Parser.C_PUSH) || commandType.equals(Parser.C_POP)) {
				generatePushPopCommand(command);
			}
		}
		
		try {
			Files.write(filePath, builder.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void generatePushPopCommand(Command command) {
		if (command.getType().equals(Parser.C_PUSH)) {
			generatePushCommand(command);
		} else if (command.getType().equals(Parser.C_POP)) {
			generatePopCommand(command);
		}
	}
	

	private void generatePopCommand(Command command) {
		generateCommentPushPop(command, true);
		Integer memorySegmentAddress = getMemorySegment(command);
		
		
		// pop local 2
		
		// @2 
		// D=A put 2 in the D register
		// @LCL
		// D=D+A put 2 + 1015 in D => 1017
		// @addr // general purpose register
		// M=D   // put 1017 at its address
		// @SP  // decrease sp
		// M=M-1
		// A=M  // select 257
		// D=M  // get the value at 257
		// @addr // 
		// A=M  // select the register at 1017
		// M=D  // put at its value the value at 257
		
		
	}


	private void generateCommentPushPop(Command command, boolean isPop) {
		String commandName = isPop?Parser.POP:Parser.PUSH;
		builder
		.append("//")
		.append(commandName)
		.append(SPACE)
		.append(command.getArg1())
		.append(SPACE)
		.append(command.getArg2())
		.append(System.lineSeparator());
	}


	private void generatePushCommand(Command command) {
		generateCommentPushPop(command, false);
		Integer memorySegmentAddress = getMemorySegment(command); 
		generatePushCommand(memorySegmentAddress);
	
	}


	private void generatePushCommand(Integer memorySegmentAddress) {
		builder
		.append("@").append(String.valueOf(memorySegmentAddress)).append(System.lineSeparator()) // @ constant no
		.append("D=A").append(System.lineSeparator()) 						 					 // D=A
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())  					 // @SP
		.append("A=M").append(System.lineSeparator()) 					  	 					 // A=M
		.append("M=D").append(System.lineSeparator())						  					 // M=D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())	  					 // @SP
		.append("M=M+1").append(System.lineSeparator());					  					 // M=M+1
	}


	private Integer getMemorySegment(Command command) {
		Integer memorySegmentAddress=0;
		if(command.getArg1().equals(MemorySegment.CONSTANT.getOperation())) {
			memorySegmentAddress = Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.LOCAL.getOperation())) {
			memorySegmentAddress = hackRam.get(LCL) + Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.ARGUMENT.getOperation())) {
			memorySegmentAddress = hackRam.get(ARG) + Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.THIS.getOperation())) {
			memorySegmentAddress = hackRam.get(THIS) + Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.THAT.getOperation())) {
			memorySegmentAddress = hackRam.get(THAT) + Integer.valueOf(command.getArg2());
		}
		return memorySegmentAddress;
	}

	private void generateArithmeticCommand(Command command) {
		generateCommentArithmeticCommand(command);
	}


	private void generateCommentArithmeticCommand(Command command) {
		builder
		.append("//")
		.append(command.getArg1())
		.append(System.lineSeparator());
	}
}
