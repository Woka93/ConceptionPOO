package booleen;

public class FabriqueJeton {

	protected static final Jeton condition = new Jeton (Jeton.Type.Condition, "?");
	protected static final Jeton opperandeOU = new Jeton (Jeton.Type.OpperandeOU, "|");
	protected static final Jeton opperandeET = new Jeton (Jeton.Type.OpperandeET, "&");
	protected static final Jeton finCondition = new Jeton (Jeton.Type.FinCondition, ":");
	protected static final Jeton finRegle = new Jeton (Jeton.Type.FinRegle, "");
	
	public static Jeton condition() {
		return condition;
	}
	
	public static Jeton opperandeOU() {
		return opperandeOU;
	}
	
	public static Jeton opperandeET() {
		return opperandeET;
	}
	
	public static Jeton finCondition() {
		return finCondition;
	}
	
	public static Jeton finRegle() {
		return finRegle;
	}

	public static Jeton fait(String representation) {
		return new Jeton(Jeton.Type.Fait, representation);
	}

	public static Jeton inconnu(String representation) {
		return new Jeton(Jeton.Type.Inconnu, representation);
	}

}
