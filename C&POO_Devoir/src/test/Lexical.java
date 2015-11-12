package test;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class Lexical {
	
	protected LineNumberReader lecteur;
	protected String ligne;
	protected int PositionListe = -1;
	protected int position;

	protected ArrayList<String> liste = new ArrayList<String>();
	
	public Lexical (LineNumberReader lecteur) {
		this.lecteur = lecteur;
		ligne = "";
		position = 0;
	}
	
	public void InitFichierRegle() throws IOException{
		String ligne;
		while ((ligne=lecteur.readLine())!=null){
			liste.add(ligne);
		}
	}
	
	private String lireLigne() {
    	return ligne = liste.get(PositionListe);
    }
    
    public Jeton suivant() throws IOException {
    	
		if(!avancer()){
			return FabriqueJeton.finFichier();
		}
		char caractere = ligne.charAt(position); 
		
		switch(caractere) {   
	 
		case '?':
		    position ++;
		    return FabriqueJeton.condition();
	
		case '&':
		    position ++;
		    return FabriqueJeton.opperandeET();
	
		case '|':
		    position ++;
		    return FabriqueJeton.opperandeOU();
		    
		case '!':
		    position ++;
		    return FabriqueJeton.opperandeNON();
	
		case '=':
		    position ++;
		    return FabriqueJeton.comparaisonEgale();
	
		case '<':
		    position ++;
		    return FabriqueJeton.comparaisonInferieure();
	
		case '>':
		    position ++;
		    return FabriqueJeton.comparaisonSuperieure();
	
		case ':':
		    position ++;
		    return FabriqueJeton.finCondition();
	
		default:
		    return extraireToken();
		}
	}
	
    private boolean avancer() throws IOException {
			
		while (true){
			while (position < ligne.length() && Character.isWhitespace(ligne.charAt(position))) {
				position ++;
			}
			
		    if (position == ligne.length()) {

				if (PositionListe >= liste.size()) {
				    return false;
				}

				PositionListe++;
				ligne = lireLigne();
				position = 0;
		    }
		    else {
		    	return true;
		    }
		}
	}
	
    private Jeton extraireToken() {
		
		int fin = position + 1;
	
		while (fin < ligne.length() && estIdentificateur(ligne.charAt(fin))) {
		    fin ++;
		}
	
		int debut = position;
	
		position = fin;
		
		return FabriqueJeton.fait(ligne.substring(debut, fin));
	}
	
    private boolean estIdentificateur(char caractere) {
		
		if (Character.isLetterOrDigit(caractere)) {
			return true;
		}
		if (caractere == '_') {
			return true;
		}
		return false;
	}
}
