package nand2tetris.translator.part.one;

public class Command {
	
	public Command() {}
	public Command(String type, String arg1, String arg2, String operation) {
		this.type = type;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	private String type;
	private String arg1;
	private String arg2;
	
	public String getType() {
		return type;
	}
	public void setType(String commandType) {
		this.type = commandType;
	}
	public String getArg1() {
		return arg1;
	}
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}
	public String getArg2() {
		return arg2;
	}
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}
}
