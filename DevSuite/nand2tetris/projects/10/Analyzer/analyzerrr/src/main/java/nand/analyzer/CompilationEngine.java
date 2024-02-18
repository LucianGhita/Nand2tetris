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
	
	private int spaceNo = 0;
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
					
					appendSpace();
					
					writeAndAdvance(tokenizedFile, writer);
					
					if (isIdentifier()) {
						writeAndAdvance(tokenizedFile, writer);
						if (isSymbol("{")) {
							writeTag(writer);
					
							
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
			System.out.println("finished compilation");
		}
	}

	private void appendSpace() {
		spaceNo += 2;
	}
	
	private void compileClassVarDec(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		writeGrammarTag("<classVarDec>", writer);
		appendSpace();
		Token nextToken = tokenizedFile.peek();
		while (isKeyword("static", nextToken) || isKeyword("field", nextToken)) {
			
			advance(tokenizedFile);
			writeAndAdvance(tokenizedFile, writer);
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
			nextToken = tokenizedFile.peek();
		}
		removeSpace();
		writeGrammarTag("</classVarDec>", writer);
	}

	private void removeSpace() {
		spaceNo -= 2;
	}
	
	private void writeGrammarTag(String tagName, BufferedWriter writer) throws IOException {
		appendSpaces(writer, spaceNo);
		writer.append(tagName);
		writer.newLine();
	}
	
	private void compileSubroutineDec(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		Token nextToken = tokenizedFile.peek();
		while (isKeyword("constructor", nextToken) || isKeyword("function", nextToken) || isKeyword("method", nextToken)) {
			writeGrammarTag("<subroutineDec>", writer);
			appendSpace();
			advance(tokenizedFile);
			
			writeAndAdvance(tokenizedFile, writer);
			
			if (isKeyword("void") || isType()) {
				writeAndAdvance(tokenizedFile, writer);
				// compile subroutine name
				if (isIdentifier()) {
					writeAndAdvance(tokenizedFile, writer);
					
					if (isSymbol("(")) {
						writeTag(writer);
						
						compileParameterList(tokenizedFile, writer);
						
						if (isSymbol(")")) {
							writeTag(writer);
						} else {
							// error
						}
					} else {
						// error
					}
					
					advance(tokenizedFile);
					compileSubroutineBody(tokenizedFile, writer);
					
					
				} else {
					// error
				}
			} else {
				// error
			}
			writeGrammarTag("</subroutineDec>", writer);
			nextToken = tokenizedFile.peek();
		}
	}

	private void compileStatements(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		Token peekToken = tokenizedFile.peek();
		while (isKeyword("let", peekToken) 
				|| isKeyword("if", peekToken) 
				|| isKeyword("while", peekToken) 
				|| isKeyword("do", peekToken) 
				|| isKeyword("return", peekToken)) {
			
			advance(tokenizedFile);
			if (isKeyword("let")) {
				compileLetStatement(tokenizedFile, writer);
			} else if (isKeyword("if")) {
				compileIfStatement(tokenizedFile, writer);
				
			} else if (isKeyword("while")) {
				compileWhileStatement(tokenizedFile, writer);
			} else if (isKeyword("do")) {
				compileDoStatement(tokenizedFile, writer);
			} else if (isKeyword("return")) {
				compileReturnStatement(tokenizedFile, writer);
			}
			peekToken = tokenizedFile.peek();
		}
	}

	private void compileReturnStatement(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		writeGrammarTag("<returnStatement>", writer);
		appendSpace();
		if (isKeyword("return")) {
			writeTag(writer);
		} else {
			// error
		}
		advance(tokenizedFile);
		if (isSymbol(";")) {
			writeTag(writer);
		} else {
			// error
		}
		removeSpace();
		writeGrammarTag("</returnStatement>", writer);
	}

	private void compileDoStatement(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		writeGrammarTag("<doStatement>", writer);
		appendSpace();
		
		if (isKeyword("do")) {
			writeAndAdvance(tokenizedFile, writer);

			if (isIdentifier()) {
				writeAndAdvance(tokenizedFile, writer);
				if (isSymbol(".")) {
					writeAndAdvance(tokenizedFile, writer);
					if (isIdentifier()) {
						writeAndAdvance(tokenizedFile, writer);
						if (isSymbol("(")) {
							writeAndAdvance(tokenizedFile, writer);
							if (isSymbol(")")) {
								writeAndAdvance(tokenizedFile, writer);
							} else {
								// error
							}
						
						} else {
//							error
						}
					} else {
//						error
					}
				} else {
//				error
				}
			}
		} else {
			// error
		}
		if (isSymbol(";")) {
			writeTag(writer);
		} else {
			// error
		}
		removeSpace();
		writeGrammarTag("</doStatement>", writer);
	}

	private void writeAndAdvance(TokenizedFile tokenizedFile, BufferedWriter writer) throws IOException, Exception {
		writeTag(writer);
		advance(tokenizedFile);
	}

	private void compileWhileStatement(TokenizedFile tokenizedFile, BufferedWriter writer) throws IOException {
		writeGrammarTag("<whileStatement", writer);
		appendSpace();
		
		
		removeSpace();
		writeGrammarTag("</whileStatement>", writer);
	}

	private void compileIfStatement(TokenizedFile tokenizedFile, BufferedWriter writer) throws IOException {
		writeGrammarTag("<ifStatement>", writer);
		appendSpace();
		
		removeSpace();
		writeGrammarTag("</ifStatement>", writer);
	}

	private void compileLetStatement(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		writeGrammarTag("<letStatement>", writer);
		appendSpace();
		writeAndAdvance(tokenizedFile, writer);
		if (isIdentifier()) {
			writeAndAdvance(tokenizedFile, writer);
			//TODO continue expression handling here
			if(isSymbol("=")) {
				writeAndAdvance(tokenizedFile, writer);
				 if (isIdentifier()) {
					 writeAndAdvance(tokenizedFile, writer);
				 } else {
					 // error
				 }
			} else {
				// error
			}
		} else {
			// error
		}
		if (isSymbol(";")) {
			writeTag(writer);
		} else {
			// error
		}
		writeGrammarTag("</letStatement>", writer);
		removeSpace();
	}

	private void compileSubroutineBody(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		writeGrammarTag("<subroutineBody>", writer);
		appendSpace();
		if (isSymbol("{")) {
			writeTag(writer);
			
			compileZeroOrMoreVarDeclarations(tokenizedFile, writer);
			
			compileStatements(tokenizedFile, writer);
			
			advance(tokenizedFile);
			if (isSymbol("}")) {
				writeTag(writer);
			} else {
				// error
			}
		} else {
			// error 
		}
		removeSpace();
		writeGrammarTag("</subroutineBody>", writer);
	}

	private void compileZeroOrMoreVarDeclarations(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		while (isKeyword("var", tokenizedFile.peek())) {
			writeGrammarTag("<varDec>", writer);
			appendSpace();
			advance(tokenizedFile);
			writeAndAdvance(tokenizedFile, writer);
			compileVarDec(tokenizedFile, writer);

			while (isSymbol(",", tokenizedFile.peek())) {
				advance(tokenizedFile);
				writeAndAdvance(tokenizedFile, writer);
				compileSubsequentVarDec(tokenizedFile, writer);
			}
			
			if (tokenizedFile.peek().isSymbol() && ";".equals(tokenizedFile.peek().getContent())) {
				advance(tokenizedFile);
				writeTag(writer);
			} else {
				// error
			}
			writeGrammarTag("</varDec>", writer);
			removeSpace();
		} 
		
	}

	private void compileVarDec(TokenizedFile tokenizedFile, BufferedWriter writer) throws IOException, Exception {
		if (isType()) {
			writeAndAdvance(tokenizedFile, writer);
			
			if (isIdentifier()) {
				writeTag(writer);
			} else {
				// error
			}
			
		} else {
			// error
		}
	}
	
	private void compileSubsequentVarDec(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		if (isIdentifier()) {
			writeTag(writer);
		} else {
			// error
		}
	}

	private void compileParameterList(TokenizedFile tokenizedFile, BufferedWriter writer) throws Exception {
		
		writeGrammarTag("<parameterList>", writer);
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
		writeGrammarTag("<parameterList>", writer);
		
		advance(tokenizedFile);
		
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
			writeAndAdvance(tokenizedFile, writer);
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
		appendSpaces(writer, spaceNo);
		writer.append("<" + currentType() + "> ");
		writer.append(currentToken.getContent());
		writer.append(" </" + currentType()  + ">");
		writer.newLine();
	}


	private void appendSpaces(BufferedWriter writer, int tabNo) throws IOException {
		for (int i = 0; i < tabNo; i++) {
			writer.append(" ");
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
