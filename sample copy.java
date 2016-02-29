# comment; written by Bob S. and Bill C.
public class Example
{

    public static void main(String[] args)
    {

    	alphabet = "abcdefghijklmnopqrstuvwxyz";
     	alternate = "zabcdefghijklmnopqrstuvwxy";
        in = "helloworld";
        out = "";
        for(int i = 0; i < in.length(); i = i+1) {

            x = alphabet.indexOf(in.charAt(i));
            out ?+ alternate.charAt(x);

        };
        System.out.println(out);
        
    };
};