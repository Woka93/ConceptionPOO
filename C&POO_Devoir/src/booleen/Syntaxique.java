package booleen;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Syntaxique {

	protected Lexical lexical;
	protected Jeton precharge;
	private HashMap<String,Boolean> MapFait = new HashMap<String,Boolean>();
	
	public Syntaxique(Lexical lexical, BufferedReader fichier_connaissances) throws IOException {
    	this.lexical = lexical;
    	initialiserBasedeFait(fichier_connaissances);
    }
	
	public void initialiserBasedeFait(BufferedReader fichier_connaissances) throws IOException {
		String ligne;
		while ((ligne = fichier_connaissances.readLine()) != null) {
			String[] token = ligne.split(" ");
			MapFait.put(token[0],Boolean.valueOf(token[2]));
		}
	}

    public boolean verifier() throws IOException {

    	int size = 0;
    	boolean derniereRegleVrai = false;
    	
    	precharge = lexical.suivant();
    	
		do{
			size = MapFait.size();
			
			if (lexical.getListe().size() == 0) {
				return true;
			}
			
			while(lexical.getPositionListe() < lexical.getListe().size()){
				
				if (derniereRegleVrai) {
					precharge = lexical.suivant();
					derniereRegleVrai = false;
				}
				
	    		if (estRegleDeclenchable()){
					ajoutDansLaBase(precharge, true);
					if(!lexical.DerniereRegle()) {
						precharge = lexical.suivant();
					} else {
						derniereRegleVrai = true;
						lexical.UpPositionListe();
					}
		    	}else{
		    		if(!lexical.DerniereRegle()){
		    			precharge = lexical.RegleSuivante();
		    		} else {
						lexical.UpPositionListe();
					}
		    	}
    		}
	    	lexical.ResetListe();
		} while (size < MapFait.size());
		
		return true;
    }
    
    protected boolean estRegleDeclenchable() throws IOException {
    	
    	if(precharge.estOpperandeET()) { 
    		precharge = lexical.suivant();
    		System.out.println(precharge.representation);
    	}
    	
    	if(!EstET()){ return false; }
    	
    	while(precharge.estOpperandeET()) {
    		precharge = lexical.suivant();
    		if(!EstET()){ return false; }
    	}
    	return true;
    }
    
    public boolean EstET() throws IOException{
    	
    	if(!EstOU()){ return false; }
    	
    	while(precharge.estOpperandeET()) {
			precharge = lexical.suivant();
			if(!EstOU()) {return false;}
		}
    	return true;
    }
    
    private boolean EstOU() throws IOException {
		
    	if(!EstFait()){ return false; }
    	
    	while(precharge.estOpperandeOU()) {
			precharge = lexical.suivant();
			if(!EstFait()) {return false;}
		}
		return true;
	}
    
    protected boolean EstFait() throws IOException {

    	if (precharge.estDansLaBase(MapFait) /*&& MapFait.get(precharge.representation)*/) {
    		precharge = lexical.suivant();
    		return true;
    	}
    	if (!precharge.estCondition()) {
    		return false;
    	}
    	precharge = lexical.suivant();
		if (!estRegleDeclenchable() || !precharge.estFinCondition()) {
			return false;
		}
    	precharge = lexical.suivant();

    	return true;
    }

	public void ajoutDansLaBase(Jeton fait, boolean etat) {
		MapFait.put(fait.representation, etat);
		System.out.println("Ajout de '" + fait.representation + "' dans la base à l'état " + etat);
		lexical.SupprimerRegleDeduit();
	}

	public void FichierDeduction(FileOutputStream out) throws IOException {
		
		String deduc = "Fait booleen : " + MapFait.toString();
		System.out.println(MapFait.toString());
		
		for (int i = 0; i < deduc.length(); i++) {
			out.write(deduc.charAt(i));
		}
	}
}
