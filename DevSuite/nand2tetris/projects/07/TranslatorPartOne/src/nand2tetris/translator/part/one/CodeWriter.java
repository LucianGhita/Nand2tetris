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
	private int arithmeticLabelNo = 0;

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
	
	private final static String ADD = "M=D+M";
	private final static String SUB = "M=M-D";
	private final static String EQ  = "D=M;JEQ";
	private final static String GT  = "D=M;JGT";
	private final static String LT  = "D=M;JLT";
	private final static String NEG = "M=-M";
	private final static String AND = "M=D&M";
	private final static String OR  = "M=D|M";
	private final static String NOT = "M=!M";
	
	static {
		hackRam.put(SP,   	  		0);
		hackRam.put(LCL,   	  		1);
		hackRam.put(ARG,  	  		2);
		hackRam.put(THIS, 	  		3);
		hackRam.put(THAT, 	  	    4);
		hackRam.put(TEMP,  	  	    5);
		hackRam.put(STATIC,  	   16);
		hackRam.put(POINTER_THIS,   3);
		hackRam.put(POINTER_THAT,   4);
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
			if (command.getArg1().equals("temp")) {
				generatePopCommandTemp(segment);
			} else {
				segment = getStaticAddress(command, segment);
				generatePopCommand(command, segment);
			}
		}
		
	}

	private void generatePopCommandTemp(String segment) {
		builder
		.append("@").append(segment).append(System.lineSeparator()) 			  //@segment
		.append("D=A").append(System.lineSeparator())							  // D=A
		.append(GENERAL_PURPOSE_REGISTER)										  // @R13
		.append("M=D").append(System.lineSeparator())							  // M=D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		  // @SP
		.append("M=M-1").append(System.lineSeparator()) 						  // M=M-1
		.append("A=M").append(System.lineSeparator())						      // A=M
		.append("D=M").append(System.lineSeparator())							  // D=M
		.append(GENERAL_PURPOSE_REGISTER)										  // @R13
		.append("A=M").append(System.lineSeparator())							  // A=M
		.append("M=D").append(System.lineSeparator())							  // M=D
		;
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
		.append("A=M").append(System.lineSeparator()) 							  // A=M
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
			Integer memorySegmentAddress = getBaseMemorySegment(command);
			String segment = String.valueOf(memorySegmentAddress);
			if (command.getArg1().equals("temp")) {
				generateTempPush(segment);
			} else if (command.getArg1().equals("constant")){
				generateConstantPushCommand(segment);
			} else {
				segment = getStaticAddress(command, segment);
				generatePushCommand(command, segment);
			}
		}
	
	}

	private void generatePushCommand(Command command, String memorySegmentAddress) {
		builder
		.append("@").append(memorySegmentAddress).append(System.lineSeparator()) 				 // @ constant no
		.append("D=M").append(System.lineSeparator())											 // D=M
		.append("@").append(command.getArg2()).append(System.lineSeparator())					 // @arg2
		.append("D=D+A").append(System.lineSeparator())											 // D=D+A      // Append the base segment to argument.
		.append("A=D").append(System.lineSeparator()) 						 					 // A=D
		.append("D=M").append(System.lineSeparator()) 											 // D=M
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())  					 // @SP
		.append("A=M").append(System.lineSeparator()) 					  	 					 // A=M
		.append("M=D").append(System.lineSeparator())						  					 // M=D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())	  					 // @SP
		.append("M=M+1").append(System.lineSeparator()) 										 // M=M+1
		;
	}

	private void generateTempPush(String segment) {
		builder
		.append("@").append(segment).append(System.lineSeparator())								 //@segment
		.append("D=M").append(System.lineSeparator())											 // D=M
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())  					 // @SP
		.append("A=M").append(System.lineSeparator()) 					  	 					 // A=M
		.append("M=D").append(System.lineSeparator())						  					 // M=D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())	  					 // @SP
		.append("M=M+1").append(System.lineSeparator()) 										 // M=M+1
		;
	}

	private void generatePushPointer(Command command) {
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

	private void generateConstantPushCommand(String memorySegmentAddress) {
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
	    } else if (command.getArg1().equals(MemorySegment.CONSTANT.getOperation())) {
			memorySegmentAddress = Integer.valueOf(command.getArg2());
		}
		return memorySegmentAddress;
	}

	
	private void generateArithmeticCommand(Command command) {
		generateCommentArithmeticCommand(command);
		System.out.println("ArithmeticCommand");
		final String arg1 = command.getArg1();
		if (arg1.equals("add")) {
			generateTwoOperandsOperation(ADD);
		} else if (arg1.equals("sub")) {
			generateTwoOperandsOperation(SUB);
		} else if (arg1.equals("neg")) {
			generateNeg(NEG);
		} else if (arg1.equals("eq")) {
			generateLogicComparisons(EQ);
		} else if (arg1.equals("gt")) {
			generateLogicComparisons(GT);
		} else if (arg1.equals("lt")) {
			generateLogicComparisons(LT);
		} else if (arg1.equals("and")) {
			generateTwoOperandsOperation(AND);
		} else if (arg1.equals("or")) {
			generateTwoOperandsOperation(OR);
		} else if (arg1.equals("not")) {
			generateNeg(NOT);
		}
	}

	private void generateLogicComparisons(String operation) {		
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("AM=M-1").append(System.lineSeparator())  						// AM=M-1
		.append("@PUSHT" + arithmeticLabelNo).append(System.lineSeparator())  									// (@PUSHT)
		.append("D=M;JEQ").append(System.lineSeparator())						// JUMP @PUSHF
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("M=0").append(System.lineSeparator())							// M=0								// M=0
		.append("@CONTINUE" + arithmeticLabelNo).append(System.lineSeparator())	// @CONTINUE
		.append("0;JMP").append(System.lineSeparator())							// 0;JMP
		.append("(PUSHT" + arithmeticLabelNo + ")").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("M=-1").append(System.lineSeparator())							// M=-1
		.append("(CONTINUE" + arithmeticLabelNo + ")").append(System.lineSeparator()) // (CONTINUE)
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M+1").append(System.lineSeparator())							// M=M+1
		;
		arithmeticLabelNo++;
	}

	private void generateNeg(String operation) {
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M-1").append(System.lineSeparator())  						// M=M-1
		.append("A=M").append(System.lineSeparator())							// A=M
		.append(operation).append(System.lineSeparator())							// M=-M
		;
	}

	private void generateTwoOperandsOperation(String operation) {
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M-1").append(System.lineSeparator())  						// M=M-1
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("D=M").append(System.lineSeparator())							// D=M					
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M-1").append(System.lineSeparator())							// M=M-1
		.append("A=M").append(System.lineSeparator())							// A=M
		.append(operation).append(System.lineSeparator())							// D=M+D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M+1").append(System.lineSeparator())	
		;
	}
	
	private void generateAddOperation() {
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M-1").append(System.lineSeparator())  						// M=M-1
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("D=M").append(System.lineSeparator())							// D=M	
		.append(GENERAL_PURPOSE_REGISTER)
		.append("M=D").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M-1").append(System.lineSeparator())							// M=M-1
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("D=M").append(System.lineSeparator())
		.append(GENERAL_PURPOSE_REGISTER)
		.append("D=D+M").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("M=D").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M+1").append(System.lineSeparator())	
		;
	}
	
	private void generateSubOperation() {
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M-1").append(System.lineSeparator())  						// M=M-1
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("D=M").append(System.lineSeparator())							// D=M	
		.append(GENERAL_PURPOSE_REGISTER)
		.append("M=D").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M-1").append(System.lineSeparator())							// M=M-1
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("D=M").append(System.lineSeparator())
		.append(GENERAL_PURPOSE_REGISTER)
		.append("D=D-M").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("A=M").append(System.lineSeparator())							// A=M
		.append("M=D").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M+1").append(System.lineSeparator())
		;
	}

	private void generateCommentArithmeticCommand(Command command) {
		builder
		.append("//")
		.append(command.getArg1())
		.append(System.lineSeparator());
	}
}