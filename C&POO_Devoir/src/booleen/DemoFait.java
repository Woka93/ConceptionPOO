package booleen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class DemoFait {

	public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java DemoLexical nomdefichier");
            return;
        }
        if (args.length != 1) {
            System.err.println("Nombre d'arguments incorrect.");
            return;
        }

        LineNumberReader lecteur = null;
        try {
            lecteur = new LineNumberReader(new FileReader(args[0]));
        }
        catch(FileNotFoundException e) {
            System.err.println("Le fichier [" + args[0] + "] n'existe pas.");
            return;
        }

        Lexical lexical = new Lexical(lecteur);

        Syntaxique syntaxique = new Syntaxique(lexical);

        try {
            if (syntaxique.verifier()) {
                System.out.println("D�duction termin�e");
                return;
            }
        }
        catch(IOException e) {
            System.err.println("Impossible de lire [" + args[0] + "]");
            return;
        }

    }
}
