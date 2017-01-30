package sim;

import java.util.ArrayList;

import car.Car;
import map.Itineraire;
import map.Map;
import map.Road;

public class SimuCreaCars {
	float deltaT;

	public SimuCreaCars() {
		super();
		deltaT = 0;
	}

	public float createCars(float t, float deltaT, Map m, boolean fake) {
		Itineraire it = new Itineraire();
		it.add(m.getRoads().get(1));
		Itineraire it2 = new Itineraire();
		it2.add(m.getRoads().get(1));
		Itineraire it3 = new Itineraire();
		it3.add(m.getRoads().get(5));
		Itineraire it4 = new Itineraire();
		it4.add(m.getRoads().get(5));
		float t2=3.7f;
		if (deltaT >= 1.0f && t < 1.0f) {
			if (!fake) {
				
				
				addCarRoad(m.getRoads().get(0), m, "test 1", 11.111f, it);
				addCarRoad(m.getRoads().get(4), m, "test 3", 11.111f, it2);
			}

			return 1;
		} else if (deltaT >= t2 && t < t2) {

			if (!fake) {
				addCarRoad(m.getRoads().get(4), m, "test 2", 11.111f, it3);
				
				
				//addCarRoad(m.getRoads().get(0), m, "test 4", 40.0f, it);
				
			} 
				
			return t2;
				
			

		} else {
			return -1;
		}

	}

	private boolean addCarRoad(Road r, Map m, String name, float speed, Itineraire it) {
		// on regarde si on peut créer la voiture
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
