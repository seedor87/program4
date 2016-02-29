package program4;

/**
 * This code has been adapted from a source on the Internet to be described, Found at:
 * http://stackoverflow.com/questions/17848207/making-a-lexical-analyzer
 * 
 * This code has been re-fashioned from a generous open source publication, we have learned it its intricacies, 
 * and we have implemented it for our submission for this project
 * 
 * This class represents the instances of tokens being generated after analyzation in this compilation process
 * 
 * @author Robert Seedorf, Bill Clark
 *
 */
public class Token {
	
	public final Type type;		// category of token, useful in conversion to executable form
    public final String c; 		// literal value of token
    
    public Token(Type t, String c) {
        this.type = t;
        this.c = c;
    }
    
    public String toString() {
        if(type == Type.ATOM) {
            return "ATOM<" + c + ">";
        }
        if(type == Type.COMMENT) {
        	return "COMMENT<" + c + ">";
        }
        if(type == Type.OPERATOR) {
        	return "OPERATOR<" + c + ">";
        }
        if(type == Type.ALT_OP) {
        	return "ALT_OPERATOR<" + c + ">";
        }
        return type.toString();
    }
}
	        
