package booleen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class DemoFait {

	public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Usage: java DemoLexical nomdefichier");
            return;
        }
        if (args.length != 3) {
            System.err.println("Nombre d'arguments incorrect.");
            return;
        }

        LineNumberReader baseregle = null;
        try {
        	baseregle = new LineNumberReader(new FileReader(args[0]));
        }
        catch(FileNotFoundException e) {
            System.err.println("Le fichier [" + args[0] + "] n'existe pas.");
            return;
        }
        
        BufferedReader basefait = null;
        try {
        	basefait = new LineNumberReader(new FileReader(args[1]));
        }
        catch(FileNotFoundException e) {
            System.err.println("Le fichier [" + args[1] + "] n'existe pas.");
            baseregle.close();
            return;
        }
        
		FileOutputStream out = new FileOutputStream(args[2]);

        
        Lexical lexical = new Lexical(baseregle);
        
        lexical.InitFichierRegle();
        
        Syntaxique syntaxique = new Syntaxique(lexical, basefait);

        try {
            if (syntaxique.verifier()) {
                System.out.println("Déduction terminée");
                syntaxique.FichierDeduction(out);
                return;
            }
        }
        catch(IOException e) {
            System.err.println("Impossible de lire [" + args[0] + "]");
            return;
        }
        baseregle.close();
        basefait.close();
        out.close();
    }
}
