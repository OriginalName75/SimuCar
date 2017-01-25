package sim;

import java.util.ArrayList;

import car.Car;
import map.Carte;
import map.Road;

public class SimuCar {

	public void simulateCars(float dt, ArrayList<Car> cars) {
		for (Car car : cars) {
			if (car.isInIntersection()) {
				if (car.getdIntersection() < 0) {
					System.err.println(car.getNom() + " distance neg 1 " + car.getdIntersection());
					System.exit(1);
				}
				if (car.isRoule()) {
					if (car.isBreaking()) {
						System.err.println(car.getNom() + "sldkjdqlkjsdlkjqsd");
						System.exit(0);
					}
					float[] d3 = car.distanceParcourue(dt);
					car.setSpeed(d3[1]);
					car.setdIntersection(car.getdIntersection() + d3[0]);

					car.continueC(dt);

					System.out.println(
							car.getNom() + " | roule dans l'inteersection : " + car.getOnRoad().getStart().getNom()
									+ " d :" + car.getdIntersection() + " vitesse " + car.getSpeed());

				} else {
					System.out.println(car.getNom() + " | attend son tour dans l'inteersection : "
							+ car.getOnRoad().getStart().getNom() + " d :" + car.getdIntersection() + " pInit "
							+ car.getPositionInitIntersection());
				}

			} else {

				if (car.isRoule()) {

					if (car.isBreaking()) {
						car.breaking(dt);

					} else {
						float[] d3 = car.distanceParcourue(dt);
						car.setdFromNode(car.getdFromNode() + d3[0]);
						car.setSpeed(d3[1]);
						if (d3[1] == 0.0f) {
							System.out.println(car.getNom() + " | va BIENTOT accélerer : " + car.getOnRoad().getName()
									+ " d :" + car.getdFromNode() + " dans " + car.getCurrentReaction());

						} else {
							System.out.println(car.getNom() + " | route : " + car.getOnRoad().getName()
									+ " | distance : " + car.getdFromNode() + " vitesse " + car.getSpeed());
						}

					}

				} else {
					System.out.println(car.getNom() + " | route : " + car.getOnRoad().getName() + " | distnce : "
							+ car.getdFromNode() + " | est arrété");
				}

			}
		}

	}

	public float simIna(float delta, float t, Car c, boolean fake, Carte m) {
		if (!fake) {
			System.out.println(c.getNom());
		}
		if (c.isInIntersection()) {
			
			if (!c.isRoule()) {
				
				if (c.getOnRoad().getStart().checkGo(c, m, !fake, c.getOnRoad(), 0, t)) {
					
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
						System.out.println(c.getNom() + " a passé l'intersection");
						c.getOnRoad().getStart().clear(c);
					}
					if (c.tempsParcourue(d2) < 0 && fake) {
						System.err.println(c.getNom() + " err tkjrdhtkjhj " + c.tempsParcourue(d2));
						System.exit(0);
					}
					return t + c.tempsParcourue(d2);

				} else {

					return -1;
				}
			}

		} else {
			
			if (c.getdFromNode() >= c.getOnRoad().getLength()+1.0F) {
				System.err.println(c.getNom()+" daps");
				System.exit(0);
			}
			float minDt2 = -1;
			String nom = "";
			boolean fre = true;
			Car nexcar = c;
			boolean find = false;

			boolean noO = false;
			
			for (Car car : c.voituresDevant(m)) {

				if (minDt2 == -1) {
					minDt2 = car.getdFromNode() - c.getdFromNode();
					nexcar = car;
					find = true;
				} else {
					if (minDt2 > car.getdFromNode() - c.getdFromNode()) {
						minDt2 = car.getdFromNode() - c.getdFromNode();
						nexcar = car;
						find = true;
					}
				}

			}
			
			minDt2 = -1;
			//System.out.println(c.getNom() + " " + find+ " " + nexcar.getNom());
			if (find) {
				
				float dt2 = c.tBreaking(nexcar, 0, true, fake);
				float dt22 = c.tBreaking(nexcar, 0, false, fake);
				
				boolean fr = true;
				//System.out.println(c.getNom() + " " +dt2 + " " +dt22);
				if (dt2 == -1 || (dt2 > dt22 && dt22 >= 0)) {
					dt2 = dt22;
					fr = false;
				}
				nom = nexcar.getNom();
				minDt2 = dt2;
				fre = fr;
				

			}
			
			if (minDt2 != -1) {
				if (minDt2 == 0) {
					return -1;
				}
				
				if (!fake) {
					if (!fre) {
						if (c.getSpeed() == 0) {
							c.stopTheCar();
						} else {
							c.startBreaking();
						}

						System.out.println("La voiture " + c.getNom() + " commence à freiner car la voiture " + nom
								+ " l'empeche d'avancer");
					} else {
						if (c.getSpeed() == 0) {
							c.startTheCar();
						} else {
							c.reAcc();
						}
						System.out.println("La voiture " + c.getNom() + " commence à accelerer ");
					}
				}
				if (minDt2 < 0 && fake) {
					System.err.println(c.getNom() + " err tkjrdhtkjhj2 " + minDt2 + " " + c.getSpeed() + " " +fre);
					System.exit(0);
				}
				return t + minDt2;
			} else {
				
				for (Car car : c.getOnRoad().getEnd().carssInNode(m, c.getOnRoad())) {
					
					float dt2 = c.tBreaking(car, 2, true, fake);
					float dt22 = c.tBreaking(car, 2, false, fake);

					boolean fr = true;

					if (dt2 == -1.0f || (dt2 > dt22 && dt22 >= 0)) {

						dt2 = dt22;
						fr = false;
					}
					if (dt2 == -1.0f) {
						if (!c.isRoule()) {
							return -1;
						}
						noO = true;
						//System.err.println(c.getNom() + " " +car.getNom());
						
					} else {
						
						if (!fake) {
							if (!fr) {

								if (c.getSpeed() == 0) {
									c.stopTheCar();
								} else {
									c.startBreaking();
								}
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
						
						if (dt2 <= 0) {
							System.err.println(c.getNom() + " err tkjrdhtkjhj2f " + dt2);
							System.exit(0);
						}
						return t + dt2;

					}
				}

			}
		
		
			if (!noO && !c.isRoule()) {

				if (!fake) {
					c.startTheCar();
					System.out.println("La voiture " + c.getNom() + " redemare");
				}
				return t + 0.0000001f;
			}
			//System.out.println(c.getNom() + " lkjqsdlkjdsq " +c.getdFromNode() );
			float d = c.getMaxSpeed() * ((float) (delta - t));
		
			
			if (c.getdFromNode() + d >= c.getOnRoad().getLength() || c.getdFromNode() >= c.getOnRoad().getLength()) {
				
				if (!c.estArrive() && c.getdFromNode() < c.getOnRoad().getLength()-0.1f) {
					
					float dleft = -1.0f;
					if (c.getSpeed() < c.getMaxSpeed() && !c.isBreaking()) {
						float tttt = c.calcAux(c.getOnRoad().getLength() - c.getdFromNode());

						
						dleft = c.dAcc(tttt);
						
						
					} else {
						dleft = c.getOnRoad().getLength() - c.getdFromNode() - c.dBreaking();
					}
					
					//System.out.println(c.getNom() + " " +c.verifyCheck() + " " +dleft);
					if (!(!c.verifyCheck() && dleft > 0 && dleft < 0.01f)) {
						
						if (dleft > 0 && fake) {

							c.setCheck(true);
							if (!fake) {
								System.err.println(c.getNom() + "Err 703" + dleft);
								System.exit(0);
								System.out.println("La voiture " + c.getNom() + " est arrivé à l'intersection"
										+ c.getOnRoad().getEnd().getNom());
							}

							float tleft = c.timeD(dleft,false);
							if (tleft <= 0 && fake) {
								System.err.println(c.getNom() + " err tkjrdhtkjhj2fff " + tleft);
								System.exit(0);
							}
							return t + tleft;

						} else if (c.verifyCheck() && !fake) {
							
							c.setPositionInitIntersection(c.getOnRoad().getEnd().roadToInt(c.getOnRoad()));
							c.setCheck(false);

							if (!(c.getOnRoad().getEnd().checkGo(c, m, false, c.getNextRoad(),
									c.getOnRoad().getLength() - c.getdFromNode(), t))) {
								if (!fake) {
									System.out.println(
											"La voiture " + c.getNom() + " doit s'arreter à cause de l'intersection");
									if (c.getSpeed() <= 0.1) {
										c.stopTheCar();
									} else {
										c.startBreaking();
									}
									return t + 0.0000001f;

								} else {

									return t + 0.0000001f;
								}
							} else {
								// System.out.println(dleft);
								System.out.println("LA voiture " + c.getNom() + " est bientot à l'intersection "
										+ c.getOnRoad().getEnd().getNom() + ". La voiture  peut passer");

							}
							return -1;
						}
					}
				}
				if (c.getOnRoad().getLength() - c.getdFromNode() < -3.0f) {
					System.err.println(c.getNom() + "dépassé" + (c.getOnRoad().getLength() - c.getdFromNode()));
					System.exit(0);
				}
				
				// la voiture est arriv�e
				if (!fake) {
					if (c.getOnRoad().getLength() - c.getdFromNode() >1.0f && !c.estArrive()) {
						System.err.println(c.getNom() + " pas dépassé" + (c.getOnRoad().getLength() - c.getdFromNode()));
						//System.exit(0);
						return -1;
					}
					Road avant = c.getOnRoad();
					if (c.goNext()) {
						System.out.println("La voiture " + c.getNom() + " est arrivé à "
								+ c.getOnRoad().getEnd().getNom() + ", à sa destination ; Element supr");
						c.setDestroy();
					} else {
						if (c.getSpeed() >= 0 && c.getSpeed() <= 0.1) {
							c.stopTheCar();

						}
						c.setPositionInitIntersection(c.getOnRoad().getStart().roadToInt(avant));
						c.setInIntersection(true);

						c.getOnRoad().getStart().checkGo(c, m, true, c.getOnRoad(), 0, t);

					}

				} else {
					if (c.getdFromNode() >= c.getOnRoad().getLength()) {
						return t + 0.000001f;
					}
				}
				float ffffff = c.timeD(c.getOnRoad().getLength() - c.getdFromNode(),false);
				if (ffffff <= 0 && fake) {
					System.err.println(c.getNom() + " err tkjrdhtkjhj2ffffdfdfdfdfd " + ffffff);
					System.exit(0);
				}
				return t + ffffff;

			} else {

				return -1;
			}

		}
	}
}
