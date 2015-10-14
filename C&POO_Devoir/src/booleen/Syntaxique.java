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
    	boolean continuer = true;
    	
    	do {
    		continuer = true;
    		size = MapFait.size();
			precharge = lexical.suivant();
			//System.out.println("condition1: " + precharge.representation);
			
    		while (continuer) {
    			// Si Regle déclenchable
    			if (estRegleDeclenchable()) {
				
					if (precharge.estFinCondition()) {
						precharge = lexical.suivant();
						if (precharge.estOpperandeNON()) {
							precharge = lexical.suivant();
							ajoutDansLaBase(precharge, false);
						} else {
							ajoutDansLaBase(precharge, true);
						}
					}
				}
    			// Verification fin de liste
    			if (lexical.getPositionListe() == lexical.getListe().size()-1) {
					continuer = false;
				}else{
					precharge = lexical.RegleSuivante();
				}	
    		}
    		lexical.ResetListe();
    	}
    	while (size != MapFait.size());
    	
    	System.out.println(MapFait.toString());
    	
    	return true;
    }
    
    protected boolean estRegleDeclenchable() throws IOException {
    	
    	if (precharge.estCondition()) {
    		precharge = lexical.suivant();
			//System.out.println("1 er fait : " + precharge.representation);
    	}
    	    	
    	if (!estFait(MapFait)) {
    		//System.out.println("=======>1 : " + precharge.representation);
    		return false;
    	}
    	
    	while (estOpperande()){
	    	if (!estFait(MapFait)) {
	    		//System.out.println("=======>2 : " + precharge.representation);
	    		return false;
	    	}
    	}
    	return true;
    }
    
    protected boolean estOpperande() throws IOException {
    	
    	if (precharge.estOpperandeET() || precharge.estOpperandeOU()) {
    		precharge = lexical.suivant();
			//System.out.println("2 nd fait : " + precharge.representation);
    		return true;
    	}
    	return false;
    }
    
    protected boolean estFait(HashMap<String, Boolean> MapFait) throws IOException {
    	
    	if (precharge.estDansLaBase(MapFait) && MapFait.get(precharge.representation)) {
    		precharge = lexical.suivant();
			//System.out.println("oppérande : " + precharge.representation);
    		return true;
    	}
    	return false;
    }

	public void ajoutDansLaBase(Jeton fait, boolean etat) {
		MapFait.put(fait.representation, etat);
		System.out.println("Ajout de '" + fait.representation + "' dans la base à l'état " + etat);
		lexical.SupprimerRegleDeduit();
	}

	public void FichierDeduction(FileOutputStream out) throws IOException {
		
		String deduc = "Fait booleen : " + MapFait.toString();
		
		for (int i = 0; i < deduc.length(); i++) {
			out.write(deduc.charAt(i));
		}
	}
}
