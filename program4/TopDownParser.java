package program4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * This is our implementation of a top  down parser for our language. 
 * During our work we discovered our language to be LL(2) and we have amended our language considerably to allow us to write this
 * We have fixed our grammar so that it now reflects the cahgnes we have made. it is included with this submission.
 * 
 * @author Robert J Seedorf, Bill Clark
 * @date 3-1-16
 * 
 */
public class TopDownParser {

	private static LexicalAnalyzer lex = new LexicalAnalyzer();
	private static LinkedList<Token> tokens;
	static int tokenLength = 0;
	
	/**
	 * the driver first collects the input from a source, in this case via file input
	 * The input is lexically analyzed, the comments and  whitespace are removed from the collection of generated tokens,
	 * and the Parser is initialized with Program()
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {	
		
		// Read in from file put in collection tokens
		File f = new File("BadSample");
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

		tokenLength = tokens.size();
		Program();
    }
	
//	public static boolean eat(boolean bool) {
//		System.out.println(bool);
//		return bool;
//	}

	public static boolean eat(Token t, Type type)
	{
		if(t.type == type)
		{
			return true;
		}
		else
		{
			System.out.println("ERROR: " + t + " is not of type " + type + " Token Number: " + (tokenLength - tokens.size()-1));
		}
		return false;
	}

	public static boolean eat(Token t, String s)
	{
		if(t.c.equals(s))
		{
			return true;
		}
		else
		{
			System.out.println("ERROR: " + t + " is not equal to " + s + " Token Number: " + (tokenLength - tokens.size()-1));
		}
		return false;
	}
	
	public static void Program()
	{
		printTokens();
		classDecl();
		System.out.println("Token Left: " + tokens.size());
	}
	
	private static void classDecl() {
		eat(tokens.pop(), "public");
		eat(tokens.pop(), "class");
		Token className = tokens.pop();      //figure out what to do with the class name next phase
		eat(tokens.pop(), Type.LCURL);
		mainMethod();
		body();
		while(tokens.peek().type != Type.RCURL) {
			methDecl();
		}
		eat(tokens.pop(), Type.RCURL);
	}
	
	private static void methDecl() {
		eat(tokens.pop(), "public");
		eat(tokens.pop(), "static");
		Token retType = tokens.pop();
		Token methName = tokens.pop();
		eat(tokens.pop(), Type.LPAREN);
		if(tokens.peek().type != Type.RPAREN) {
			param();
		}
		eat(tokens.pop(), Type.RPAREN);
		body();
	}

	public static void mainMethod() {
		eat(tokens.pop(), "public");
		eat(tokens.pop(), "static");
		eat(tokens.pop(), "void");
		eat(tokens.pop(), "main");
		eat(tokens.pop(), Type.LPAREN);
		eat(tokens.pop(), "String");
		eat(tokens.pop(), Type.LSQUARE);
		eat(tokens.pop(), Type.RSQUARE);
		Token args = tokens.pop();
		eat(tokens.pop(), Type.RPAREN);
	}
	
	private static void body() {
		eat(tokens.pop(), Type.LCURL);
		while(tokens.peek().type != Type.RCURL) {	
			statement();
		}
		eat(tokens.pop(), Type.RCURL);
	}

	private static void statement() {
		Token determinant = tokens.pop();
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
			if(tokens.peek().type == Type.ASSIGN) {
				assignment(determinant);
			}
			else if (tokens.peek().type == Type.OPERATOR)
			{
				assignment(determinant); //Needs a new method
			}
			else if (tokens.peek().type == Type.ALT_OP)
			{
				assignment(determinant); //Needs a new method
			}
			else methodCall(determinant);
			eat(tokens.pop(), Type.SEMICOLON);

		}
		else;		//no more options left

	}

	private static void param()
	{
		if(tokens.peek().type != Type.RPAREN)
		{
			Token param = tokens.pop();                //do something with param in the next phase.
			if (tokens.peek().type == Type.PERIOD)
			{
				methodCall(param);
			}
			if (tokens.peek().type == Type.COMMA)
			{
				eat(tokens.pop(), Type.COMMA);
				param();
			}
		}
		else;			//no more params left
	}
	
	private static void assignment(Token varname) {
		Token operation = tokens.peek();
		if(operation.type == Type.ASSIGN)
		{
			eat(tokens.pop(), Type.ASSIGN);
		}
		else if(operation.type == Type.ALT_OP)
		{
			eat(tokens.pop(), Type.ALT_OP);
		}
		else
		{
			eat(tokens.pop(), Type.OPERATOR);
		}
		Token value = tokens.pop();
		if(value.type == Type.DQUOTE || value.type == Type.SQUOTE)
		{
			eat(value, Type.DQUOTE);
			if (tokens.peek().type == Type.DQUOTE) value = new Token(Type.NULL_STRING, "");
			else value = tokens.pop();
			eat(tokens.pop(), Type.DQUOTE);
		}
		else if (tokens.peek().type == Type.PERIOD)
		{
			methodCall(value);
		}
		else if (tokens.peek().type == Type.OPERATOR)
		{
			eat(tokens.pop(), Type.OPERATOR);
			eat(tokens.pop(), Type.ATOM);
		}
		else
		{
			value = value;
		}
	}
	
	private static void methodCall(Token tok) {
		while(tokens.peek().type == Type.PERIOD)
		{
			eat(tokens.pop(), Type.PERIOD);
			eat(tokens.pop(), Type.ATOM);					//see above
		}

		eat(tokens.pop(), Type.LPAREN);
		param();
		eat(tokens.pop(), Type.RPAREN);
	}
	
	private static void whileLoop() {
		eat(tokens.pop(), Type.LPAREN);
		statement();
		eat(tokens.pop(), Type.RPAREN);
		body();
	}
	
	private static void forLoop() {
		eat(tokens.pop(), Type.LPAREN);
		while(tokens.peek().type != Type.RPAREN)
		{
			statement();
		}
		eat(tokens.pop(), Type.RPAREN);
		body();
		
	}
	
	private static void ifStmt() {
		eat(tokens.pop(), Type.LPAREN);
		statement();
		eat(tokens.pop(), Type.RPAREN);
		body();
		while(tokens.peek().c.equals("else")) {
			eat(tokens.pop(), "else");
			if(tokens.peek().c.equals("if"))
			{
				eat(tokens.pop(), "if");
				eat(tokens.pop(), Type.LPAREN);
				statement();
				eat(tokens.pop(), Type.RPAREN);
				body();
			}
			else
			{
				body();
			}

		}
	}

	public static void printTokens()
	{
		for(int x = 0; x < tokens.size();x++)
		{
			System.out.println(x + " " + tokens.get(x));
		}
	}
	
	
}
