package booleen;

import java.io.IOException;
import java.io.LineNumberReader;

public class Lexical {
	
	protected LineNumberReader lecteur;
	protected String ligne;
	protected int position;
	
	public Lexical (LineNumberReader lecteur) {
		this.lecteur = lecteur;
		ligne = "";
		position = 0;
	}
	
	public String lireLigne() {
    	return ligne;
    }

    public int lirePosition() {
    	return position;
    }
	
    public void resetLecteur() {
		try {
			lecteur.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public Jeton suivant() throws IOException {
		
		if (! avancer()) {
		    return FabriqueJeton.finFichier();
		}
	
		// Caractere correspondant a la position courante.
		char caractere = ligne.charAt(position); 
	
		// Il faut identifier le jeton.
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
		    return FabriqueJeton.finCondition();
	
		default: // Chiffre ou bien representation inconnue.
		    if (Character.isAlphabetic(caractere)) {
		    	return extraireToken();
		    }
		    // C'est la representation inconnue.
		    position ++;
		    return FabriqueJeton.inconnu(ligne.substring(position - 1, position));
		}

	}
	
	public boolean avancer() throws IOException {
		while (true) {
			while (position < ligne.length() && Character.isWhitespace(ligne.charAt(position))) {
				position ++;
			}
			
		    if (position == ligne.length()) {
		    	
				ligne = lecteur.readLine();
				
				if (ligne == null) {
				    return false;
				}
				position = 0;
		    }
		    else {
		    	return true;
		    }
		}
	}
	
	public Jeton extraireToken() {
		
		int fin = position + 1;
	
		while (fin < ligne.length() && estIdentificateur(ligne.charAt(fin))) {
		    fin ++;
		}
	
		int debut = position;
	
		position = fin;
		
		return FabriqueJeton.fait(ligne.substring(debut, fin));
	}
	
	public boolean estIdentificateur(char caractere) {
		
		if (Character.isLetterOrDigit(caractere)) {
			return true;
		}
		if (caractere == '_') {
			return true;
		}
		return false;
	}

	public void depart() {
		try {
			lecteur.mark(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
