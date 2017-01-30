package map;

import java.util.ArrayList;

import car.Car;

public class Node {
	protected String nom;
	

	public Node(String nom) {
		super();
		this.nom = nom;
		
	}
	public boolean checkGo(Car r, Map m, boolean in) {
		System.out.println("Erreur de construction dela map 3");
		return false;
	}
	public void clear(Car r) {
		System.out.println("Erreur de construction dela map 4");
		
	}
	public float getD(Car r) {
		System.out.println("Erreur de construction dela map 1");
		return 0;
	}
	public boolean isInter() {
		return false;
	}
	public int roadToInt(Road r) {
		System.out.println("Erreur de construction dela map 2");
		return 0;
	}
	public String getNom() {
		return nom;
	}
	
	public ArrayList<Car> carssInNode( Map m, Road ro) {
		
		return new ArrayList<Car>();
	}
	public ArrayList<Car> carssInNode( Map m) {
		ArrayList<Car> r = new ArrayList<Car>();
		for (Car c: m.getCars()) {
			if (c.isInIntersection() && c.getOnRoad().getStart().equals(this)) {
				r.add(c);
			}
		}
		return r;
	}
	
	public ArrayList<Road> raodsCoToNode( Map m) {
		ArrayList<Road> r = new ArrayList<Road>();
		for (Road road : m.getRoads()) {
			if (road.getStart().equals(this) || road.getEnd().equals(this)) {
				r.add(road);
			}
		}
		return r;
	}
	public ArrayList<Road> raodsWhereNodeIsStart(Map m) {
		ArrayList<Road> r = new ArrayList<Road>();
		for (Road road : m.getRoads()) {
			if (road.getStart().equals(this)) {
				r.add(road);
			}
		}
		return r;
	}
}
