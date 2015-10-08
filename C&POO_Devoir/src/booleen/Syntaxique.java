package booleen;

import java.io.IOException;

public class Syntaxique {

	protected Lexical lexical;
	protected Jeton precharge;
	
	public Syntaxique(Lexical lexical) {
    	this.lexical = lexical;
    }

    public boolean verifier() throws IOException {

		precharge = lexical.suivant();
		Ssytem.out.println(precharge.representation);
	
		if (! estRegleDeclanchable()) {
		    return false;
		}
		
		if (precharge.estFinCondition()) {
			ajoutDansLaBase(lexical.suivant());
			precharge = lexical.suivant();
		}
		return precharge.estFinRegle();
    }
    
    protected boolean estRegleDeclanchable() throws IOException {
    	
    	if (precharge.estCondition()) {
    		precharge = lexical.suivant();
    	}
    	
    	if (!estFaitVrai()) {
    		return false;
    	}
    	
    	if (!estOpperande()) {
    		return false;
    	}
    	
    	if (!estFaitVrai()) {
    		return false;
    	}
    	
    	return true;
    }
    
    protected boolean estOpperande() throws IOException {
    	
    	if (precharge.estOpperandeET() || precharge.estOpperandeOU()) {
    		precharge = lexical.suivant();
    		return true;
    	}
    	return false;    	
    }
    
    protected boolean estFaitVrai() throws IOException {
    	if (precharge.estDansLaBase()) {
    		precharge = lexical.suivant();
    		return true;
    	}
    	return false;
    }

	public void ajoutDansLaBase(Jeton fait) {
		System.out.println("Règle déclenchée");
	}
}
