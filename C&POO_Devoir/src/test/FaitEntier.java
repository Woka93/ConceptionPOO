package test;

public class FaitEntier extends Fait {
	
	protected int valeur;
	
	public FaitEntier (String nom, int valeur) {
		super();
		this.nom = nom;
		this.type = "entier";
		this.valeur = valeur;
	}
	
	public int getValeur() {
		return this.valeur;
	}

	@Override
	protected boolean aPourValeur(Object value) {
		int val = (int) value;
		return this.valeur == val;
	}

}
