package nand2tetris.translator.part.two;

import java.util.Arrays;
import java.util.Optional;

public enum ArithmethicOperation {
	ADD("add"),
	SUB("sub"),
	NEG("neg"),
	EQ("eq"),
	GT("gt"),
	LT("lt"),
	AND("and"),
	OR("or"),
	NOT("not");
	
	private String operation;
	
	ArithmethicOperation(String operation) {
		this.operation = operation;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public static Optional<ArithmethicOperation> get(String url) {
        return Arrays.stream(ArithmethicOperation.values())
            .filter(env -> env.operation.equals(url))
            .findFirst();
    }
	
}
