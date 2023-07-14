package nand2tetris.translator.part.one;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	private String vmFileContent;
	public final static String C_ARITHMETIC = "C_ARITHMETIC";
	public final static String C_PUSH		= "C_PUSH";
	public final static String C_POP  	    = "C_POP";
	
	public final static String POP  = "pop";
	public final static String PUSH = "push";
	
	
	private List<Command> commands = new ArrayList<>();
	
	public Parser(String vmFileContent) {
		this.vmFileContent = vmFileContent;
	}

	public void parseCommands(){
		if (vmFileContent != null) {
			String[] codeLines = vmFileContent.split(System.lineSeparator());
			for (var line : codeLines) {
				line = handleInlineComments(line).stripTrailing().stripLeading();
				if (!line.isBlank() && !line.startsWith("//")) {
					if (line.startsWith(PUSH) || line.startsWith(POP)) {
						Command command = getPushPop(line);
						commands.add(command);
					} else {
						Command command = getArithmethicCommand(line);
						commands.add(command);
					}
				}
			}
		}
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
