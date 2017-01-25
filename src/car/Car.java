package car;

import java.util.ArrayList;

import map.Itineraire;
import map.Carte;
import map.Road;

public class Car {
	protected Road onRoad;
	protected float speed;
	protected float dFromNode;
	protected String nom;
	protected int lenght;
	protected Itineraire itineraire;
	protected boolean hasAct;
	protected boolean destroy;
	protected float maxSpeed;
	protected boolean roule;
	protected float dIntersection;
	protected int positionInitIntersection;
	protected float tempsMoyenReaction;
	protected float currentReaction;
	protected boolean isBreaking;
	protected float dAvantIntersection;
	protected boolean checked;

	public boolean verifyCheck() {
		return checked;
	}

	public void setCheck(boolean arriver) {
		this.checked = arriver;
	}

	public float getdAvantIntersection() {
		return dAvantIntersection;
	}

	public void setdAvantIntersection(float dAvantIntersection) {
		this.dAvantIntersection = dAvantIntersection;
	}

	private float tempsAcc() {
		float acce = 100.0f / (1.4f * 36.0f);
		// 27,7778/14 = (maxSPeed - speed)/x
		if (speed > 0) {
			return (maxSpeed - speed) / acce;
		} else {
			if (tempsMoyenReaction > currentReaction) {
				return maxSpeed / acce + tempsMoyenReaction - currentReaction;
			} else {
				return maxSpeed / acce;
			}

		}

	}

	private float vitesseBreak(float t) {

		// 27,7778/14 = (x - speed)/t
		if (speed > 0) {
			return -10.0f * t / 3.6f + speed;
		} else {
			return 0;

		}
	}

	private float vitesseAcc(float t) {

		// 27,7778/14 = (x - speed)/t
		if (speed > 0) {
			return 27.7778f * t / 14.0f + speed;
		} else {
			if (t <= tempsMoyenReaction - currentReaction) {
				return 0.0f;
			} else {
				if (tempsMoyenReaction > currentReaction) {
					return 27.7778f * (t - (tempsMoyenReaction - currentReaction)) / 14.0f;
				} else {
					return 27.7778f * t / 14.0f;
				}
			}

		}
	}

	public float tBreaking(float d) {

		if (speed == 0) {

			System.err.println(getNom() + " Erreur 777");
			System.exit(0);
		}
		float desc = 100.0f / (36.0f);
		float deltaSQRT = (float) Math.sqrt(speed * speed + 4 * (desc) * d); // disrimint

		float tt = (speed + deltaSQRT) / (desc);
		if (tt < 0) {
			System.err.println("Erreur 7778 : " + tt);
			System.exit(0);
		}
		return tt;
	}

	public float tBreaking() {
		float desc = 100.0f / (36.0f);
		return speed / desc;
	}

	private float dBreaking(float t) {
		float desc = 100.0f / (36.0f);
		return speed * t - desc * t * t / 2.0f;
	}

	public float dBreakingMAx() {

		// desc*speed /desc*speed /desc = d - speed *speed/desc
		// d = speed*speed/(2*desc) + speed*speed/desc
		float desc = 100.0f / (36.0f);
		return getMaxSpeed() * getMaxSpeed() / (2 * desc);
		// return dBreaking(tBreaking());
	}

	public float dBreaking() {

		// desc*speed /desc*speed /desc = d - speed *speed/desc
		// d = speed*speed/(2*desc) + speed*speed/desc
		float desc = 100.0f / (36.0f);
		return getSpeed() * getSpeed() / (2 * desc);
		// return dBreaking(tBreaking());
	}

	public float dAcc(float t) {
		float acce = 100.0f / (1.4f * 36.0f);
		// 27,7778/14*t*t/2 = d - Vo*t;
		if (speed > 0) {
			return acce * t * t / 2.0f + speed * t;
		} else {
			if (t <= tempsMoyenReaction - currentReaction) {
				return 0.0f;
			} else {
				float newT = t;
				if (tempsMoyenReaction > currentReaction) {
					newT = t - (tempsMoyenReaction - currentReaction);
				}
				return acce * newT * newT / 2.0f;
			}

		}
	}

	public void breaking(float t) {
		float desc = 100.0f / (36.0f);
		if (speed > 0) {

			float nextspeed = speed - desc * t;

			if (nextspeed <= 0) {
				nextspeed=0;
				float newt = speed / desc;
				dFromNode = dFromNode + dBreaking(newt);
				System.out.println(
						getNom() + " EST ARRETE | route : " + getOnRoad().getName() + " | distnce : " + getdFromNode());
				stopTheCar();

			} else {
				dFromNode = dFromNode + dBreaking(t);
				speed = nextspeed;
				System.out.println(getNom() + " FREINE | route : " + getOnRoad().getName() + " | distnce : "
						+ getdFromNode() + " vitesse " + getSpeed());

			}

		} else {
			System.out.println(
					getNom() + " EST ARRETE | route : " + getOnRoad().getName() + " | distnce : " + getdFromNode());
		}
	}

	public void setBreaking(boolean isBreaking) {
		this.isBreaking = isBreaking;
	}

	public float getCurrentReaction() {
		return currentReaction;
	}

	public float timeD(float d, boolean force) {

		// Cas où on freine et la voiture devant accélère

		if (isBreaking) {

			return tBreaking(d);
		} else if (!isRoule()) {
			if (force) {
				return tempsParcourue(d);
			}
			return -1;

		} else if (getSpeed() < getMaxSpeed()) {

			return tempsParcourue(d);

		} else {
			return d / getSpeed();
		}
	}

	private float colision(Car r, float Dinit, boolean acc, boolean fake) {
		float acce = 100.0f / (1.4f * 36.0f);
		float desc = 100.0f / (36.0f);
		
		// Cas où on freine et la voiture devant accélère
		
		if (isBreaking) {
			
			if (!acc) {
				return -1;
			}

			if (r.isBreaking() || !r.isRoule()) {

				return -1;

			} else {
				
				float dbreak = speed * speed / (desc * 2);

				if (dbreak <= Dinit) {

					if (Dinit - dbreak < 1.0f) {
						if (r.getSpeed() == 0.0f) {
							if (tempsMoyenReaction - r.getCurrentReaction()<0) {
								if (fake) {
									
									System.err.println(getNom() + " Erreur kjhj ");
									System.exit(0);
								}
								
							}
							return 0.00000001f + tempsMoyenReaction - r.getCurrentReaction();
						}
					}
					return 1.0f;
				}

				float tvMax = (getMaxSpeed() - speed) / acce;
				float delta = r.getSpeed() * r.getSpeed() - 2 * acce * (Dinit - (getSpeed() * getSpeed()) / (desc * 2));
				if (delta<0 && fake) {
					
					System.err.println(getNom() + " " +r.getNom() + " err tkjrj2dsdsdsdsgfgfgfg " + " " +Dinit + " " + delta+ " " + tvMax);
					System.exit(0);
					
				}
				float t = (-r.getSpeed() + (float) Math.sqrt(delta)) / acce;
				if (r.getSpeed() == 0) {
					t = t - (tempsMoyenReaction - r.getCurrentReaction());
					if (t <= 0 && !fake) {
						System.err.println("Erreur 50°50505050");
						return -1f;

					}
				}
				if (t <= tvMax) {
					if (t <= 1.0f) {
						return 1.0f;
					}

					return t;
				} else {
					float dA = tvMax * tvMax * acce / 2 + r.getSpeed() * tvMax + Dinit - getMaxSpeed() * tvMax;
					t = (speed * speed / (2 * desc) - dA) / getMaxSpeed();
					if (r.getSpeed() == 0) {
						t = t - (tempsMoyenReaction - r.getCurrentReaction());
						if (t <= 0 && !fake) {
							System.err.println("Erreur 50°5050505g0");
							return -1f;

						}
					}
					if (t <= 1.0f) {
						return 1.0f;
					}
					return t;

				}

			}
		} else if (!isRoule()) {
			
			if (!acc) {
				return -1;
			}
			if (r.isBreaking() || !r.isRoule()) {
				return -1;

			} else {
				return 0.000001f;
			}

		} else if (getSpeed() < getMaxSpeed()) {
			
			if (acc) {
				return -1;
			}

			if (r.isBreaking() || !r.isRoule()) {
				
				if (getSpeed() == 0) {
					if (tempsMoyenReaction - getCurrentReaction()<0) {
						if (fake) {
							
							System.err.println(getNom() + " Erreur kfjhj " + getCurrentReaction());
							System.exit(0);
						}
						
					}
					return tempsMoyenReaction - getCurrentReaction() + 0.0000001f;// a
																					// opti
				}
				
				float t = aux1(r, Dinit, fake);
				if (getNom().equals("v 57")) {
					System.err.println(t);
				}
				if (t == -1) {
					return -1;
				}
				if (t<=0 && t>=-0.1f) {
					return 0.00000001f;
				}
				if (t <= 0 && fake) {
					
					System.err.println(getNom() + " Erreur 5065065fff60 " + t + " " +r.getNom() + " " +r.getSpeed());
					System.exit(0);
				}
				return t;
			} else if (r.getSpeed() < r.getMaxSpeed()) {
				
				if (getSpeed() == 0) {
					return -1;// a opti
				}
				float t = frein(r, Dinit);
				if (t == -1) {
					return -1;
				}
				if (t <= 0 && fake) {
					System.err.println(
							getNom() + " Erreur 50650sdfsdfsdfsdfsdf6560 " + t + " " + r.getNom() + " " + Dinit);

					System.exit(0);
				}
				return t;

			} else {
				return -1;
			}

		} else {
			
			if (acc) {
				
				return -1;
			}
			if (r.isBreaking() || !r.isRoule()) {
				
				float t = aux1(r, Dinit,fake);
				if (t == -1) {
					return -1;
				}
				if (t <= 0 && t>-0.1) {
					return 0.00000001f;
				}
				if (t <= 0 && fake) {

					System.err.println(getNom() + " Erreur 50650656jh0 " + t + " " + r.getNom() + " " + Dinit);
					System.exit(0);
				}

				return t;
			} else if (r.getSpeed() < r.getMaxSpeed()) {
				
				float t = frein(r, Dinit);
				
				if (t == -1) {
					return -1;
				}
				if (t <= 0 && fake) {
					System.err.println(getNom() + " Erreur 50650656fffffsdf0" + t + " " + r.getNom() + " " + Dinit);
					System.exit(0);
				}
				
				return t;
			} else {
				return -1;
			}

		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////

	private float frein(Car r, float Dinit) {
		float acc = 100.0f / (1.4f * 36.0f);

		float TVmaxA = (getMaxSpeed() - getSpeed()) / acc;

		float TVmaxB = (getMaxSpeed() - r.getSpeed()) / acc;

		float XVmaxA = TVmaxA * (getMaxSpeed() + getSpeed()) / 2;
		float XVmaxB = TVmaxB * (r.getMaxSpeed() + r.getSpeed()) / 2;
		// System.out.println(getNom() + " XVmaxA " +XVmaxA );
		// System.out.println(getNom() + " XVmaxB " +XVmaxB );
		if (r.getSpeed() >= getSpeed()) {
			if (r.getSpeed() == 0 && getSpeed() == 0) {
				if (r.getCurrentReaction() < getCurrentReaction()) {
					return 0.0000001f;
				}

			}
			
			return -1; // En gros la voiture de devant va plus vite donc on
						// accélère jusqu'à atteindre la vitesse max et attendre
						// un pochain évènement.
		} else {

			float Dfinal = XVmaxB - XVmaxA - Dinit;
			// System.out.println("lkdjsqldkjqlksd " + Dfinal);
			if (Dfinal > 35.0f) {
				
				return -1; // Pareil on ne fait rien
			} else {
				return 0.000001f;
			}
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	// cas ou la voiture de devant freine

	private float aux1(Car r, float Dinit, boolean fake) {
		
		float desc = 100.0f / (36.0f);
		float dFrB = r.getSpeed() * r.getSpeed() / (2 * desc);
		float dFrA = getSpeed() * getSpeed() / (2 * desc);
		if (!isBreaking()) { // a opti
			dFrA = getMaxSpeed() * getMaxSpeed() / (2 * desc);
		}
		float ddd=Dinit - 1.0f + dFrB - dFrA  - r.getLenght();
		if (getNom().equals("v 22")) {
			System.err.println(Dinit + " " +dFrA + " " +dFrB+ " "  + " " +ddd);
		}
		if (ddd > -0.1f && ddd<0) {
			return 0.000001f;
		}
		if (ddd < -0.1f && fake) {
			if (!isBreaking()) { 
				return 0.000001f;// a opti
			}
			System.err.println(getNom() + " " +r.getNom() + " err tkjrdhtkjhj2dsdsdsdsgfgfgfg " + " " +Dinit + " " +dFrA + " " +dFrB+ " "  + " " +ddd);
			System.exit(0);
		}
		float rep = timeD(ddd,false);
		if (rep < -0.1f && fake) {
			System.err.println(getNom() + " " +r.getNom() + " err tkjrdhtkjhj2dsdsdsds " + " " +Dinit + " " +dFrA + " " +dFrB+ " " +rep + " " +ddd);
			System.exit(0);
		}
		
		return rep; // apres voir avec
															// plus 1

	}

	public float tBreaking(Car r, int plus, boolean acce, boolean fake) {
		float ddd = 0;
		
		if (plus == 0) {
			ddd = r.getdFromNode() - getdFromNode() - r.getLenght() / 2 - getLenght() / 2;
		} else {
			ddd = getOnRoad().getLength() - getdFromNode() - r.getLenght() / 2 - getLenght() / 2;

		}

		return colision(r, ddd, acce, fake);

	}

	public float tempsParcourue(float d) {
		
		float acc = 100.0f / (1.4f * 36.0f);
		if (speed < maxSpeed) {
			if (speed > 0) {
				float deltaSQRT = (float) Math.sqrt(speed * speed + 4 * (acc) * d / 2.0f); // disrimint
				if (deltaSQRT<0) {
					
					System.err.println(getNom() +  " err tkjrjdsdsgfgfgfg " + " " +deltaSQRT);
					System.exit(0);
					
				}
				return (-speed + deltaSQRT) / acc;
			} else {
				if (tempsMoyenReaction > currentReaction) {
					if (d<-0.1) {
						
						
						System.err.println(getNom() +  " err tkjrjsgfgfgfg " + " " +d);
						System.exit(0);
							
						
					}
					return ((float) Math.sqrt(2.0f * d * (1 / acc))) + tempsMoyenReaction - currentReaction;
				} else {
					if (d<-0.1) {
						
						
						System.err.println(getNom() +  " err tkjrjsgfgfgfg " + " " +d);
						System.exit(0);
							
						
					}
					return ((float) Math.sqrt(2.0f * d * (1 / acc)));
				}
			}
		} else {
			return d / speed;
		}
	}

	public void continueC(float t) {
		if (speed == 0.0f && !isBreaking) {
			currentReaction = t;
		}
	}

	public float[] distanceParcourue(float deltaT) {
		if (isBreaking) {
			float[] rep = { dBreaking(deltaT), vitesseBreak(deltaT) };
			return rep;
		}
		if (speed < maxSpeed) {
			float t = tempsAcc();
			if (t <= deltaT) {

				if (speed == 0) {
					float[] rep = { dAcc(t) + getSpeed() * (deltaT - t), maxSpeed };
					currentReaction += deltaT;
					return rep;
				} else {
					float[] rep = { dAcc(t) + getSpeed() * (deltaT - t), maxSpeed };

					return rep;
				}

			} else {
				if (vitesseAcc(deltaT)<speed) {
					System.err.println("Err 333" + getNom() + " " + speed + " " + deltaT  + " " + vitesseAcc(deltaT) );
					System.exit(0);
				}
				float[] rep = { dAcc(deltaT), vitesseAcc(deltaT) };
				if (speed == 0) {
					currentReaction += deltaT;
				}
				return rep;
			}

		} else {
			float[] rep = { getSpeed() * deltaT, maxSpeed };
			return rep;

		}
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	// intersection
	protected boolean isInIntersection;

	public float getdIntersection() {
		return dIntersection;
	}

	public void setdIntersection(float dIntersection) {
		this.dIntersection = dIntersection;
	}

	public int getLenght() {
		return lenght;
	}

	///

	public int getPositionInitIntersection() {
		return positionInitIntersection;
	}

	public void startBreaking() {
		isBreaking = true;
		roule=true;
	}

	public void setPositionInitIntersection(int positionInitIntersection) {
		this.positionInitIntersection = positionInitIntersection;
	}

	public void reAcc() {
		isBreaking = false;
		roule = true;
	}

	public Road getNextRoad() {
		return itineraire.first();
	}

	public boolean estArrive() {
		return itineraire.estArrive();
	}

	public boolean goNext() {

		if (itineraire.estArrive()) {
			return true;
		} else {
			String nomAvant = onRoad.getEnd().getNom();

			positionInitIntersection = onRoad.getEnd().roadToInt(onRoad);
			onRoad = itineraire.first();

			itineraire.goNext();
			System.out.println("La voiture " + nom + " est arrivé à " + nomAvant + " va maitenant dans la route "
					+ onRoad.getName());
			dFromNode = 0;
			return false;
		}
	}

	public void stopTheCar() {
		setSpeed(0);
		roule = false;
		currentReaction = 0;
		isBreaking = false;

	}

	public boolean isRoule() {
		return roule;
	}

	public void startTheCar() {
		currentReaction = 0;
		isBreaking = false;
		roule = true;
	}

	public Car(String nom, Road onRoad, float speed) {
		super();
		destroy = false;
		this.onRoad = onRoad;
		this.speed = speed;
		this.dFromNode = 0;
		isInIntersection = false;
		this.nom = nom;
		lenght = 4;
		this.hasAct = true;
		itineraire = new Itineraire();
		maxSpeed = speed;
		roule = true;
		tempsMoyenReaction = 2;
		currentReaction = 0;
		isBreaking = false;
	}

	public boolean isBreaking() {
		return isBreaking;
	}

	public boolean isInIntersection() {
		return isInIntersection;
	}

	public void setInIntersection(boolean isInIntersection) {
		if (isInIntersection) {
			dIntersection = 0;
		}
		this.isInIntersection = isInIntersection;
	}

	public Itineraire getItineraire() {
		return itineraire;
	}

	public void setItineraire(Itineraire itineraire) {
		this.itineraire = itineraire;
	}

	public void addRoaadToIt(Road r) {
		itineraire.add(r);
	}

	public void setOnRoad(Road onRoad) {
		this.onRoad = onRoad;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setdFromNode(float dFromNode) {
		this.dFromNode = dFromNode;
	}

	public float getSpeed() {
		return speed;
	}

	public String getNom() {
		return nom;
	}

	public void setDestroy() {
		this.destroy = true;
	}

	public boolean isDestroy() {
		return destroy;
	}

	public ArrayList<Car> voituresDevant(Carte p) {
		ArrayList<Car> cars = new ArrayList<Car>();

		ArrayList<Car> others = getOnRoad().carsInRoad(p);
		for (Car car : others) {
			if (!car.equals(this)) {
				if (car.getdFromNode() > getdFromNode()) {
					cars.add(car);

				}
			}
		}

		return cars;
	}

	public boolean percution(Car car) {

		return onRoad.equals(car.getOnRoad()) && dFromNode + lenght / 2 >= car.getdFromNode() - car.getLenght() / 2
				&& dFromNode - lenght / 2 <= car.getdFromNode() + car.getLenght() / 2;

	}

	public float getdFromNode() {
		return dFromNode;
	}

	public Road getOnRoad() {
		return onRoad;
	}
	public float calcAux(float d) {
		float acce = 100.0f / (1.4f * 36.0f);
		float desc = 100.0f / (36.0f);
		float c = getSpeed()*getSpeed()/(2*desc) - d;
		float a=acce*acce/(2.0F*desc)+acce/2.0f;
		float b = acce*getSpeed()/desc + getSpeed();
		float delta = b*b - 4*a*c;
		if (delta<=0) {
			System.err.println(getNom() + "delta neg");
			System.exit(1);
		}
		float rep = (-b+(float) Math.sqrt(delta))/(2.0f*a);
		
		return rep;
		
	}
}
