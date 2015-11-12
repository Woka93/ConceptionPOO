package test;

public class FaitSymbolique extends Fait {

	protected String valeur;
	
	public FaitSymbolique (String nom, String valeur) {
		
		super();
		this.nom = nom;
		this.type = "symbolique";
		this.valeur = valeur;
	}
	
	public String getValeur() {
		return this.valeur;
	}

	@Override
	protected boolean aPourValeur(Object value) {
		String val = (String) value;
		return this.valeur.equals(val);
	}
}
