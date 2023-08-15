package nand2tetris.translator.part.two;

import java.util.Arrays;
import java.util.Optional;

public enum MemorySegment {
	
	LOCAL("local"),
	ARGUMENT("argument"),
	THIS("this"),
	THAT("that"),
	CONSTANT("constant"),
	STATIC("static"),
	POINTER("pointer"),
	TEMP("temp");

	private String memorySegment;
	
	MemorySegment(String operation) {
		this.memorySegment = operation;
	}
	
	public String getOperation() {
		return memorySegment;
	}
	
	public static Optional<MemorySegment> get(String url) {
        return Arrays.stream(MemorySegment.values())
            .filter(env -> env.memorySegment.equals(url))
            .findFirst();
    }
}
