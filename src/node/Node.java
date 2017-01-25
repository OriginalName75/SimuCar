package node;

import java.util.ArrayList;

import car.Car;
import map.Carte;
import map.Road;

public class Node {
	protected String nom;
	protected ArrayList<Integer> repartition;
	public Node(String nom) {
		super();
		this.nom = nom;
		repartition = new ArrayList<Integer>();
		
	}
	public void geneRep() {
		
	}
	public void addRep(int i) {
		repartition.add(i);
	}
	/**
	 * @return the repartition
	 */
	public ArrayList<Integer> getRepartition() {
		return repartition;
	}

	
	public float getWait() {
		return -1;
	}
	public void setNext() {
		System.err.println("Erreur de génération de voiture 1");
		
	}
	public Node chooseRandomDesti() {
		System.err.println("Erreur de proba 2");
		System.exit(0);
		return null;
	}
	public boolean checkGo(Car r, Carte m, boolean in, Road nextRoad, float plusD, float t) {
		System.err.println("Erreur de construction dela map 3");
		return false;
	}
	public void clear(Car r) {
		System.err.println("Erreur de construction dela map 4");
		
	}
	public float getD(Car r) {
		System.err.println("Erreur de construction dela map 1");
		return 0;
	}
	public boolean isInter() {
		return false;
	}
	public int roadToInt(Road r) {
		System.err.println("Erreur de construction dela map 2");
		return 0;
	}
	public String getNom() {
		return nom;
	}
	
	public ArrayList<Car> carssInNode( Carte m, Road ro) {
		
		return new ArrayList<Car>();
	}
	public ArrayList<Car> carssInNode( Carte m) {
		ArrayList<Car> r = new ArrayList<Car>();
		for (Car c: m.getCars()) {
			if (c.isInIntersection() && c.getOnRoad().getStart().equals(this)) {
				r.add(c);
			}
		}
		return r;
	}
	
	public ArrayList<Road> raodsCoToNode( Carte m) {
		ArrayList<Road> r = new ArrayList<Road>();
		for (Road road : m.getRoads()) {
			if (road.getStart().equals(this) || road.getEnd().equals(this)) {
				r.add(road);
			}
		}
		return r;
	}
	public ArrayList<Road> raodsWhereNodeIsStart(Carte m) {
		ArrayList<Road> r = new ArrayList<Road>();
		for (Road road : m.getRoads()) {
			if (road.getStart().equals(this)) {
				r.add(road);
			}
		}
		return r;
	}
}
