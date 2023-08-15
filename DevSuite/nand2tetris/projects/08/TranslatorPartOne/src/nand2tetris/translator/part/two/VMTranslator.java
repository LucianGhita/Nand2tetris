package nand2tetris.translator.part.two;

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
			StringBuilder fileContent = new StringBuilder();
			String asmFileName = "";
			try {
				asmFileName = path.getFileName().toString();
				if(!path.toFile().isDirectory()) {
					fileContent.append(Files.readString(path));
					
				} else {
					final File[] files = path.toFile().listFiles();
					for (var file : files) {
						if (file.getAbsolutePath().endsWith(".vm")) {
							fileContent.append(Files.readString(file.toPath()));
							fileContent.append(System.lineSeparator());
						}
					}
				}
				Parser parser = new Parser(fileContent.toString());
				parser.parseCommands();
				commands = parser.getCommands();
			} catch (IOException e) {
				System.out.println("The file " + asmFileName + " at the filepath specified does not exist or it cannot be accessed");
			}
			
			Path assemblyFilePath = getAssemblyFilePath(path);
			
			CodeWriter writer = new CodeWriter(commands, assemblyFilePath);
			writer.generate();
		} else {
			System.out.println("The translator only takes one file or directory at a time.");
		}
		
	}

	private static Path getAssemblyFilePath(Path asmFileName) {
		String asmName = asmFileName.toAbsolutePath().toString();
		Path assemblyFilePath = null;
		if (asmName.endsWith(".vm")) {
			asmName = asmName.substring(0, asmName.lastIndexOf(".")) + ".asm";
			String pwd = ""; 
			try {
				pwd = asmName.substring(0, asmName.lastIndexOf(File.separator));
			} catch (IndexOutOfBoundsException e) {
				pwd = asmName.substring(0, asmName.lastIndexOf("\\"));
			}
			
			assemblyFilePath = Path.of(pwd + File.separator + asmName);
		} else {
			final String fileName = asmFileName.toFile().getName();
			final String pwd = asmFileName.toFile().getParent();
			assemblyFilePath = Path.of(pwd + File.separator + fileName + File.separator +fileName + ".asm");
		}
		
		return assemblyFilePath;
	}
	
	
}
