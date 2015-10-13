package booleen;

import java.io.IOException;
import java.util.HashMap;

public class Syntaxique {

	protected Lexical lexical;
	protected Jeton precharge;
	private HashMap<String,Boolean> MapFait = new HashMap<String,Boolean>();
	
	public Syntaxique(Lexical lexical) {
    	this.lexical = lexical;
    	MapFait.put("a", true);
    }

    public boolean verifier() throws IOException {

    	int size = 0;
    	
    	do {
    		size = MapFait.size();
			precharge = lexical.suivant();
			//System.out.println(precharge.representation);
			
    		while (lexical.getListe().size() != lexical.getPositionListe()) {
			
    			if (estRegleDeclenchable()) {
				
					if (precharge.estFinCondition()) {
						precharge = lexical.suivant();
						ajoutDansLaBase(precharge);
					}
					
					//precharge = lexical.suivant();
	    			//System.out.println(precharge.representation);
					
    			}else{
    				lexical.RegleSuivante();
    			}	
    		}
    		lexical.ResetListe();
    	}
    	while (size != MapFait.size());
    	
    	//System.out.println(MapFait.toString());
    	
    	return true;
    }
    
    protected boolean estRegleDeclenchable() throws IOException {
    	
    	if (precharge.estCondition()) {
    		precharge = lexical.suivant();
			System.out.println(precharge.representation);
    	}
    	
    	if (!estFait(MapFait)) {
    		//System.out.println("========>1" + precharge.representation);
    		return false;
    	}
    	
    	while (estOpperande()){
    		//System.out.println("========>2" + precharge.representation);
    	if (!estFait(MapFait)) {
    		return false;
    	}
    	}
    	return true;
    }
    
    protected boolean estOpperande() throws IOException {
    	
    	if (precharge.estOpperandeET() || precharge.estOpperandeOU()) {
    		precharge = lexical.suivant();
			System.out.println(precharge.representation);
    		return true;
    	}
    	return false;
    }
    
    protected boolean estFait(HashMap<String, Boolean> MapFait) throws IOException {
    	
    	if (precharge.estDansLaBase(MapFait) && MapFait.get(precharge.representation)) {
    		precharge = lexical.suivant();
			System.out.println(precharge.representation);
    		return true;
    	}
    	return false;
    }

	public void ajoutDansLaBase(Jeton fait) {
		MapFait.put(fait.representation, true);
		System.out.println("Ajout de '" + fait.representation + "' dans la base");
		lexical.SupprimerRegleDeduit();
	}
}
