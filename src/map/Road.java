package map;

import java.util.ArrayList;

import car.Car;

public class Road {
	private Node start;
	private Node end;
	private float length;
	public Road(Node start, Node end, float length) {
		super();
		this.start = start;
		this.end = end;
		this.length=length;
	}
	public Road(String start, String end, float length) {
		super();
		this.start = new Node(start);
		this.end = new Node(end);
		this.length=length;
	}
	public String getName() {
		return this.start.getNom() + " -> " + this.end.getNom();
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
	public ArrayList<Car> carsInRoad(Map m) {
		ArrayList<Car> cars = new ArrayList<Car>();
		for (Car car : m.getCars()) {
			if (car.getOnRoad().equals(this)) {
				cars.add(car);
			}
		}
		
		
		return cars;
	}
}
