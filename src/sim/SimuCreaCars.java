package sim;

import java.util.ArrayList;
import java.util.LinkedList;

import car.Car;
import map.Carte;
import map.DijkstraAlgorithm;
import map.Itineraire;
import map.Road;
import node.Node;

public class SimuCreaCars {
	long idCar;
	private DijkstraAlgorithm alg;

	public SimuCreaCars(Carte m) {
		super();
		alg = new DijkstraAlgorithm(m);
		idCar=0;

	}

	private Itineraire buildIt(Node a, Node b, Carte m) {
		alg.execute(a);
		// sim.simulate(500);
		LinkedList<Node> path = alg.getPath(b);
		int goo = 0;
		Itineraire it = new Itineraire();
		Node before = a;
		for (Node vertex : path) {

			if (goo > 0) {

				it.add(m.findRoad(before, vertex));
			}
			before = vertex;
			goo++;
		}

		return it;

	}

	public float createCar(float t, float deltaT, Carte m, boolean fake) {

		ArrayList<Node> l = new ArrayList<Node>();
		float minn = -1;
		for (Node n : m.getNodes()) {
			if (n.getWait() != -1.0f) {
				if (n.getWait() < 0) {
					System.err.println("Erreur 666-");
					System.exit(0);

				}

				if (minn == -1) {
					minn = n.getWait();
					l.add(n);
				} else {
					if (minn > n.getWait()) {
						minn = n.getWait();
						l = new ArrayList<Node>();
						l.add(n);
					} else if (minn == n.getWait()) {

						l.add(n);
					}
				}
			}
		}
		if (minn != -1 && deltaT >= minn && t < minn) {
			if (!fake) {

				for (Node node : l) {
					Node desti = node.chooseRandomDesti();

					Itineraire it = buildIt(node, desti, m);
					// check next Gaussian value
					Road f = it.first();
					it.goNext();
					addCarRoad(f, m, "v " +idCar, 13.8889f, it);
					idCar++;	
					node.setNext();
				}

			}
			return minn;
		}

		return -1;
	}

	public float createCarTest(float t, float deltaT, Carte m, boolean fake) {
		Itineraire it = new Itineraire();
		it.add(m.getRoads().get(1));
		Itineraire it2 = new Itineraire();
		it2.add(m.getRoads().get(1));
		Itineraire it3 = new Itineraire();
		it3.add(m.getRoads().get(5));
		Itineraire it4 = new Itineraire();
		it4.add(m.getRoads().get(5));
		Itineraire it5 = new Itineraire();
		it5.add(m.getRoads().get(5));
		Itineraire it6 = new Itineraire();
		it6.add(m.getRoads().get(5));
		float t2 = 4.7f;
		if (deltaT >= 1.0f && t < 1.0f) {
			if (!fake) {

				addCarRoad(m.getRoads().get(2), m, "voiture 1", 11.111f, it);

			}

			return 1;
		} else if (deltaT >= t2 && t < t2) {

			if (!fake) {
				// addCarRoad(m.getRoads().get(4), m, "voiture 3", 11.111f,
				// it4);

			}

			return t2;

		} else {
			return -1;
		}

	}

	private boolean addCarRoad(Road r, Carte m, String name, float speed, Itineraire it) {
		// on regarde si on peut cr�er la voiture
		Car c = new Car(name, r, speed);

		c.setItineraire(it);
		ArrayList<Road> roads = r.getStart().raodsWhereNodeIsStart(m);
		ArrayList<Car> cars = new ArrayList<Car>();
		for (Road road : roads) {
			ArrayList<Car> cars2 = road.carsInRoad(m);
			cars.addAll(cars2);
		}
		boolean colision = false;
		for (Car car : cars) {
			if (c.percution(car)) {
				colision = true;

				break;
			}
		}
		if (!colision) {
			// on peut creer la voiture

			m.addCar(c);
			System.out.println("Creation de la voiture " + c.getNom() + " dans la route " + r.getName());

			return true;
		} else {

			System.out.println("Impossible de creer la voiture " + c.getNom() + " dans la route " + r.getName());

			return false;
		}
	}

}
