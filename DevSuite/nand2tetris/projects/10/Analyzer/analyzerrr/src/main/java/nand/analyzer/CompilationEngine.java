package nand.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CompilationEngine {
	
	private JackTokenizer tokenizer;

	private Token currentToken;
	
	public CompilationEngine(JackTokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
	
	private int tabNo = 0;
	public void compile() {
		List<TokenizedFile> tokenizedFiles = tokenizer.getTokenizedFiles();
		if (tokenizedFiles != null) {
			for (var tokenizedFile : tokenizedFiles) {
				compileTokenizedFile(tokenizedFile);
			}
		}
	}
	
	private void compileTokenizedFile(TokenizedFile tokenizedFile) {
		compileClass(tokenizedFile);
	}

	private void compileClass(TokenizedFile tokenizedFile) {
		try {
			advance(tokenizedFile); 
			if (isKeyword("class")) {
				String compiledFile = createXmlFile(tokenizedFile);
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(compiledFile))) {
					writer.append("<class>");
					writer.newLine();
					tabNo += 1;
					writeTag(writer);
					advance(tokenizedFile);
					if (isIdentifier()) {
						writeTag(writer);
						advance(tokenizedFile);
						if (isSymbol("{")) {
							writeTag(writer);
							advance(tokenizedFile);
							compileClassVarDec(tokenizedFile, writer);
							compileSubroutineDec(tokenizedFile, writer);
						} {
							// error
						}
					} else {
						// error
					}
					writer.append("</class>");
				}
			} else {
				// error
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void compileClassVarDec(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		if (isKeyword("static") || isKeyword("field")) {
			tabNo += 1;
			
			writeTag(writer);
			
			advance(tokenizedFile);
			
			compileType(writer);

			advance(tokenizedFile);
			
			compileVarName(writer);
			
			tabNo -= 1;
		}
	}

	private void compileVarName(BufferedWriter writer) throws IOException {
		if (isIdentifier()) {
			writeTag(writer);
		}
	}

	private void compileType(BufferedWriter writer) throws IOException {
		if (isKeyword("int") || isKeyword("char") || isKeyword("boolean") || isIdentifier()) {
			writeTag(writer);
		} else {
			// error here!
		}
	}

	private void compileSubroutineDec(TokenizedFile tokenizedFile, BufferedWriter writer) {
		
	}

	

	private void writeTag(BufferedWriter writer) throws IOException {
		appendTabs(writer, tabNo);
		writer.append("<" + currentType() + ">");
		writer.append(currentToken.getContent());
		writer.append("</" + currentType()  + ">");
		writer.newLine();
	}


	private void appendTabs(BufferedWriter writer, int tabNo) throws IOException {
		for (int i = 0; i < tabNo; i++) {
			writer.append("\t");
		}
	}


	private String currentType() {
		return currentToken.getType().toString().toLowerCase();
	}

	private void advance(TokenizedFile tokenizedFile) throws Exception {
		currentToken = tokenizedFile.advance();
	}


	
	private boolean isKeyword(String content) {
		return currentToken.isKeyword() && currentToken.getContent().equals(content);
	}
	
	private boolean isSymbol(String content) {
		return currentToken.isSymbol() && currentToken.getContent().equals(content);
	}
	
	private boolean isIdentifier() {
		return currentToken.isIdentifier();
	}
	
	private String createXmlFile(TokenizedFile tokenizedFile) {
		Path filePath = tokenizedFile.getFilePath();
		Path fileParent = filePath.getParent();
		String fileName = filePath.getFileName().toString().split("\\.")[0];
		String compiledFile = fileParent.toString() + File.separator +  fileName + "Tokenized.xml";
		return compiledFile;
	}


	
}
