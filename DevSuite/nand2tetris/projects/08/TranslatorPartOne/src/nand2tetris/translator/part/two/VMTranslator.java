package nand2tetris.translator.part.two;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VMTranslator {
	
	
	public static void main(String[] args) {
		if(args.length == 1 ) {
			Path path = Path.of(args[0]);
			StringBuilder fileContent = new StringBuilder();
			String asmFileName = "";
			try {
				asmFileName = path.getFileName().toString();
				if(!path.toFile().isDirectory()) {
					fileContent.append(Files.readString(path));
					parseAndGenerate(path, fileContent);
				} else {
					writeInit(fileContent);
					final File[] files = path.toFile().listFiles();
					for (var file : files) {
						if (file.getAbsolutePath().endsWith(".vm")) {
							fileContent.append(Files.readString(file.toPath()));
							fileContent.append(System.lineSeparator());
						}
					}
					parseAndGenerate(path, fileContent);
				}
			} catch (IOException e) {
				System.out.println("The file " + asmFileName + " at the filepath specified does not exist or it cannot be accessed");
			}
		}
		
	}

	private static void writeInit(StringBuilder fileContent) {
		fileContent.append("SP.Init").append(System.lineSeparator());
		fileContent.append("call Sys.init 0").append(System.lineSeparator());
	}

	private static void parseAndGenerate(Path path, StringBuilder fileContent) {
		List<Command> commands;
		Parser parser = new Parser(fileContent.toString());
		parser.parseCommands();
		commands = parser.getCommands();
		
		Path assemblyFilePath = getAssemblyFilePath(path);
		CodeWriter writer = new CodeWriter(commands, assemblyFilePath);
		writer.generate();
	}

	private static Path getAssemblyFilePath(Path asmFileName) {
		final String fileNameString = asmFileName.toString();
		if (fileNameString.endsWith(".vm")) {
			return Path.of(fileNameString.substring(0, fileNameString.lastIndexOf(".")) + ".asm"); 
		}
 		String asmName = asmFileName.toAbsolutePath().toString();
		asmName = asmName + File.separator + asmFileName.getFileName() + ".asm";
		return Path.of(asmName);
	}
	
	
}
