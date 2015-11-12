package test;

class FabriqueJeton {

	protected static final Jeton condition = new Jeton (Jeton.Type.Condition, "?");
	protected static final Jeton opperandeOU = new Jeton (Jeton.Type.OpperandeOU, "|");
	protected static final Jeton opperandeET = new Jeton (Jeton.Type.OpperandeET, "&");
	protected static final Jeton opperandeNON = new Jeton (Jeton.Type.OpperandeNON, "!");
	protected static final Jeton comparaisonEgale = new Jeton (Jeton.Type.ComparaisonEgale, "=");
	protected static final Jeton comparaisonSuperieure = new Jeton (Jeton.Type.ComparaisonSuperieure, ">");
	protected static final Jeton comparaisonInferieure = new Jeton (Jeton.Type.ComparaisonInferieure, "<");
	protected static final Jeton finCondition = new Jeton (Jeton.Type.FinCondition, ":");
	protected static final Jeton finFichier = new Jeton (Jeton.Type.FinFichier, ".");
	
	public static Jeton condition() {
		return condition;
	}
	
	public static Jeton opperandeOU() {
		return opperandeOU;
	}
	
	public static Jeton opperandeET() {
		return opperandeET;
	}
	
	public static Jeton opperandeNON() {
		return opperandeNON;
	}
	
	public static Jeton comparaisonEgale() {
		return comparaisonEgale;
	}
	
	public static Jeton comparaisonSuperieure() {
		return comparaisonSuperieure;
	}
	
	public static Jeton comparaisonInferieure() {
		return comparaisonInferieure;
	}
	
	public static Jeton finCondition() {
		return finCondition;
	}
	
	public static Jeton finFichier() {
		return finFichier;
	}
	
	public static Jeton fait(String representation) {
		return new Jeton(Jeton.Type.Fait, representation);			
	}
}
