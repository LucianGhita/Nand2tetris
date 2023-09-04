package nand2tetris.translator.part.two;

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
	
	// Maybe another way for generating unique label prefixes can be used, but it is good enough for the hack computer.
	
	private int arithmeticLabelNo = 0;
	private int callNumber = 0;
	
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
	private final static String GENERAL_PURPOSE_REGISTER_2= "@R14" + System.lineSeparator();
	private final static String ADD = "M=D+M";
	private final static String SUB = "M=M-D";
	private final static String EQ  = "D;JEQ";
	private final static String GT  = "D;JGT";
	private final static String LT  = "D;JLT";
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
			} else if (commandType.equals(Parser.C_LABEL) || commandType.equals(Parser.C_IF) || commandType.equals(Parser.C_GOTO)) {
				generateBranchingCommand(command);
			} else if (commandType.equals(Parser.C_FUNCTION)) {
				generateFunction(command);
			} else if (commandType.equals(Parser.C_CALL)) {
				generateCall(command);
			} else if (commandType.equals(Parser.C_RETURN)) {
				generateReturn();
			} else if (commandType.equals(Parser.SP_INIT)) {
				generateSPInit();
			}
		}
		try {
			Files.write(filePath, builder.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void generateSPInit() {
		builder
		.append("@256").append(System.lineSeparator())
		.append("D=A").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		  // @SP
		.append("M=D").append(System.lineSeparator())
		;
	
	}
	
	private void generateReturn() {
		generateReturnComment();
		
		// endframe = LCL
		generateEndFrame();
		// retAddr = *(endframe-5)
		saveReturnAddress();
		
		// *ARG = pop()
		popReturnValueIntoArgValue();
		
		// SP = ARG + 1
		repositionSP();
		
		// THAT = *(endframe-1)
		endFrameMinusArg("@1", "@" + hackRam.get(THAT) + System.lineSeparator());
		// THIS = *(endframe-2)
		endFrameMinusArg("@2", "@" + hackRam.get(THIS) + System.lineSeparator());
		// ARG = *(endframe-3)
		endFrameMinusArg("@3", "@" + hackRam.get(ARG) + System.lineSeparator());
		// LCL = *(endframe-4)
		endFrameMinusArg("@4", "@" + hackRam.get(LCL) + System.lineSeparator());
		
		// goto retAddr
		gotoRetAddr();
		
	}

	private void gotoRetAddr() {
		builder
		.append(GENERAL_PURPOSE_REGISTER)
		.append("A=M").append(System.lineSeparator())
		.append("0;JMP").append(System.lineSeparator())
		;
	}

	private void repositionSP() {
		builder.append("@").append(hackRam.get(ARG)).append(System.lineSeparator())
		.append("D=M").append(System.lineSeparator())
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		  // @SP
		.append("M=D+1").append(System.lineSeparator());
		;
	}

	private void popReturnValueIntoArgValue() {
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		  // @SP
		.append("M=M-1").append(System.lineSeparator()) 						  // M=M-1
		.append("A=M").append(System.lineSeparator()) 							  // A=M
		.append("D=M").append(System.lineSeparator()) 							  // D=M
		.append("@")
		.append(hackRam.get(ARG)).append(System.lineSeparator())
		.append("A=M").append(System.lineSeparator())
		.append("M=D").append(System.lineSeparator())
		;
	}

	private void saveReturnAddress() {
		endFrameMinusArg("@5", GENERAL_PURPOSE_REGISTER_2);
//		builder
//		.append("@5").append(System.lineSeparator())
//		.append("D=A").append(System.lineSeparator())
//		.append(GENERAL_PURPOSE_REGISTER)
//		.append("D=M-D").append(System.lineSeparator())
//		.append("A=D").append(System.lineSeparator())
//		.append("D=M").append(System.lineSeparator())
//		.append(GENERAL_PURPOSE_REGISTER_2)
//		.append("M=D").append(System.lineSeparator())
//		;
	}

	private void generateEndFrame() {
		builder
		.append("@")
		.append(hackRam.get(LCL)).append(System.lineSeparator())		// @LCL
		.append("D=M").append(System.lineSeparator())
		.append(GENERAL_PURPOSE_REGISTER)
		.append("M=D").append(System.lineSeparator())
		;
	}

	private void endFrameMinusArg(String arg, String memSegment) {
		builder
		.append(arg).append(System.lineSeparator())
		.append("D=A").append(System.lineSeparator())
		.append(GENERAL_PURPOSE_REGISTER)
		.append("D=M-D").append(System.lineSeparator())
		.append("A=D").append(System.lineSeparator())
		.append("D=M").append(System.lineSeparator())
		.append(memSegment)
		.append("M=D").append(System.lineSeparator())
		;
	}

	private void generateReturnComment() {
		builder.append("//").append("return").append(System.lineSeparator());
	}

	private void generateCall(Command command) {
		appendCallComment(command);

		// push returnAddr
		final String returnAddressString = command.getArg1() + "$"+ callNumber;
		saveToStack(returnAddressString);
		
		// push LCL
		saveToStack(String.valueOf(hackRam.get(LCL)));
		
		// push ARG 
		saveToStack(String.valueOf(hackRam.get(ARG)));
		
		// push THIS
		saveToStack(String.valueOf(hackRam.get(THIS)));
		
		// push THAT
		saveToStack(String.valueOf(hackRam.get(THAT)));
		
		// ARG=SP-5-nArgs
		repositionARG(command);
		
		// LCL = SP
		repositionLCL();
		
		// goto functionName
		executeCallFunction(command);
				
		// (command.getArg1()+$+callNumber)
		builder
		.append("(").append(returnAddressString).append(")").append(System.lineSeparator());
		
		callNumber++;
		
	}

	private void appendCallComment(Command command) {
		builder
		.append("//").append("call").append(SPACE).append(command.getArg1()).append(SPACE).append(command.getArg2()).append(System.lineSeparator());
	}

	private void executeCallFunction(Command command) {
		builder
		.append("@").append(command.getArg1()).append(System.lineSeparator())			// @command.getArg1()
		.append("0;JMP").append(System.lineSeparator())									// 0;JMP
		;
	}

	private void repositionLCL() {
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		        		// @SP
		.append("D=M").append(System.lineSeparator())											//D=M
		.append("@").append(hackRam.get("LCL")).append(System.lineSeparator())					// @LCL
		.append("M=D").append(System.lineSeparator());	// M=D
	}

	private void repositionARG(Command command) {
		builder
		.append("@5").append(System.lineSeparator()) 											// @5 
		.append("D=A").append(System.lineSeparator())											// D=A
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		        		// @SP
		.append("D=M-D").append(System.lineSeparator())											// D=M-D
		.append("@").append(command.getArg2()).append(System.lineSeparator())					// @nArgs
		.append("D=D-A").append(System.lineSeparator())											// D=D-A								
		.append("@").append(String.valueOf(hackRam.get(ARG))).append(System.lineSeparator())	// @ARG
		.append("M=D").append(System.lineSeparator())											// M=D-nArgs
		;
	}

	private void saveToStack(final String savedAdress) {
		builder
		.append("@").append(savedAdress).append(System.lineSeparator())  // @returnAddr_callNumber
		.append("D=A").append(System.lineSeparator())									// D=A
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		        // @SP
		.append("A=M").append(System.lineSeparator())									// A=M
		.append("M=D").append(System.lineSeparator())									// M=D
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())				// @SP
		.append("M=M+1").append(System.lineSeparator()) 						  	    // M=M+1
		;
	}

	private void generateFunction(Command command) {
		generateFunctionComment(command);
		// generate function label
		builder.append("(").append(command.getArg1()).append(")").append(System.lineSeparator());
		int nVar = Integer.valueOf(command.getArg2());
		// repeat nVar times
		// push 0
		pushZeroNVarTimes(nVar);
	}

	private void pushZeroNVarTimes(int nVar) {
		for (int i=0; i< nVar; i++) {
			builder
			.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		        // @SP
			.append("A=M").append(System.lineSeparator())									// A=M
			.append("M=0").append(System.lineSeparator())									// M=0
			.append("@").append(hackRam.get(SP)).append(System.lineSeparator())				// @SP
			.append("M=M+1").append(System.lineSeparator()) 						  	    // M=M+1
			;
		}
	}

	private void generateFunctionComment(Command command) {
		builder
		.append("//")
		.append(command.getArg1())
		.append(SPACE)
		.append(command.getArg2())
		.append(System.lineSeparator());
	}

	private void generateBranchingCommand(Command command) {
		generateCommentBranching(command);
		if (command.getType().equals(Parser.C_LABEL) ) {
			generateLabel(command);
		} else if (command.getType().equals(Parser.C_GOTO)) {
			generateGoto(command);
		} else if (command.getType().equals(Parser.C_IF)) {
			generateIfGoto(command);
		}
	}

	private void generateIfGoto(Command command) {
		builder
		.append("@")
		.append(hackRam.get(SP)).append(System.lineSeparator())
		.append("M=M-1").append(System.lineSeparator())
		.append("A=M").append(System.lineSeparator())
		.append("D=M").append(System.lineSeparator())
		.append("@").append(command.getArg1()).append(System.lineSeparator())
		.append("D;JGT").append(System.lineSeparator())
		.append("D;JLT").append(System.lineSeparator())
		;
	}

	private void generateCommentBranching(Command command) {
		builder
		.append("//" + command.getType() + " " + command.getArg1())
		.append(System.lineSeparator());
	}

	private void generateGoto(Command command) {
		executeCallFunction(command);
		
	}

	private void generateLabel(Command command) {
		// (LABEL)
		builder
		.append("(")
		.append(command.getArg1())
		.append(")")
		.append(System.lineSeparator());
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
		saveToStack(memorySegmentAddress);					  					 
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
		if (arg1 != null) {
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
	}

	private void generateLogicComparisons(String operation) {		
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())			    // @SP
		.append("M=M-1").append(System.lineSeparator())  							    // M=M-1
		.append("A=M").append(System.lineSeparator())  							        // A=M
		.append("D=M").append(System.lineSeparator())  							        // D=M
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())			    // @SP
		.append("M=M-1").append(System.lineSeparator())  							    // M=M-1
		.append("A=M").append(System.lineSeparator())  							        // A=M
		.append("D=M-D").append(System.lineSeparator())									// D-M
		.append("@PUSHT" + arithmeticLabelNo).append(System.lineSeparator())	  	    // (@PUSHT)
		.append(operation).append(System.lineSeparator())							    // JUMP @PUSHF
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())			    // @SP
		.append("A=M").append(System.lineSeparator())								    // A=M
		.append("M=0").append(System.lineSeparator())								    // M=0								
		.append("@CONTINUE" + arithmeticLabelNo).append(System.lineSeparator())		    // @CONTINUE
		.append("0;JMP").append(System.lineSeparator())								    // 0;JMP
		.append("(PUSHT" + arithmeticLabelNo + ")").append(System.lineSeparator())    // (PUSHT)
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		   		// @SP
		.append("A=M").append(System.lineSeparator())									// A=M
		.append("M=-1").append(System.lineSeparator())									// M=-1
		.append("(CONTINUE" + arithmeticLabelNo + ")").append(System.lineSeparator()) // (CONTINUE)
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())				// @SP
		.append("M=M+1").append(System.lineSeparator())									// M=M+1
		;
		arithmeticLabelNo++;
	}

	private void generateNeg(String operation) {
		builder
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M-1").append(System.lineSeparator())  						// M=M-1
		.append("A=M").append(System.lineSeparator())							// A=M
		.append(operation).append(System.lineSeparator())						// M=-M
		.append("@").append(hackRam.get(SP)).append(System.lineSeparator())		// @SP
		.append("M=M+1").append(System.lineSeparator())  						// M=M-1
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
		.append(operation).append(System.lineSeparator())						// D=M+D
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