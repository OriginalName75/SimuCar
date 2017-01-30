package sim;

import java.util.ArrayList;

import car.Car;
import map.Map;

public class SimuCar {

	public void simulateCars(float dt, ArrayList<Car> cars) {
		for (Car car : cars) {
			if (car.isInIntersection()) {
				if (car.getdAvantIntersection() < car.getOnRoad().getStart().getdDansintersection()) {
					if (car.isRoule()) {
						float[] d3 = car.distanceParcourue(dt);
						car.setSpeed(d3[1]);
						car.setdAvantIntersection(car.getdAvantIntersection()+ d3[0]);
						car.continueC(dt);
						System.out.println(car.getNom() + " | roule avant l'inteersection : "
								+ car.getOnRoad().getStart().getNom() + " d :" + car.getdIntersection());
					}else {
						System.out.println(car.getNom() + " | attend son tour AVANT l'inteersection : "
								+ car.getOnRoad().getStart().getNom() + " d :" + car.getdIntersection());
					}
					
				}else {
					if (car.isRoule()) {
						float[] d3 = car.distanceParcourue(dt);
						car.setSpeed(d3[1]);
						car.setdIntersection(car.getdIntersection() + d3[0]);
	
						car.continueC(dt);
	
						System.out.println(car.getNom() + " | roule dans l'inteersection : "
								+ car.getOnRoad().getStart().getNom() + " d :" + car.getdIntersection());
					} else {
						System.out.println(car.getNom() + " | attend son tour dans l'inteersection : "
								+ car.getOnRoad().getStart().getNom() + " d :" + car.getdIntersection());
					}
				}

			} else {
				if (car.isRoule()) {
					if (car.isBreaking()) {
						car.breaking(dt);
					} else {
						float[] d3 = car.distanceParcourue(dt);
						car.setdFromNode(car.getdFromNode() + d3[0]);
						car.setSpeed(d3[1]);
						System.out.println(car.getNom() + " | route : " + car.getOnRoad().getName() + " | distnce : "
								+ car.getdFromNode());
					}

				} else {
					System.out.println(car.getNom() + " | route : " + car.getOnRoad().getName() + " | distnce : "
							+ car.getdFromNode() + " | est arrété");
				}

			}
		}

	}

	public float simIna(float delta, float t, Car c, boolean fake, Map m) {
		float d = c.getSpeed() * ((float) (delta - t));

		if (c.isInIntersection()) {
			if (c.getdAvantIntersection() < c.getOnRoad().getStart().getdDansintersection()) {
				float minDt2 = -1;
				String nom = "";
				boolean fre = true;
				for (Car car : c.getOnRoad().getEnd().carssInNode(m, c.getOnRoad())) {
					
					if (car.getdAvantIntersection() < car.getOnRoad().getStart().getdDansintersection()) {
						float dt2 = c.tBreaking(car, 1, true);
						float dt22 = c.tBreaking(car, 1, false);
						boolean fr = true;
						if (dt2 > dt22) {
							dt2 = dt22;
							fr = false;
						}
						if (minDt2 == -1) {
							minDt2 = dt2;
							nom = car.getNom();
							fre = fr;
						} else {
							if (minDt2 > dt2) {
								minDt2 = dt2;
								nom = car.getNom();
								fre = fr;
							}
						}
					}
				}
				if (minDt2 != -1) {
					if (minDt2 == 0) {
						return -1;
					}
					if (!fake) {
						if (fre) {
							c.setBreaking(true);
							System.out.println("La voiture " + c.getNom() + " commence à freiner car la voiture " + nom
									+ " l'empeche d'avancer AVANT LINTER");
						} else {
							if (c.getSpeed() == 0) {
								c.startTheCar();
							} else {
								c.reAcc();
							}
							System.out.println("La voiture " + c.getNom() + " commence à accelerer  AVANT LINTER");
						}
					}
					return t + minDt2;
				}
				if (c.isRoule()) {
					if (c.getOnRoad().getStart().checkGo(c, m, !fake)) {
						
					}
					/////////////////////// attendre quentin
					
				}else {
					return -1;
				}
			}
			if (!c.isRoule()) {

				if (c.getOnRoad().getStart().checkGo(c, m, !fake)) {
					if (!fake) {
						c.startTheCar();

					}

					if (t + 0.0000001f < delta) {

						return t + 0.0000001f;
					} else {
						return -1;
					}

				} else {
					return -1;
				}

			} else {
				float d2 = c.getOnRoad().getStart().getD(c);
				float[] d3 = c.distanceParcourue(delta - t);

				if (d3[0] >= d2) {
					if (!fake) {

						c.setInIntersection(false);
						System.out.println(c.getNom() + " à passé l'intersection");
						c.getOnRoad().getStart().clear(c);
					}

					return t + c.tempsParcourue(d2);

				} else {

					return -1;
				}
			}

		} else {

			float minDt2 = -1;
			String nom = "";
			boolean fre = true;
			for (Car car : c.voituresDevant(m)) {

				if (!car.isRoule() || car.getSpeed() != car.getMaxSpeed()) {

					float dt2 = c.tBreaking(car, 0, true);
					float dt22 = c.tBreaking(car, 0, false);
					boolean fr = true;
					if (dt2 > dt22) {
						dt2 = dt22;
						fr = false;
					}
					if (minDt2 == -1) {
						minDt2 = dt2;
						nom = car.getNom();
						fre = fr;
					} else {
						if (minDt2 > dt2) {
							minDt2 = dt2;
							nom = car.getNom();
							fre = fr;
						}
					}

				}

			}
			if (minDt2 != -1) {
				if (minDt2 == 0) {
					return -1;
				}
				if (!fake) {
					if (fre) {
						c.setBreaking(true);
						System.out.println("La voiture " + c.getNom() + " commence à freiner car la voiture " + nom
								+ " l'empeche d'avancer");
					} else {
						if (c.getSpeed() == 0) {
							c.startTheCar();
						} else {
							c.reAcc();
						}
						System.out.println("La voiture " + c.getNom() + " commence àaccelerer ");
					}
				}
				return t + minDt2;
			} else {

				for (Car car : c.getOnRoad().getEnd().carssInNode(m, c.getOnRoad())) {

					float dt2 = c.tBreaking(car, 2, true);
					float dt22 = c.tBreaking(car, 2, false);
					boolean fr = true;
					if (dt2 > dt22) {
						dt2 = dt22;
						fr = false;
					}
					if (dt2 == -1.0f && fake) {

						return -1;
					} else {
						if (!fake) {
							if (fr) {
								c.startBreaking();
								System.out.println("La voiture " + c.getNom() + " commence à freiner car la voiture "
										+ car.getNom() + " l'empeche d'avancer, cas 2");
							} else {
								if (c.getSpeed() == 0) {
									c.startTheCar();
								} else {
									c.reAcc();
								}
								System.out.println("La voiture " + c.getNom() + " commence à accelerer, cas 2");
							}
						}

						return t + dt2;
					}
				}

			}
			if (!c.isRoule()) {
				if (!fake) {
					c.startTheCar();
					System.out.println("La voiture " + c.getNom() + " redemare");
				}
				return t + 0.0000001f;
			}
			if (c.getdFromNode() + d >= c.getOnRoad().getLength()) {

				// la voiture est arrivée
				if (!fake) {

					if (c.goNext()) {
						System.out.println("La voiture " + c.getNom() + " est arrivé à "
								+ c.getOnRoad().getEnd().getNom() + ", à sa destination ; Element supr");
						c.setDestroy();
					} else {
						c.setInIntersection(true);

						c.getOnRoad().getStart().checkGo(c, m, true);

					}

				}

				return t + (c.getOnRoad().getLength() - c.getdFromNode()) / c.getSpeed();

			} else {

				return -1;
			}

		}
	}
}
