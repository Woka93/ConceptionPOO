package booleen;

import java.util.HashMap;

public class Jeton {
	
	protected Type type;
	protected String representation;
	
	public enum Type {
		Condition,
		OpperandeOU,
		OpperandeET,
		OpperandeNON,
		Fait,
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

	public boolean estDansLaBase(HashMap<String, Boolean> MapFait) {
		return type == Jeton.Type.Fait && MapFait.containsKey(representation);
	}
	
	public boolean estFinCondition() {
		return type == Jeton.Type.FinCondition;
	}
	
	public boolean estFinFichier() {
		return type == Jeton.Type.FinFichier;
	}
}
