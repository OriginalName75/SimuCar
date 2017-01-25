package map;

import java.util.ArrayList;

import car.Car;
import node.Node;

public class Road {
	private Node start;
	private Node end;
	private float length;
	protected String nom;
	
	public Road(Node start, Node end, float length,String nom) {
		super();
		this.start = start;
		this.end = end;
		this.length=length;
		this.nom=nom;
	}
	public Road(String start, String end, float length,String nom) {
		super();
		this.start = new Node(start);
		this.end = new Node(end);
		this.length=length;
		this.nom=nom;
	}
	public String getName() {
		return  nom + " : " + this.start.getNom() + " -> " + this.end.getNom();
	}
	public Node getStart() {
		return start;
	}
	public Node getEnd() {
		return end;
	}
	public float getLength() {
		return length;
	}
	public ArrayList<Car> carsInRoad(Carte m) {
		ArrayList<Car> cars = new ArrayList<Car>();
		for (Car car : m.getCars()) {
			if (car.getOnRoad().equals(this)) {
				cars.add(car);
			}
		}
		
		
		return cars;
	}
}
