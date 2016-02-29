package program4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class TopDownParser {

	private static LexicalAnalyzer lex = new LexicalAnalyzer();
	private static LinkedList<Token> tokens;
	
	public static void main(String[] args) throws IOException {	
		
		// Read in from file put in collection tokens
		File f = new File("/Users/robertseedorf/Desktop/sample.java");
		FileInputStream fis = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		tokens = new LinkedList<Token>();
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			tokens.addAll(lex.analyze(line));
		}
		br.close();
		
		/*
		 * Throw out comments first 
		 */
		for(int i= 0; i < tokens.size(); i++) {
			if(tokens.get(i).type == Type.COMMENT || tokens.get(i).type == Type.NULL_STRING) {
				tokens.remove(i);
			}
		}
		
		Program();
    }
	
	public static boolean eat(boolean bool) {
		System.out.println(bool);
		return bool;
	}
	
	public static void Program() {
		classDecl();
	}
	
	private static void classDecl() {
		eat(tokens.pop().c.equals("public"));
		eat(tokens.pop().c.equals("class"));
		Token className = tokens.pop();      //figure out what to do with the class name next phase
		eat(tokens.pop().type == Type.LCURL);
		mainMethod();
		body();
		while(tokens.peek().type != Type.RCURL) {
			methDecl();
		}
	}
	
	private static void methDecl() {
		eat(tokens.pop().c.equals("public"));
		eat(tokens.pop().c.equals("static"));
		Token retType = tokens.pop();
		Token methName = tokens.pop();
		eat(tokens.pop().type == Type.LPAREN);
		if(tokens.peek().type != Type.RPAREN) {
			param();
		}
		eat(tokens.pop().type == Type.RPAREN);
		body();
	}

	public static void mainMethod() {
		eat(tokens.pop().c.equals("public"));
		eat(tokens.pop().c.equals("static"));
		eat(tokens.pop().c.equals("void"));
		eat(tokens.pop().c.equals("main"));
		eat(tokens.pop().type == Type.LPAREN);
		eat(tokens.pop().c.equals("String"));
		eat(tokens.pop().type == Type.LSQUARE);
		eat(tokens.pop().type == Type.RSQUARE);
		Token args = tokens.pop();
		eat(tokens.pop().type == Type.RPAREN);
		body();
	}
	
	private static void body() {
		eat(tokens.pop().type == Type.LCURL);
		while(tokens.peek().type != Type.RCURL) {	
			statement();
		}
		eat(tokens.pop().type == Type.RCURL);
	}

	private static void statement() {
		Token determinant = tokens.peek();
		if (determinant.c.equals("while")) {
			whileLoop();
		}
		else if (determinant.c.equals("for")) {
			forLoop();
		}
		else if (determinant.c.equals("if")) {
			ifStmt();
		}
		else if (determinant.type == Type.ATOM) {
			if(tokens.peek().type == Type.OPERATOR || tokens.peek().type == Type.ASSIGN) {
				assignment();
			}
			methodCall();
		}
		else;		//no more options left
		eat(tokens.pop().type == Type.SEMICOLON);
	}

	private static void param() {
		Token param = tokens.pop();				//do something with param in the next phase.
		if(tokens.peek().type == Type.PERIOD) {
			methodCall(param);
		}
		if(tokens.peek().type == Type.COMMA) {
			eat(tokens.pop().type == Type.COMMA);
			param();
		}
		else;			//no more params left
	}
	
	private static void assignment() {
		System.out.println(tokens.peek());
		Token varName = tokens.pop();			//do something with varName operation and value in the next phase.
		Token operation = tokens.peek();
		if(operation.c.equals("=")) {
			eat(tokens.pop().type == Type.ASSIGN);
		}
		else {
			eat(tokens.pop().type == Type.ALT_OP);
		}
		Token value;
		System.out.println(tokens.peek());
		if(tokens.peek().type == Type.DQUOTE || tokens.peek().type == Type.SQUOTE) {
			eat(tokens.pop().type == Type.DQUOTE);
			value = tokens.pop();
			eat(tokens.pop().type == Type.DQUOTE);
		}
		else {
			value = tokens.pop();
		}
		eat(tokens.pop().type == Type.SEMICOLON);
	}
	
	private static void methodCall() {
		if(tokens.peek().type == Type.PERIOD) {
			eat(tokens.pop().c.equals("."));
		}
		else {
			Token owner = tokens.pop();
			eat(tokens.pop().type == Type.PERIOD);
			
		}
		Token methName = tokens.pop();
		eat(tokens.pop().type == Type.LPAREN);
		param();
		eat(tokens.pop().type == Type.RPAREN);
	}
	
	private static void methodCall(Token tok) {
		Token owner = tok;							//see above
		eat(tokens.pop().type == Type.PERIOD);
		Token methName = tokens.pop();
		eat(tokens.pop().type == Type.LPAREN);
		param();
		eat(tokens.pop().type == Type.RPAREN);
	}
	
	private static void whileLoop() {
		// TODO Auto-generated method stub
		
	}
	
	private static void forLoop() {
		// TODO Auto-generated method stub
		
	}
	
	private static void ifStmt() {
		// TODO Auto-generated method stub
		
	}
	
	
}
