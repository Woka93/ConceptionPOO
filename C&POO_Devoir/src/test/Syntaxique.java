package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

class Syntaxique {
	
	public Lexical lexical;
	public Jeton precharge;
	private HashMap<String,Fait> MapFait = new HashMap<String,Fait>();
	
	public Syntaxique (Lexical lexical, BufferedReader fichier_connaissances) {
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
	
	public void test() throws IOException {

		/*precharge = lexical.suivant();
		System.out.println(precharge.representation);*/
		
		if(!precharge.estCondition()) {
			
			GestionConculsion();
		}
		else {
			precharge = lexical.suivant();
			if (estRegleDeclenchable()) {
				precharge = lexical.suivant();
				//System.out.println(precharge.representation);
				GestionConculsion();
				System.out.println("OK");
			}
		}
		/*String deduc = "";
		for (Entry<String, Fait> entry : MapFait.entrySet()) {
			FaitBooleen f = (FaitBooleen) entry.getValue();
			deduc += " " + entry.getKey() + " = " + f.valeur + ",";
		}
		System.out.println(deduc);*/
		System.out.println(MapFait.toString());
	}
	
	private boolean estRegleDeclenchable() throws IOException {

		//System.out.println(precharge.representation);
    	if(precharge.estOpperandeET()) {
    		precharge = lexical.suivant();
			//System.out.println(precharge.representation);
    	}
    	
    	if(!EstET()){ return false; }
    	
    	while(precharge.estOpperandeET()) {
    		precharge = lexical.suivant();
			//System.out.println(precharge.representation);
    		if(!EstET()){ return false; }
    	}
    	return true;
    }
    
    private boolean EstET() throws IOException{
    	
    	if(!EstOU()){ return false; }
    	
    	while(precharge.estOpperandeET()) {
			precharge = lexical.suivant();
			//System.out.println(precharge.representation);
			if(!EstOU()) {return false;}
		}
    	return true;
    }
    
    private boolean EstOU() throws IOException {
		
    	if(!EstFait()) {
    		precharge = lexical.suivant();
			//System.out.println(precharge.representation);
    		if(!precharge.estOpperandeOU()) { return false; }
    	}
    	
		while(precharge.estOpperandeOU()) {
			precharge = lexical.suivant();
			//System.out.println(precharge.representation);
			if(!EstFait()) {
				precharge = lexical.suivant();
				//System.out.println(precharge.representation);
	    		return true;
			}
		}
		return true;
	}
    
    private boolean EstFait() throws IOException {

    	if (precharge.estOpperandeNON()) {
			//System.out.println("NON : " + precharge.representation);
			precharge = lexical.suivant();
			//System.out.println("fait : " + precharge.representation);
			if (precharge.estDansLaBase(MapFait)) {
        		FaitBooleen FaitBool = (FaitBooleen) MapFait.get(precharge.representation);
        		if (!FaitBool.getValeur()) {
	        		precharge = lexical.suivant();
					//System.out.println(precharge.representation);
	        		return true;
        		}
        	}
		}
		else if (!precharge.estDansLaBase(MapFait)) {
			while (!precharge.estOpperandeET() || !precharge.estOpperandeOU()) {
				precharge = lexical.suivant();
				//System.out.println(precharge.representation);
			}
			return false;
		}
		else {
			Fait saveFait = MapFait.get(precharge.representation);
			
			switch (saveFait.type) {
			
			case "booleen" : 
				FaitBooleen FaitBool = (FaitBooleen) saveFait;
        		if (FaitBool.getValeur()) {
        			precharge = lexical.suivant();
            		//System.out.println(precharge.representation);
	        		return true;
        		}
        		break;
        		
			case "symbolique" :
				FaitSymbolique FaitSymbol;
				precharge = lexical.suivant();
				//System.out.println(precharge.representation);
				
				if (!precharge.estOpperandeNON()) {
					precharge = lexical.suivant();
					//System.out.println(precharge.representation);
					FaitSymbol = (FaitSymbolique) saveFait;
					if (FaitSymbol.aPourValeur(precharge.representation)) {
	        			precharge = lexical.suivant();
						return true;
					}
				} else {
					precharge = lexical.suivant();
					//System.out.println(precharge.representation);
					precharge = lexical.suivant();
					//System.out.println(precharge.representation);
					FaitSymbol = (FaitSymbolique) saveFait;
					if (!FaitSymbol.aPourValeur(precharge.representation)) {
	        			precharge = lexical.suivant();
						return true;
					}
				}
				break;
				
			case "entier" :
				FaitEntier FaitInt;
				precharge = lexical.suivant();
				//System.out.println(precharge.representation);
				
				if (precharge.estComparaisonEgale()) {
					FaitInt = (FaitEntier) saveFait;
					if (FaitInt.aPourValeur(Integer.parseInt(precharge.representation))) {
	        			precharge = lexical.suivant();
						return true;
					}
				}
				else if (precharge.estComparaisonInferieure()) {
					precharge = lexical.suivant();
					System.out.println(precharge.representation);
					if (precharge.estComparaisonEgale()) {
						precharge = lexical.suivant();
						//System.out.println(precharge.representation);
						FaitInt = (FaitEntier) saveFait;
						if (FaitInt.getValeur() <= (Integer.parseInt(precharge.representation))) {
		        			precharge = lexical.suivant();
							return true;
						}
					}
					else {
						FaitInt = (FaitEntier) saveFait;
						if (FaitInt.getValeur() < (Integer.parseInt(precharge.representation))) {
		        			precharge = lexical.suivant();
							return true;
						}
					}
				}
				else {
					precharge = lexical.suivant();
					//System.out.println(precharge.representation);
					if (precharge.estComparaisonEgale()) {
						precharge = lexical.suivant();
						//System.out.println(precharge.representation);
						FaitInt = (FaitEntier) saveFait;
						if (FaitInt.getValeur() >= (Integer.parseInt(precharge.representation))) {
		        			precharge = lexical.suivant();
							return true;
						}
					}
					else {
						FaitInt = (FaitEntier) saveFait;
						if (FaitInt.getValeur() > (Integer.parseInt(precharge.representation))) {
		        			precharge = lexical.suivant();
							return true;
						}
					}
				}
			}
			return false;
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
    
    private void GestionConculsion() throws IOException {
    	
		if (precharge.estOpperandeNON()) {
			precharge = lexical.suivant();
			FaitBooleen FaitBool = new FaitBooleen(precharge.representation, false);
			MapFait.put(precharge.representation, FaitBool);
		}
		else {
			Jeton saveFait = precharge;
			//System.out.println("save fait : " + precharge.representation);
			precharge = lexical.suivant();
			//System.out.println(precharge.representation);
			
			if (precharge.estComparaisonEgale()) {
				precharge = lexical.suivant();
				//System.out.println(precharge.representation);
				
				if (!estEntier(precharge.representation)) {
					FaitSymbolique FaitSymbol = new FaitSymbolique(saveFait.representation, precharge.representation);
					MapFait.put(saveFait.representation, FaitSymbol);
				}
				else {
					FaitEntier FaitInt = new FaitEntier(saveFait.representation, Integer.parseInt(precharge.representation));
					MapFait.put(saveFait.representation, FaitInt);
				}
			}
			else {
				FaitBooleen FaitBool = new FaitBooleen(saveFait.representation, true);
				MapFait.put(saveFait.representation, FaitBool);
			}
		}
    }
}