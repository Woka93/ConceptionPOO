package booleen;

public class Jeton {
	
	protected Type type;
	protected String representation;
	
	public enum Type {
		Condition,
		OpperandeOU,
		OpperandeET,
		Fait,
		Inconnu,
		FinCondition,
		FinRegle
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

	public boolean estDansLaBase() {
		return type == Jeton.Type.Fait;
	}
	
	public boolean estFinCondition() {
		return type == Jeton.Type.FinCondition;
	}
	
	public boolean estFinRegle() {
		return type == Jeton.Type.FinRegle;
	}

}
