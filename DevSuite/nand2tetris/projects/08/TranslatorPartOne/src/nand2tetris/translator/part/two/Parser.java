package nand2tetris.translator.part.two;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	private String vmFileContent;
	public final static String C_ARITHMETIC = "C_ARITHMETIC";
	public final static String C_PUSH		= "C_PUSH";
	public final static String C_POP  	    = "C_POP";
	public final static String C_LABEL 		= "C_LABEL";
	public final static String C_GOTO	 	= "C_GOTO";
	public final static String C_IF			= "C_IF";
	public final static String C_FUNCTION 	= "C_FUNCTION";
	public final static String C_RETURN 	= "C_RETURN";
	public final static String C_CALL		= "C_CALL";
	
	public final static String POP   = "pop";
	public final static String PUSH  = "push";
	
	public final static String IF    = "if-goto";
	public final static String LABEL = "label";
	public final static String GOTO  = "goto";
	
	public final static String FUNCTION = "function";
	public final static String CALL = "call";
	public final static String RETURN = "return";
	
	public final static String SP_INIT = "SP_INIT";
	
	
	private List<Command> commands = new ArrayList<>();
	
	public Parser(String vmFileContent) {
		this.vmFileContent = vmFileContent;
	}

	public void parseCommands(){
		if (vmFileContent != null) {
			String[] codeLines = vmFileContent.split(System.lineSeparator());
			for (var line : codeLines) {
				line = handleInlineComments(line).stripTrailing().stripLeading();
				Command command;
				if (!line.isBlank() && !line.startsWith("//")) {
					if (line.startsWith(PUSH) || line.startsWith(POP)) {
						command = getPushPop(line);
					} else if (line.startsWith(IF) || line.startsWith(LABEL) || line.startsWith(GOTO) ) {
						command = getBranchingCommand(line);
					} else if (line.startsWith(FUNCTION) || line.startsWith(CALL) || line.startsWith(RETURN)) {
						command = getFunctionCommands(line);
					} else if (line.equals("SP.Init")) {
						command = new Command();
						command.setType(SP_INIT);
					} else {
						command = getArithmethicCommand(line);
					}
					commands.add(command);
				}
			}
		}
	}
		
	private Command getFunctionCommands(String line) {
		Command command = new Command();
		String[] functionCommand = line.split("\\s+");
		if (functionCommand[0].startsWith(FUNCTION)) {
			command.setType(C_FUNCTION);
			command.setArg1(functionCommand[1]);
			command.setArg2(functionCommand[2]);
		} else if (functionCommand[0].startsWith(CALL)) {
			command.setType(C_CALL);
			command.setArg1(functionCommand[1]);
			command.setArg2(functionCommand[2]);
		} else if (functionCommand[0].startsWith(RETURN)) {
			command.setType(C_RETURN);
		}
		return command;
	}

	private Command getBranchingCommand(String line) {
		Command command = new Command();
		String[] branchingCommand = line.split("\\s+");
		if (branchingCommand[0].startsWith(IF)) {
			command.setType(C_IF);
		} else if (branchingCommand[0].startsWith(GOTO)) {
			command.setType(C_GOTO);
		} else if (branchingCommand[0].startsWith(LABEL)) {
			command.setType(C_LABEL);
		}
		
		command.setArg1(branchingCommand[1]);
		return command;
	}

	private Command getArithmethicCommand(String line) {
		Command command = new Command();
		
		command.setType(Parser.C_ARITHMETIC);
		for (var operation : ArithmethicOperation.values()) {
			final String operationString = operation.getOperation();
			if (line.equals(operationString)) {
				command.setArg1(operationString);
				break;
			}
		}
		return command;
	}

	private Command getPushPop(String line) {
		Command command = new Command();
		
		String[] pushPop = line.split("\\s+");
		if (pushPop.length != 3) {
			throw new IllegalArgumentException ("push/pop command should have only two arguments");
		}
		
		if (pushPop[0].equals(PUSH)) {
			command.setType(C_PUSH);
		} else {
			command.setType(C_POP);
		}
		
		for (var operation : MemorySegment.values()) {
			final String operationString = operation.getOperation();
			if (pushPop[1].equals(operationString)) {
				command.setArg1(operationString);
				break;
			}
		}
		
		command.setArg2(pushPop[2]);
		
		return command;
	}


	public static String handleInlineComments(String line) {
		if(line.contains("//") && !line.startsWith("//")) {
			line = line.substring(0, line.indexOf("/"));
		}
		return line;
	}
	
	public List<Command> getCommands() {
		return commands;
	}
}
