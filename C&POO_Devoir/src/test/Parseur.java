package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

class Parseur {
	
	protected Lexical lexical;
	protected Jeton precharge;
	private HashMap<String,Fait> MapFait = new HashMap<String,Fait>();
	
	public Parseur (Lexical lexical, BufferedReader fichier_connaissances) {
    	this.lexical = lexical;
    	initialiserBasedeFait(fichier_connaissances);
    }
	
	private void initialiserBasedeFait(BufferedReader fichier_connaissances) {
		String ligne;
		try {
			while ((ligne = fichier_connaissances.readLine()) != null) {
				String[] token = ligne.split(" ");
				ajoutFaitConnuDansLaBase(token);
			}
		} catch (IOException e) {
			System.out.println("Impossible de lire le fichier [" + fichier_connaissances + "]");;
		}
	}
	
	private void ajoutFaitConnuDansLaBase (String [] token) {
		if (token[2].equals("true") || token[2].equals("false")) {
			MapFait.put(token[0], new FaitBooleen(token[0], Boolean.valueOf(token[2])));
		} else if (estEntier(token[2])) {
			MapFait.put(token[0], new FaitEntier(token[0], Integer.parseInt(token[2])));
		} else {
			MapFait.put(token[0], new FaitSymbolique(token[0], token[2]));
		}
	}
	
	private boolean estEntier (String id) {
		
		int cpt = 0;
		
		for (int i = 0; i < id.length(); i++) {
			if (Character.isDigit(id.charAt(i))) {
				cpt++;
			}
		}
		return cpt == id.length();
	}
	
	public boolean verifier() throws IOException {
    	
    	precharge = lexical.suivant();
		
		if (estRegleDeclenchable()) {
			
			//ajoutDansLaBase(precharge, true);
			System.out.println("Règle OK");
		}
		
		return true;
    }
    
	private boolean estRegleDeclenchable() throws IOException {
    	
    	if(precharge.estOpperandeET()) { 
    		precharge = lexical.suivant();
    	}
    	
    	if(!EstET()){ return false; }
    	
    	while(precharge.estOpperandeET()) {
    		precharge = lexical.suivant();
    		if(!EstET()){ return false; }
    	}
    	return true;
    }
    
    private boolean EstET() throws IOException{
    	
    	if(!EstOU()){ return false; }
    	
    	while(precharge.estOpperandeET()) {
			precharge = lexical.suivant();
			if(!EstOU()) {return false;}
		}
    	return true;
    }
    
    private boolean EstOU() throws IOException {
		
    	if(!EstFait()) {
    		precharge = lexical.suivant();
    		if(!precharge.estOpperandeOU()) { return false; }
    	}
    	
		while(precharge.estOpperandeOU()) {
			precharge = lexical.suivant();
			if(!EstFait()) {
				precharge = lexical.suivant();
	    		return true;
			}
		}
		return true;
	}
    
    private boolean EstFait() throws IOException {

    	if (precharge.estOpperandeNON()) {
    		precharge = lexical.suivant();
        	if (precharge.estDansLaBase(MapFait)) {
        		FaitBooleen f = (FaitBooleen) MapFait.get(precharge.representation);
        		if (!f.getValeur()) {
	        		precharge = lexical.suivant();
	        		return true;
        		}
        	}
    	} else {
    		Jeton saveFait = precharge;
    		precharge = lexical.suivant();
    		System.out.println("2" + precharge.representation);
    		
    		if (precharge.estOpperandeET() || precharge.estOpperandeOU() || precharge.estFinCondition()) {
    			if (saveFait.estDansLaBase(MapFait)) {
            		FaitBooleen FaitBool = (FaitBooleen) MapFait.get(saveFait.representation);
            		if (!FaitBool.getValeur()) {
    	        		return true;
            		}
            	}
    		}
    		else if (precharge.estComparaisonEgale()) {
    			precharge = lexical.suivant();
    			
    			if (true/*!precharge.estEntier()*/) {
    				String chaine;
    				FaitSymbolique FaitSymbol1 = (FaitSymbolique) MapFait.get(saveFait.representation);
        			if (precharge.estDansLaBase(MapFait)) {
        				FaitSymbolique FaitSymbol2 = (FaitSymbolique) MapFait.get(precharge.representation);
        				chaine = FaitSymbol2.getValeur();
        			} else {
        				chaine = precharge.representation;
        			}
        			if (FaitSymbol1.getValeur().equals(chaine)) {
    					precharge = lexical.suivant();
    					return true;
    				}
    			}
    			else {
    				FaitEntier FaitInt1 = (FaitEntier) MapFait.get(saveFait.representation);
        			boolean estSup = precharge.estComparaisonSuperieure();
        			int entier;
        			precharge = lexical.suivant();
        			
        			if (estEntier(precharge.representation)) {
        				entier = Integer.parseInt(precharge.representation);
        			} else if (precharge.estDansLaBase(MapFait)) {
        				FaitEntier FaitInt2 = (FaitEntier) MapFait.get(precharge.representation);
        				entier = FaitInt2.getValeur();
        			} else {
        				return false;
        			}
        			
        			if (estSup) {
        				return FaitInt1.getValeur() > entier;
        			} else {
        				return FaitInt1.getValeur() < entier;
        			}
    			}
    		}
    		else if (precharge.estComparaisonInferieure() || precharge.estComparaisonSuperieure()) {
    			FaitEntier FaitInt1 = (FaitEntier) MapFait.get(saveFait.representation);
    			boolean estSup = precharge.estComparaisonSuperieure();
    			int entier;
    			precharge = lexical.suivant();
    			
    			if (estEntier(precharge.representation)) {
    				entier = Integer.parseInt(precharge.representation);
    			} else if (precharge.estDansLaBase(MapFait)) {
    				FaitEntier FaitInt2 = (FaitEntier) MapFait.get(precharge.representation);
    				entier = FaitInt2.getValeur();
    			} else {
    				return false;
    			}
    			
    			if (estSup) {
    				return FaitInt1.getValeur() > entier;
    			} else {
    				return FaitInt1.getValeur() < entier;
    			}
    		}
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

    /*private boolean GestionFait() throws IOException {
    	
    	switch(precharge.type) {
		
		case FaitBooleen:
			estFaitBooleen();
			break;
		
		case FaitEntier:
			FaitEntier FaitInt = (FaitEntier) MapFait.get(precharge.representation);
        	if (precharge.estDansLaBase(MapFait)) {
        		precharge = lexical.suivant();
        		
        	}
			break;
		
		case FaitSymbolique:
			return esFaitSymbolique();
		
		default:
			break;    		
		}
		return false;
		
	}

	private boolean estFaitBooleen() throws IOException {
		
		FaitBooleen FaitBool = (FaitBooleen) MapFait.get(precharge.representation);
    	if (precharge.estDansLaBase(MapFait) && !FaitBool.getValeur()) {
    		precharge = lexical.suivant();
    		return true;
    	}
    	return false;
	}
	
	private boolean esFaitSymbolique() throws IOException {

		boolean nonEgal = false;
		String chaine;
		
    	if (precharge.estDansLaBase(MapFait)) {
			FaitSymbolique FaitSymbol1 = (FaitSymbolique) MapFait.get(precharge.representation);
    		precharge = lexical.suivant();
    		
    		if (precharge.estOpperandeNON()) {
    			precharge = lexical.suivant();
    			nonEgal = true;
    		}
    		
    		if (precharge.estComparaisonEgale()) {
    			precharge = lexical.suivant();
    			
    			if (precharge.estDansLaBase(MapFait)) {
    				FaitSymbolique FaitSymbol2 = (FaitSymbolique) MapFait.get(precharge.representation);
    				chaine = FaitSymbol2.getValeur();
    			} else {
    				chaine = precharge.representation;
    			}
    			
    			if (nonEgal) {
    				if (!FaitSymbol1.getValeur().equals(chaine)) {
    					precharge = lexical.suivant();
    					return true;
    				}
    			} else {
    				if (FaitSymbol1.getValeur().equals(chaine)) {
    					precharge = lexical.suivant();
    					return true;
    				}
    			}
    			
    		}
    	}
    	return false;
	}*/

	/*private void ajoutDansLaBase(Jeton fait, Fait fait1) {
		
	}*/

}
