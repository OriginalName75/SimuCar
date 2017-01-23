package sim;

import java.util.ArrayList;

import car.Car;
import map.Map;

public class SimuCar {

	public void simulateCars(float dt, ArrayList<Car> cars) {
		for (Car car : cars) {
			if (car.isInIntersection()) {
			
				if (car.isRoule()) {
					float[] d3 = car.distanceParcourue(dt);
					car.setSpeed(d3[1]);
					car.setdIntersection(car.getdIntersection() + d3[0]);

					car.continueC(dt);
					
					System.out.println(car.getNom() + " | roule dans l'inteersection : "
							+ car.getOnRoad().getStart().getNom() + " d :" + car.getdIntersection()+ " vitesse " +car.getSpeed());
					
					
				} else {
					System.out.println(car.getNom() + " | attend son tour dans l'inteersection : "
							+ car.getOnRoad().getStart().getNom() + " d :" + car.getdIntersection()+ " vitesse " +car.getSpeed());
				}
				

			} else {
				
				if (car.isRoule()) {
					
					if (car.isBreaking()) {
						car.breaking(dt);
						
					}else {
						float[] d3 = car.distanceParcourue(dt);
						car.setdFromNode(car.getdFromNode() + d3[0]);
						car.setSpeed(d3[1]);
						if (d3[1]==0.0f) {
							System.out.println(car.getNom() + " | va BIENTOT accélerer : "
									+ car.getOnRoad().getName() + " d :" + car.getdIntersection()+ " dans " +car.getCurrentReaction());
								
						}else {
							System.out.println(car.getNom() + " | route : " + car.getOnRoad().getName() + " | distance : "
									+ car.getdFromNode() + " vitesse " +car.getSpeed());
						}
						
					}

				} else {
					System.out.println(car.getNom() + " | route : " + car.getOnRoad().getName() + " | distnce : "
							+ car.getdFromNode() + " | est arr�t�");
				}

			}
		}

	}

	public float simIna(float delta, float t, Car c, boolean fake, Map m) {
		
		
		if (c.isInIntersection()) {
			
			if (!c.isRoule()) {

				if (c.getOnRoad().getStart().checkGo(c, m, !fake,c.getOnRoad(),0)) {
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

					return t + c.tempsParcourue(d2);

				} else {

					return -1;
				}
			}

		} else {
			
			float minDt2 = -1;
			String nom = "";
			boolean fre = true;
			Car nexcar = c;
			boolean find=false;
			boolean isFirst=true;
			for (Car car : c.voituresDevant(m)) {
				isFirst=false;
				if (minDt2 == -1) {
					minDt2=car.getdFromNode()-c.getdFromNode();
					nexcar=car;
					find=true;
				}else {
					if (minDt2 > car.getdFromNode()-c.getdFromNode()) {
						minDt2=car.getdFromNode()-c.getdFromNode();
						nexcar=car;
						find=true;
					}
				}
				
				

			}
			if (find && (!nexcar.isRoule() || nexcar.getSpeed() != nexcar.getMaxSpeed())) {
				
				float dt2 = c.tBreaking(nexcar, 0, true,fake);
				float dt22 = c.tBreaking(nexcar, 0, false,fake);
				
				boolean fr = true;
				//System.out.println(c.getNom() + " " +dt2 + " " +dt22);
				if (dt2==-1 || (dt2 > dt22 && dt22>=0)) {
					dt2 = dt22;
					fr = false;
				}
				nom = nexcar.getNom();
				minDt2=dt2;
				fre=fr;
				
			}
			if (minDt2 != -1) {
				if (minDt2 == 0) {
					return -1;
				}
				if (!fake) {
					if (!fre) {
						c.setBreaking(true);
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
			
				
				return t + minDt2;
			} else {
				
				for (Car car : c.getOnRoad().getEnd().carssInNode(m, c.getOnRoad())) {
					
					float dt2 = c.tBreaking(car, 2, true,fake);
					float dt22 = c.tBreaking(car, 2, false,fake);
					
					boolean fr = true;
					if (dt2 > dt22) {
						dt2 = dt22;
						fr = false;
					}
					if (dt2 == -1.0f) {
						//System.out.println(c.getNom() + " Cas pas sur");
						
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
			//System.out.println(c.getNom() + " lkjqsdlkjdsq " + c.getdFromNode() );
			float d = c.getMaxSpeed() * ((float) (delta - t));
			if (c.getdFromNode() + d >= c.getOnRoad().getLength() || c.getdFromNode()  >= c.getOnRoad().getLength()) {
				
				if (!isFirst) {
					return -1;
				}
				if (!c.estArrive()) {
					float dleft = c.getOnRoad().getLength() - c.getdFromNode() - c.dBreaking();
					
					
					if (dleft>=0) {
						
						c.setCheck(true);
						if (!fake) {
							System.err.println("Err 703");
							System.exit(0);
							System.out.println("La voiture " + c.getNom() + " arrive à l'intersection" +c.getOnRoad().getEnd().getNom() );
						}
						
						float tleft=c.timeD(dleft);
						return t +tleft;
						
					}else if (c.verifyCheck() && dleft<=0 && !fake) {
					
						
						c.setCheck(false);
						
						c.setPositionInitIntersection(c.getOnRoad().getEnd().roadToInt(c.getOnRoad()));
						if (!(c.getOnRoad().getEnd().checkGo(c, m, false, c.getNextRoad(),c.getOnRoad().getLength() - c.getdFromNode()))) {
							if (!fake) {
								System.out.println("La voiture " + c.getNom() + " doit s'arreter à cause de l'intersection");
								c.startBreaking();
								return t +0.0000001f;
								
							}else {
								
								return t + 0.0000001f;
							}
						}else {
							System.out.println("LA voiture "+ c.getNom() + " est bientot à l'intersection "+ c.getOnRoad().getEnd().getNom()+ ". La voiture  peut passer" );
							
						
						}
						return -1;
					}
				}
				
				// la voiture est arriv�e
				if (!fake) {

					if (c.goNext()) {
						System.out.println("La voiture " + c.getNom() + " est arrivé � "
								+ c.getOnRoad().getEnd().getNom() + ", à sa destination ; Element supr");
						c.setDestroy();
					} else {
					
						c.setInIntersection(true);

						c.getOnRoad().getStart().checkGo(c, m, true, c.getOnRoad(),0);

					}

				}else {
					if (c.getdFromNode() > c.getOnRoad().getLength()) {
						return t + 0.000001f;
					}
				}
				return t + c.timeD(c.getOnRoad().getLength() - c.getdFromNode()) ;

			} else {

				return -1;
			}

		}
	}
}
