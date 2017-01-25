package node;

public class ProbaArrive {
	private int proba;
	private StartOrEnd cible;
	/**
	 * @param proba
	 * @param cible
	 */
	public ProbaArrive(int proba, StartOrEnd cible) {
		super();
		this.proba = proba;
		this.cible = cible;
	}
	/**
	 * @return the proba
	 */
	public int getProba() {
		return proba;
	}
	/**
	 * @return the cible
	 */
	public StartOrEnd getCible() {
		return cible;
	}
	

}
