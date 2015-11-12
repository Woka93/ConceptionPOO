package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class DemoTest {
	
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
        
        /*Parseur parseur = new Parseur(lexical, basefait);

        try {
            if (parseur.verifier()) {
                System.out.println("Déduction terminée");
                //parseur.FichierDeduction(out);
                //return;
            }
        }
        catch(IOException e) {
            System.err.println("Impossible de lire [" + args[0] + "]");
            //return;
        }
        */

        Syntaxique syntax = new Syntaxique(lexical, basefait);
        syntax.precharge = syntax.lexical.suivant();
        syntax.test();
        syntax.test();
        syntax.test();
        
        baseregle.close();
        basefait.close();
        out.close();
    }

}
