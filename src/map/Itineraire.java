package map;

import java.util.ArrayList;

public class Itineraire {
	protected ArrayList<Road> roads;

	public Itineraire() {
		roads = new ArrayList<Road>();

	}

	public void add(Road r) {
		roads.add(r);
	}

	public boolean estArrive() {
		return roads.isEmpty();
	}
	public Road first() {
		return roads.get(0);
	}
	public void goNext() {
		roads.remove(0);
		
	}
}
