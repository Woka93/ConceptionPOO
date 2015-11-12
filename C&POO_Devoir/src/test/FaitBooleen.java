package test;

public class FaitBooleen extends Fait{
	
	protected boolean valeur;
	
	public FaitBooleen (String nom, boolean valeur) {
		
		super();
		this.nom = nom;
		this.type = "booleen";
		this.valeur = valeur;
	}
	
	public boolean getValeur () {
		return this.valeur;
	}

	@Override
	protected boolean aPourValeur(Object value) {
		boolean val = (boolean) value;
		return !(this.valeur^val);
	}

}
