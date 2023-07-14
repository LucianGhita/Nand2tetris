package nand2tetris.translator.part.one;

import java.util.List;

public class CodeWriter {
	private List<Command> commands;
	
	public CodeWriter(List commands) {
		this.commands = commands;
	}
	
	
	private void populateCommandList() {
		for (var command : commands) {
			String commandType = command.getType();
			if (commandType.equals(Parser.C_ARITHMETIC)) {
				generateArithmeticCommand(command);
			} else if (commandType.equals(Parser.C_PUSH) || commandType.equals(Parser.C_POP)) {
				generatePushPopCommand(command);
			}
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
		// TODO Auto-generated method stub
		
	}


	private void generatePushCommand(Command command) {
		// TODO Auto-generated method stub
		
	}


	private void generateArithmeticCommand(Command command) {
		// TODO Auto-generated method stub
		
	}
}
