package program4;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This code has been adapted from a source on the Internet to be described, Found at:
 * http://stackoverflow.com/questions/17848207/making-a-lexical-analyzer
 * 
 * This code has been re-fashioned from a generous open source publication, we have learned it its intricacies, 
 * and we have implemented it for our submission for this project
 * 
 * This class is the analyzation tool that programmatically parses, separates, tokenizes and returns in String format Tokens of the line of text being analyzed
 * 
 * @author Robert Seedorf, Bill Clark
 *
 */
public class LexicalAnalyzer {
	
	private static final String WSPACE = " ";
	private static final String LPAREN = "(";
	private static final String RPAREN = ")";
	private static final String ALT_OP = "?";
	private static final String LCURL = "{";
	private static final String RCURL = "}";
	private static final String LSQUARE = "[";
	private static final String RSQUARE = "]";
	private static final String EQUAL = "=";
	private static final String PLUS = "+";
	private static final String MINUS = "-";
	private static final String MULT = "*";
	private static final String DIV = "/";
	private static final String MOD = "%";
	private static final String GTHAN = ">";
	private static final String LTHAN = "<";
	private static final String HAT = "^";
	private static final String BANG = "!";
	private static final String AND = "&";
	private static final String PIPE = "|";
	private static final String SQUOTE = "'";
	private static final String DQUOTE = "\"";
	private static final String HASH = "#";
	private static final String FSLASH = "\\";
	private static final String PERIOD = ".";
	private static final String COMMA = ",";
	private static final String SEMICOLON = ";";
	private static final String COLON = ":";
	private static final char USCORE = '_';
	private static final char TILDE = '~';
	private static final char APOST = '`';
	private static final char AT = '@';
	
	//the toggle for varying implementations of existing keywords
	private ArrayList<String> reserveSymbols = new ArrayList<String>(); 	//the collection of reserved key symbols, in other words they cannot be used as identifiers
	
	public LexicalAnalyzer() {
		reserveSymbols = new ArrayList<String>(Arrays.asList(LPAREN, RPAREN, LCURL, RCURL, LSQUARE, RSQUARE, PLUS, MINUS, 
				MULT, DIV, MOD, GTHAN, LTHAN, HAT, BANG, AND, PIPE, SQUOTE, DQUOTE, FSLASH, SEMICOLON, COLON, PERIOD));
	}

   /**
    * returns String of token value for printing. NOTE words with '.' or '_' are acceptable and treated as complete atoms
    * @param s
    * @param i
    * @return
    */
    public static String getTokenString(String s, int i) {
        int j = i;
        while(j < s.length()) {
            if(Character.isLetter(s.charAt(j)) || Character.isDigit(s.charAt(j)) || s.charAt(j) == USCORE || s.charAt(j) == TILDE || s.charAt(j) == APOST || s.charAt(j) == AT) {
                j++;
            }
            else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }
    /**
     * This method takes a string, that is a substring of the whole test input and analyzes it to be categorized as some particular token
     * @param input - substring of line to be read
     * @return - token representing categorization of substring
     */
    public static Token getToken(String input) {
    	if(input.length() > 0 && input.charAt(0) == '?') {
            return new Token(Type.ALT_OP, input.substring(1)); 
    	}
    	for(int i = 0; i < input.length(); ) {
            switch(input.charAt(i)) {
            case '(':
                return new Token(Type.LPAREN, LPAREN);
            case ')':
                return new Token(Type.RPAREN, RPAREN);
            case '{':
            	return new Token(Type.LCURL, LCURL);
            case '}':
            	return new Token(Type.RCURL, RCURL);
            case '[':
            	return new Token(Type.LSQUARE, LSQUARE);
            case ']':
            	return new Token(Type.RSQUARE, RSQUARE);
            case '=':
            	return new Token(Type.ASSIGN, EQUAL);
            case '+':
            	return new Token(Type.OPERATOR, PLUS);
            case '-':
            	return new Token(Type.OPERATOR, MINUS);
            case '*':
            	return new Token(Type.OPERATOR, MULT);
            case '/':
            	return new Token(Type.OPERATOR, DIV);
            case '%':
            	return new Token(Type.OPERATOR, MOD);
            case '>':
            	return new Token(Type.OPERATOR, GTHAN);
            case '<':
            	return new Token(Type.OPERATOR, LTHAN);
            case '^':
            	return new Token(Type.OPERATOR, HAT);
            case '!':
            	return new Token(Type.OPERATOR, BANG);
            case '&':
            	return new Token(Type.OPERATOR, AND);
            case '|':
            	return new Token(Type.OPERATOR, PIPE);
            case '\\':
            	return new Token(Type.FSLASH, FSLASH);
            case '\'':
            	return new Token(Type.SQUOTE, SQUOTE);
            case '"':
            	return new Token(Type.DQUOTE, DQUOTE);
            case ';':
            	return new Token(Type.SEMICOLON, SEMICOLON);
            case ':':
            	return new Token(Type.COLON, COLON);
            case '.':
            	return new Token(Type.PERIOD, PERIOD);
            case ',':
            	return new Token(Type.COMMA, COMMA);
            default:
                if(!Character.isWhitespace(input.charAt(i))) {
                	String atom = getTokenString(input, i);
                    i += atom.length();
                    return new Token(Type.ATOM, atom);
                }
                break;
            }
    	}
        return new Token(Type.NULL_STRING, "");
    }

    /**
     * This method processes the total input String to clarify elements whose tokenization will be precluded by illegal characters, keywords, etc. 
     * @param s - Total string to be pre-processed
     * @return String with necessary pre-processing enacted upon it.
     */
    public String preprocess(String s) {
    	for(String str: reserveSymbols) {
    		s = s.replace(str, WSPACE + str + WSPACE);
    	}
    	s = s.replace(ALT_OP + WSPACE, ALT_OP);	//replace an instance of alt-operator ('?') with itself ignoring trailing white space
    	s = s.replaceAll("\\s+", " ");			// replace any number of white spaces with a single white sapce
    	s = s.trim();							// trim excess whitespace off the ends to eliminate false null tokens
    	return s;
    }
    
    /**
     * this method programmatically parses the entire input string and tokenizes each element with a call to getToken().
     * 
     * Note this method recognizes comments only line by line, this can cause some confusion on batch jobs.
     * 
     * @param input - total input String
     * @return - List of tokens of elements of input
     */
    public List<Token> analyze(String input) {
    	List<Token> result = new ArrayList<Token>();
    	if(input.length() > 0 && input.charAt(0) == '#') {
    		result.add(new Token(Type.COMMENT, input.substring(1)));
    		return result;
    	}
    	String[] withComment  = input.split(HASH, 2);
    	input = preprocess(withComment[0]);
    	if(withComment.length < 2) {
    		String[] split = input.split(WSPACE);
            for(String s: split) {
            	result.add(getToken(s));
            }
            return result;
    	}
    	String commentString = withComment[1];
    	String[] split = input.split(WSPACE);
        for(String s: split) {
        	result.add(getToken(s));
        }
        result.add(new Token(Type.COMMENT, commentString));
        return result;	
    }
}