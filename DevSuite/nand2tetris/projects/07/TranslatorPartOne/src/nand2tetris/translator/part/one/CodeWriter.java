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
	
	private final static String SP      = "SP";
	private final static String LCL     = "LCL";
	private final static String ARG     = "ARG";
	private final static String THIS    = "THIS";
	private final static String THAT    = "THAT";
	private final static String STATIC  = "STATIC";
	private final static String TEMP    = "TEMP";
	private final static String POINTER_THIS = "POINTER_THIS";
	private final static String POINTER_THAT = "POINTER_THAT";
	
	private final static String GENERAL_PURPOSE_REGISTER = "@R13" + System.lineSeparator();
	
	static {
		hackRam.put(SP,   	  0);
		hackRam.put(LCL,   	  1);
		hackRam.put(ARG,  	  2);
		hackRam.put(THIS, 	  3);
		hackRam.put(THAT, 	  4);
		hackRam.put(TEMP,  	  5);
		hackRam.put(STATIC,  16);
		hackRam.put(POINTER_THIS,  3);
		hackRam.put(POINTER_THAT,  4);
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
	
	// TODO handle temp and pointer !!! 
	private void generatePopCommand(Command command) {
		generateCommentPushPop(command, true);
		
		// pop local 2
		
		// @2 
		// D=A put 2 in the D register
		// @LCL
		// D=D+M put 2 + 1015 in D => 1017
		// @addr // general purpose register
		// M=D   // put 1017 at its address
		// @SP  // decrease sp
		// M=M-1
		// A=M  // select 257
		// D=M  // get the value at 257
		// @addr // 
		// A=M  // select the register at 1017
		// M=D  // put at its value the value at 257
		
		if (command.getArg1().equals(MemorySegment.POINTER.getOperation())) {
			generatePopPointer(command);
		} else {
			Integer baseMemorySegment = getBaseMemorySegment(command);
			String segment = String.valueOf(baseMemorySegment);
			segment = getStaticAddress(command, segment);
			generatePopCommand(command, segment);
		}
		
	}


	private void generatePopPointer(Command command) {
		
		String pointer = "";
		if (command.getArg2().equals("0")) {
			pointer = String.valueOf(hackRam.get(POINTER_THIS));
		} else {
			pointer = String.valueOf(hackRam.get(POINTER_THAT));
		}
		
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		  // @SP
		.append("M=M-1").append(System.lineSeparator()) 						  // M=M-1
		.append("D=M").append(System.lineSeparator())							  // D=M
		.append("@").append(pointer).append(System.lineSeparator())				  // @pointer
		.append("M=D").append(System.lineSeparator())							  // M=D (this assigns the value SP points at)
		;
	}


	private void generatePopCommand(Command command, String segment) {
		builder
		.append("@").append(command.getArg2()).append(System.lineSeparator()) 	  // @arg2
		.append("D=A").append(System.lineSeparator()) 							  // D=A
		.append("@" + segment).append(System.lineSeparator()) 				      // @MemorySegment
		.append("D=D+M").append(System.lineSeparator()) 						  // D=D+M
		.append(GENERAL_PURPOSE_REGISTER)										  // @R13
		.append("M=D").append(System.lineSeparator())							  // M=D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		  // @SP
		.append("M=M-1").append(System.lineSeparator()) 						  // M=M-1
		.append("A=M").append(System.lineSeparator()) 							  // A=M
		.append("D=M").append(System.lineSeparator()) 							  // D=M
		.append(GENERAL_PURPOSE_REGISTER)										  // @R13
		.append("A=M").append(System.lineSeparator()) 							  // A=M
		.append("M=D").append(System.lineSeparator())							  // M=D
		;
	}


	private String getStaticAddress(Command command, String segment) {
		if (command.getArg1().equals(MemorySegment.STATIC.getOperation())) {
			segment = "Foo." + command.getArg2();
		}
		return segment;
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
		if (command.getArg1().equals(MemorySegment.POINTER.getOperation())) {
			generatePushPointer(command);
		} else {
			Integer memorySegmentAddress = getMemorySegment(command);
			String segment = String.valueOf(memorySegmentAddress);
			segment = getStaticAddress(command, segment);
			generatePushCommand(segment);
		}
	
	}


	private void generatePushPointer(Command command) {
		System.out.println("command");
		String pointer = "";
		if (command.getArg2().equals("0")) {
			pointer = String.valueOf(hackRam.get(POINTER_THIS));
		} else {
			pointer = String.valueOf(hackRam.get(POINTER_THAT));
		}
		builder
		.append("@").append(pointer).append(System.lineSeparator())
		.append("D=M").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())  					 // @SP
		.append("A=M").append(System.lineSeparator()) 					  	 					 // A=M
		.append("M=D").append(System.lineSeparator())						  					 // M=D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())	  					 // @SP
		.append("M=M+1").append(System.lineSeparator()) 										 // M=M+1
		;					  					 
		
	}


	private void generatePushCommand(String memorySegmentAddress) {
		builder
		.append("@").append(memorySegmentAddress).append(System.lineSeparator()) 				 // @ constant no
		.append("D=A").append(System.lineSeparator()) 						 					 // D=A
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())  					 // @SP
		.append("A=M").append(System.lineSeparator()) 					  	 					 // A=M
		.append("M=D").append(System.lineSeparator())						  					 // M=D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())	  					 // @SP
		.append("M=M+1").append(System.lineSeparator()) 										 // M=M+1
		;					  					 
	}


	private Integer getMemorySegment(Command command) {
		Integer memorySegmentAddress = null;
		if(command.getArg1().equals(MemorySegment.CONSTANT.getOperation())) {
			memorySegmentAddress = Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.LOCAL.getOperation())) {
			memorySegmentAddress = hackRam.get(LCL) + Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.ARGUMENT.getOperation())) {
			memorySegmentAddress = hackRam.get(ARG) + Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.THIS.getOperation())) {
			memorySegmentAddress = hackRam.get(THIS) + Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.THAT.getOperation())) {
			memorySegmentAddress = hackRam.get(TEMP) + Integer.valueOf(command.getArg2());
		} else if (command.getArg1().equals(MemorySegment.TEMP.getOperation())) {
			memorySegmentAddress = hackRam.get(TEMP) + Integer.valueOf(command.getArg2());
		} 
		return memorySegmentAddress;
	}
	
	private Integer getBaseMemorySegment(Command command) {
		Integer memorySegmentAddress = null;
		if (command.getArg1().equals(MemorySegment.LOCAL.getOperation())) {
			memorySegmentAddress = hackRam.get(LCL);
		} else if (command.getArg1().equals(MemorySegment.ARGUMENT.getOperation())) {
			memorySegmentAddress = hackRam.get(ARG);
		} else if (command.getArg1().equals(MemorySegment.THIS.getOperation())) {
			memorySegmentAddress = hackRam.get(THIS);
		} else if (command.getArg1().equals(MemorySegment.THAT.getOperation())) {
			memorySegmentAddress = hackRam.get(THAT);
		} else if (command.getArg1().equals(MemorySegment.TEMP.getOperation())) {
			memorySegmentAddress = hackRam.get(TEMP) + Integer.valueOf(command.getArg2());
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
