package booleen;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class Lexical {
	
	protected LineNumberReader lecteur;
	protected String ligne;
	protected int PositionListe = -1;
	protected int position;
	
	
	public int getPosition() {
		return position;
	}
	
	public int getPositionListe() {
		return PositionListe;
	}

	public ArrayList<String> getListe() {
		return liste;
	}

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
	
	public void ResetListe(){
		PositionListe = -1;
	}
	
	public String lireLigne() {
    	return ligne = liste.get(PositionListe);
    }

    public int lirePosition() {
    	return position;
    }
    
public Jeton suivant() throws IOException {
		
		if(!avancer()){
			//System.out.println("coucou");
			return FabriqueJeton.finFichier();
		}
		//System.out.println(position);
		// Caractere correspondant a la position courante.
		char caractere = ligne.charAt(position); 
		//System.out.println(caractere);
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
	
		default:
		    return extraireToken();
		}

	}
	
	public boolean avancer() throws IOException {
			
		while (true){
			while (position < ligne.length() && Character.isWhitespace(ligne.charAt(position))) {
				position ++;
			}
			
		    if (position == ligne.length()) {
		    
				//System.out.println(PositionListe + " " + liste.size());

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
	
	public boolean DerniereRegle(){
		
		//System.out.println("PostionListe : " + PositionListe + " ListeSize : " + liste.size());
		
		if(PositionListe == liste.size() - 1){
			return true;
		}
		
		return false;
	}
	
	public Jeton RegleSuivante() throws IOException{
		Jeton precharge = suivant();
		while (precharge.representation != "?") {
			precharge = suivant();
			//System.out.println("test: " + precharge.representation);
		}
		return precharge;
	}

	public void SupprimerRegleDeduit(){
		liste.remove(PositionListe);
		PositionListe--;
		//System.out.println(liste.toString());
	}

	public void setPositionListe(int positionListe) {
		PositionListe = positionListe;
	}
}
