package test;

import java.util.HashMap;

class Jeton {

	protected Type type;
	protected String representation;

	public enum Type {
		Condition,
		OpperandeOU,
		OpperandeET,
		OpperandeNON,
		Fait,
		ComparaisonEgale,
		ComparaisonSuperieure,
		ComparaisonInferieure,
		FinCondition,
		FinFichier
     }
	
	protected Jeton(Type type, String representation) {
		this.type = type;
		this.representation = representation;
    }

	public boolean estCondition() {
		return type == Jeton.Type.Condition;
	}

	public boolean estOpperandeOU() {
		return type == Jeton.Type.OpperandeOU;
	}

	public boolean estOpperandeET() {
		return type == Jeton.Type.OpperandeET;
	}
	
	public boolean estOpperandeNON() {
		return type == Jeton.Type.OpperandeNON;
	}
	
	public boolean estComparaisonEgale() {
		return type == Jeton.Type.ComparaisonEgale;
	}
	
	public boolean estComparaisonSuperieure() {
		return type == Jeton.Type.ComparaisonSuperieure;
	}
	
	public boolean estComparaisonInferieure() {
		return type == Jeton.Type.ComparaisonInferieure;
	}

	public boolean estDansLaBase(HashMap<String, Fait> mapFait) {
		return mapFait.containsKey(representation);
	}
	
	public boolean estFinCondition() {
		return type == Jeton.Type.FinCondition;
	}
	
	public boolean estFinFichier() {
		return type == Jeton.Type.FinFichier;
	}
}
