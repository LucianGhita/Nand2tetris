package nand2tetris.translator.part.one;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VMTranslator {
	
	
	public static void main(String[] args) {
		if(args.length == 1 ) {
			Path path = Path.of(args[0]);
			String fileContent = null;
			try {
				fileContent = Files.readString(path);
				Parser parser = new Parser(fileContent);
				parser.parseCommands();
				List<Command> commands = parser.getCommands();
				if (commands.size() > 0) {
					System.out.println("Here we have some commands");
				}
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
			
			try {
				Path assemblyFilePath = Path.of(pwd + File.separator + asmName);
				Files.write(assemblyFilePath, "Nothing".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("The translator only takes one file at a time.");
		}
		
	}
	
	
}
