package map;

import java.util.ArrayList;

import car.Car;
import inter.Stop;
import inter.TrafficLight;
import node.Intersection;
import node.Node;
import node.StartOrEnd;
import sim.Horaire;

public class Carte {
	private ArrayList<Road> roads;
	private ArrayList<Car> cars;
	private ArrayList<Node> nodes;
	
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public ArrayList<Road> getRoads() {
		return roads;
	}

	public ArrayList<Car> getCars() {
		return cars;
	}

	public void addCar(Car c) {
		cars.add(c);
	}

	public Road findRoad(Node a, Node b) {
		for (Road road : roads) {
			if (road.getStart().equals(a) && road.getEnd().equals(b)) {
				return road;
			}
		}
		System.err.println("Route introuvable");
		System.exit(0);
		return null;
	}

	public Itineraire BuildIt(Node a, Node b) {

		Itineraire it = new Itineraire();

		return it;

	}
	public void geneRepartition() {
		for (Node node : nodes) {
			node.geneRep();
		}
	}
	public Carte() {
		
		roads = new ArrayList<Road>();
		cars = new ArrayList<Car>();
		nodes = new ArrayList<Node>();
		StartOrEnd p1 = new StartOrEnd("P1", new Horaire(40, 300, 20, 100, 20));
		nodes.add(p1);
		StartOrEnd p2 = new StartOrEnd("P2", new Horaire(50, 200, 30, 150, 30));
		nodes.add(p2);
		StartOrEnd p3 = new StartOrEnd("P3", new Horaire(30, 100, 20, 300, 15));
		nodes.add(p3);
		StartOrEnd p4 = new StartOrEnd("P4", new Horaire(20, 100, 30, 200, 50));
		nodes.add(p4);
		StartOrEnd p5 = new StartOrEnd("P5", new Horaire(30, 400, 20, 150, 20));
		nodes.add(p5);
		StartOrEnd p6 = new StartOrEnd("P6", new Horaire(20, 50, 30, 100, 10));
		nodes.add(p6);
		StartOrEnd p7 = new StartOrEnd("P7", new Horaire(30, 30, 10, 100, 10));
		nodes.add(p7);

		p1.addProbaDesti(5, p2);
		p1.addProbaDesti(10, p3);
		p1.addProbaDesti(10, p4);
		p1.addProbaDesti(5, p5);
		p1.addProbaDesti(35, p6);
		p1.addProbaDesti(35, p7);

		p2.addProbaDesti(10, p1);
		p2.addProbaDesti(5, p3);
		p2.addProbaDesti(20, p4);
		p2.addProbaDesti(20, p5);
		p2.addProbaDesti(25, p6);
		p2.addProbaDesti(20, p7);

		p3.addProbaDesti(15, p1);
		p3.addProbaDesti(15, p2);
		p3.addProbaDesti(20, p4);
		p3.addProbaDesti(20, p5);
		p3.addProbaDesti(20, p6);
		p3.addProbaDesti(10, p7);

		p4.addProbaDesti(15, p1);
		p4.addProbaDesti(10, p2);
		p4.addProbaDesti(10, p3);
		p4.addProbaDesti(20, p5);
		p4.addProbaDesti(40, p6);
		p4.addProbaDesti(5, p7);

		p5.addProbaDesti(10, p1);
		p5.addProbaDesti(30, p2);
		p5.addProbaDesti(10, p3);
		p5.addProbaDesti(10, p4);
		p5.addProbaDesti(10, p6);
		p5.addProbaDesti(30, p7);

		p6.addProbaDesti(20, p1);
		p6.addProbaDesti(10, p2);
		p6.addProbaDesti(40, p3);
		p6.addProbaDesti(10, p4);
		p6.addProbaDesti(10, p5);
		p6.addProbaDesti(10, p7);

		p7.addProbaDesti(20, p1);
		p7.addProbaDesti(20, p2);
		p7.addProbaDesti(20, p3);
		p7.addProbaDesti(20, p4);
		p7.addProbaDesti(10, p5);
		p7.addProbaDesti(10, p6);

		Intersection i1 = new Intersection("I1");
		nodes.add(i1);
		Intersection i2 = new Intersection("I2");
		nodes.add(i2);
		Intersection i3 = new Intersection("I3");
		nodes.add(i3);
		Intersection i4 = new Intersection("I4");
		nodes.add(i4);

		i4.setInter(0, new Stop());
		i4.setInter(4, new Stop());
		i3.setInter(0, new TrafficLight());
		i3.setInter(2, new TrafficLight());
		i3.setInter(6, new TrafficLight());
		i1.setInter(0, new Stop());
		i1.setInter(4, new Stop());

		Road r2_1 = new Road(p5, i4, 4500, "R2.1");
		roads.add(r2_1);
		Road r3_2 = new Road(p6, i4, 1000, "R3.2");
		roads.add(r3_2);
		Road r2_2 = new Road(i4, i3, 800, "R2.2");
		roads.add(r2_2);
		Road r2_3 = new Road(i3, p4, 1400, "R2.3");
		roads.add(r2_3);
		Road r4 = new Road(p7, i3, 3000, "R4");
		roads.add(r4);
		Road r3_1 = new Road(i4, i1, 3500, "R3.1");
		roads.add(r3_1);
		Road r1_1 = new Road(p1, i1, 3000, "R1.1");
		roads.add(r1_1);
		Road r1_2 = new Road(i1, i2, 1300, "R1.2");
		roads.add(r1_2);
		Road r1_3 = new Road(i2, p3, 2000, "R1.3");
		roads.add(r1_3);
		Road r2 = new Road(i2, p2, 4500, "R2");
		roads.add(r2);

		// les retours

		Road r2_1_R = new Road(i4, p5, 4500, "R2.1_R");
		roads.add(r2_1_R);
		Road r3_2_R = new Road(i4, p6, 1000, "R3.2_R");
		roads.add(r3_2_R);
		Road r2_2_R = new Road(i3, i4, 800, "R2.2_R");
		roads.add(r2_2_R);
		Road r2_3_R = new Road(p4, i3, 1400, "R2.3_R");
		roads.add(r2_3_R);
		Road r4_R = new Road(i3, p7, 3000, "R4_R");
		roads.add(r4_R);
		Road r3_1_R = new Road(i1, i4, 3500, "R3.1_R");
		roads.add(r3_1_R);
		Road r1_1_R = new Road(i1, p1, 3000, "R1.1_R");
		roads.add(r1_1_R);
		Road r1_2_R = new Road(i2, i1, 1300, "R1.2_R");
		roads.add(r1_2_R);
		Road r1_3_R = new Road(p3, i2, 2000, "R1.3_R");
		roads.add(r1_3_R);
		Road r2_R = new Road(p2, i2, 4500, "R2_R");
		roads.add(r2_R);

		
		
		i1.set(r3_1, 0);
		i1.set(r3_1_R, 1);
		i1.set(r1_2_R, 2);
		i1.set(r1_2, 3);
		i1.set(r1_1, 6);
		i1.set(r1_1_R, 7);
		
		i2.set(r1_3_R, 2);
		i2.set(r1_3, 3);
		i2.set(r2_R, 4);
		i2.set(r2, 5);
		i2.set(r1_2, 6);
		i2.set(r1_2_R, 7);
		
		i3.set(r4, 0);
		i3.set(r4_R, 1);
		i3.set(r2_3_R, 2);
		i3.set(r2_3, 3);
		i3.set(r2_2, 6);
		i3.set(r2_2_R, 7);
		
		i4.set(r3_2, 0);
		i4.set(r3_2_R, 1);
		i4.set(r2_2_R, 2);
		i4.set(r2_2, 3);
		i4.set(r3_1_R, 4);
		i4.set(r3_1, 5);
		i4.set(r2_1, 6);
		i4.set(r2_1_R, 7);
		
		

	}

}
