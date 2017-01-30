package sim;

import java.util.ArrayList;
import java.util.Iterator;

import car.Car;
import map.Map;

public class Simulation {
	public Map map;
	private SimuCar simuCar;
	private SimuCreaCars simuCreaCars;

	public Simulation() {
		super();
		map = new Map();
		simuCar = new SimuCar();
		simuCreaCars = new SimuCreaCars();
	}

	public void simulate(float delta) {
		simulate(delta, 0);

	}

	public void simulate(float delta, float t) {
		
		float min = simuCreaCars.createCars(t, delta, map, true);
		ArrayList<Integer> idMin = new ArrayList<Integer>();
		if (min != -1) {
			idMin.add(-1);
		}

		int i = 0;

		for (Car car : map.getCars()) {
			float a = simuCar.simIna(delta, t, car, true, map);

			if (a != -1) {
				if (min == -1) {
					min = a;

					idMin = new ArrayList<Integer>();
					idMin.add(i);
				} else {
					if (min > a) {
						min = a;

						idMin = new ArrayList<Integer>();
						idMin.add(i);
					} else if (min == a) {
						idMin.add(i);

					}
				}

			}
			i++;
		}
		if (min != -1) {
			simuCar.simulateCars( min - t, map.getCars());
		}
		if (idMin.contains(-1)) {

			simuCreaCars.createCars(t, delta, map, false);
		}
		i = 0;
		for (Car car : map.getCars()) {
			if (idMin.contains(i)) {

				simuCar.simIna(delta, t, car, false, map);

			}
			i++;
		}

		if (min != -1) {

			System.out.println("t = " + min);
			System.out.println();

			Iterator<Car> ite = map.getCars().iterator();
			while (ite.hasNext()) {
				Car c = ite.next(); // must be called before you can call
									// i.remove()
				// Do something
				if (c.isDestroy()) {
					
					ite.remove();
					c = null;
				}

			}
			
			
			simulate(delta, min);
		} else {
			simuCar.simulateCars(	 delta - t, map.getCars());
			System.out.println("Fin de la simulation");
			System.out.println("t = " + delta);
		}

	}
}
