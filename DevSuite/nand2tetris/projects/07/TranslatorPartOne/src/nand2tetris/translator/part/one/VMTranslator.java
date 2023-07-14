package nand2tetris.translator.part.one;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VMTranslator {
	
	
	public static void main(String[] args) {
		List<Command> commands = new ArrayList<>();
		if(args.length == 1 ) {
			Path path = Path.of(args[0]);
			String fileContent = null;
			try {
				fileContent = Files.readString(path);
				Parser parser = new Parser(fileContent);
				parser.parseCommands();
				commands = parser.getCommands();
			} catch (IOException e) {
				System.out.println("The file " + path.getFileName() + " at the filepath specified does not exist or it cannot be accessed");
			}
			
			String asmName = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf(".")) + ".asm";
			String pwd = ""; 
			try {
				pwd = path.toString().substring(0, path.toString().lastIndexOf(File.separator));
			} catch (IndexOutOfBoundsException e) {
				pwd = path.toString().substring(0, path.toString().lastIndexOf("\\"));
			}
			
			Path assemblyFilePath = Path.of(pwd + File.separator + asmName);
			CodeWriter writer = new CodeWriter(commands, assemblyFilePath);
			writer.generate();
		} else {
			System.out.println("The translator only takes one file at a time.");
		}
		
	}
	
	
}
