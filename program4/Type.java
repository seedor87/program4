package program4;

/**
 * This class is the enumeration of useful reserve symbols to be used in further lexical analysis of the line being processed
 * Included is a key to illustrate the meanings of the keywords in relation to the language
 * 
 * @author Robert Seedorf
 *
 */
public enum Type {
    /*
     * Key:
     * 
     * NULL_STRING -	""
     * LPAREN - 		(
     * RPAREN - 		)
     * LCURL -			{
     * RCURL -			}
     * LSQUARE -		[
     * RSQUARE -		]
     * ASSIGN -			=
     * OPERATOR - 		+ - * / % < > ^ & |
     * SQUOTE - 		'
     * DQUOTE - 		"
     * COMMENT -		#
     * SEMICOLON -		;
     * COLON -			:
     * PERIOD -			.
     * COMMA			,
     * ALT_OP - 		?
     * ATOM -			all others (a-z | A-Z | 0-9)
     * 
     */
    NULL_STRING, LPAREN, RPAREN, OPERATOR, ATOM, SQUOTE, DQUOTE, COMMENT, SEMICOLON, LCURL, RCURL, 
    ALT_OP, LSQUARE, RSQUARE, FSLASH, COLON, PERIOD, COMMA, ASSIGN;
}
