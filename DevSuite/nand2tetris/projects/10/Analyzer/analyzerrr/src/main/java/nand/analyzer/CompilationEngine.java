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
							
							advance(tokenizedFile);
							
							if (isSymbol("}")) {
								writeTag(writer);
							} else {
								// error
							}
								
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
		tabNo += 1;
		Token nextToken = tokenizedFile.peek();
		while (isKeyword("static", nextToken) || isKeyword("field", nextToken)) {
			
			advance(tokenizedFile);
			writeTag(writer);
			
			advance(tokenizedFile);
			compileType(writer);

			advance(tokenizedFile);
			compileVarName(writer);
			
			// compile further variable declaration
			compileFurtherVariableDeclaration(tokenizedFile, writer);
			
			advance(tokenizedFile);
			if (isSymbol(";")) {
				writeTag(writer);
			} else {
				// error here
			}
			
		}
		tabNo -= 1;
	}

	private void compileSubroutineDec(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		tabNo += 1;
		Token nextToken = tokenizedFile.peek();
		while (isKeyword("constructor", nextToken) || isKeyword("function", nextToken) || isKeyword("method", nextToken)) {
			advance(tokenizedFile);
			
			writeTag(writer);
			advance(tokenizedFile);
			
			if (isKeyword("void") || isType()) {
				writeTag(writer);
				advance(tokenizedFile);
				// compile subroutine name
				if (isIdentifier()) {
					writeTag(writer);
					
					advance(tokenizedFile);
					compileParameterList(tokenizedFile, writer);
					
					advance(tokenizedFile);
					compileSubroutineBody(tokenizedFile, writer);
					
				} else {
					// error
				}
			} else {
				// error
			}
			
		}
	}

	private void compileSubroutineBody(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		if (isSymbol("{")) {
			writeTag(writer);
			
			compileZeroOrMoreVarDeclarations(tokenizedFile, writer);
			
			if (isSymbol("}")) {
				writeTag(writer);
			} else {
				// error
			}
		} else {
			// error 
		}
	}

	private void compileZeroOrMoreVarDeclarations(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		while (isKeyword("var", tokenizedFile.peek())) {
			
			// compile var
			advance(tokenizedFile);
			writeTag(writer);
			
			// compile type
			advance(tokenizedFile);
			compileType(writer);
			
			// compile varName identifier
			advance(tokenizedFile);
			compileVarName(writer);
			
			
		}
		
	}

	private void compileParameterList(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		if (isSymbol("(")) {
			writeTag(writer);
			
			compileVarName(writer);
			
			// here I need to compile the type as well.

			while (isSymbol(",", tokenizedFile.peek())) {
				
				// compile ","
				advance(tokenizedFile);
				writeTag(writer);
				
				// compile type
				compileType(writer);
				advance(tokenizedFile);
			
				// compile varName
				advance(tokenizedFile);
				compileVarName(writer);
			}
			
			advance(tokenizedFile);
			
			if (isSymbol(")")) {
				writeTag(writer);
			} else {
				// error
			}
		} else {
			// error
		}
	}

	/**
	 * Compile further variable declaration, before this we do not advance because
	 * this is an optional list The advance is done inside this method after we peek
	 * the next token
	 * 
	 * @param tokenizedFile
	 * @param writer
	 * @throws Exception
	 * @throws IOException
	 */
	private void compileFurtherVariableDeclaration(TokenizedFile tokenizedFile, BufferedWriter writer)
			throws Exception, IOException {
		while (isSymbol(",", tokenizedFile.peek())) {
			advance(tokenizedFile);
			writeTag(writer);
			advance(tokenizedFile);
			compileVarName(writer);
		}
	}

	private void compileVarName(BufferedWriter writer) throws IOException {
		if (isIdentifier()) {
			writeTag(writer);
		} else {
			// error here!
		}
	}

	private void compileType(BufferedWriter writer) throws IOException {
		if (isType()) {
			writeTag(writer);
		} else {
			// error here!
		}
	}

	private boolean isType() {
		return isKeyword("int") || isKeyword("char") || isKeyword("boolean") || isIdentifier();
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
	
	private String currentType(Token token) {
		return token.getType().toString().toLowerCase();
	}

	
	private boolean isKeyword(String content, Token token) {
		return token.isKeyword() && token.getContent().equals(content);
	}
	
	private boolean isSymbol(String content, Token token) {
		return token.isSymbol() && token.getContent().equals(content);
	}
	
	private boolean isIdentifier(Token token) {
		return token.isIdentifier();
	}
	
	private String createXmlFile(TokenizedFile tokenizedFile) {
		Path filePath = tokenizedFile.getFilePath();
		Path fileParent = filePath.getParent();
		String fileName = filePath.getFileName().toString().split("\\.")[0];
		String compiledFile = fileParent.toString() + File.separator +  fileName + "Tokenized.xml";
		return compiledFile;
	}


	
}
