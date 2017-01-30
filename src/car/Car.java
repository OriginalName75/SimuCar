package car;

import java.util.ArrayList;

import map.Itineraire;
import map.Map;
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
	
	
	public float getdAvantIntersection() {
		return dAvantIntersection;
	}

	public void setdAvantIntersection(float dAvantIntersection) {
		this.dAvantIntersection = dAvantIntersection;
	}

	private float tempsAcc() {

		// 27,7778/14 = (maxSPeed - speed)/x
		if (speed > 0) {
			return (maxSpeed - speed) * 14.0f / 27.7778f;
		} else {
			if (tempsMoyenReaction > currentReaction) {	
				return maxSpeed * 14.0f / 27.7778f + tempsMoyenReaction - currentReaction;
			} else {
				return maxSpeed * 14.0f / 27.7778f;
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
	
	private float tBreaking() {
		return speed * 10.0f / 27.7778f;
	}

	private float dBreaking(float t) {

		return speed * t - 27.7778f / 10.0f * t * t / 2.0f;
	}

	public float dBreaking() {
		return dBreaking(tBreaking());
	}

	private float dAcc(float t) {
		// 27,7778/14*t*t/2 = d - Vo*t;
		if (speed > 0) {
			return 27.7778f / 14.0f * t * t / 2.0f - speed * t;
		} else {
			if (t <= tempsMoyenReaction - currentReaction) {
				return 0.0f;
			} else {
				float newT = t;
				if (tempsMoyenReaction > currentReaction) {
					newT = t - (tempsMoyenReaction - currentReaction);
				}
				return (27.7778f / 14.0f) * newT * newT / 2.0f;
			}

		}
	}

	public void breaking(float t) {
		if (speed > 0) {
			float nextspeed = speed - 27.7778f * t / 10.0f;

			if (nextspeed <= 0) {
				float newt = speed * 10.0f / 27.7778f;
				dFromNode = dFromNode + dBreaking(newt);
				System.out.println(
						getNom() + " EST ARRETE | route : " + getOnRoad().getName() + " | distnce : " + getdFromNode());
				stopTheCar();

			} else {

				System.out.println(
						getNom() + " FREINE | route : " + getOnRoad().getName() + " | distnce : " + getdFromNode());
				dFromNode = dFromNode + dBreaking(t);
				speed = nextspeed;

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
	
	public float tBreaking(Car r, int plus, boolean acce) {
		float di = 0;

		if (r.isRoule()) {
			if (!isRoule()) {
				
				if (!acce) {
					return -1;
				}else {
					return 0.00001f;
					
				}
				
			} else if (isBreaking()) {
				
				if (!acce) {
					return -1;
				}else {
					System.out.println("cas pas fait");
					return -1;
				}
				
			} else {
				
				float ddd = 0;
				if (r.isBreaking()) {
					if (!acce) {
						float dd = r.dBreaking();
	
						if (plus==0) {
							ddd = getOnRoad().getLength() - getdFromNode() - dBreaking();
						} else if (plus==1) {
							ddd = r.getdAvantIntersection() + dd - dBreaking() - r.getLenght() / 2 - getLenght() / 2;
						
						} else {
							ddd = r.getdFromNode() + dd - dBreaking() - r.getLenght() / 2 - getLenght() / 2;
	
						}
						if (ddd < 0) {
							ddd = 0;
							System.out.println("erreur");
						}
						if (getMaxSpeed() == getSpeed()) {
							return ddd / getSpeed();
						} else {
							return tempsParcourue(ddd);
						}
					}else {
						System.out.println("cas pas fait 2");
						return -1;
					}
					
					
				} else if (getSpeed() <= r.getSpeed()) {
					
					if (acce) {
						return -1;
					}
					if (getSpeed() == r.getSpeed() && getSpeed() == 0 && tempsMoyenReaction > currentReaction) {

						if (getCurrentReaction() > r.getCurrentReaction()) {
							if (plus==0) {
								if (tempsParcourue(
										getOnRoad().getLength() - getdFromNode() - dBreaking()) < tempsMoyenReaction
												- r.getCurrentReaction()) {
									currentReaction = r.getCurrentReaction();
									System.out.println("cas pas sur 1");
									
									
									
									return 0.00001f; // pas sur
									
								} else {
									return -1;
								}
							} else if (plus==1) {
								if (r.getdAvantIntersection() + getCurrentReaction() - r.getLenght() / 2
										- getLenght() / 2 < tempsMoyenReaction - r.getCurrentReaction()) {
									currentReaction = r.getCurrentReaction();
									System.out.println("cas pas sur 2");
									return 0.00001f; // pas sur
								
								} else {
									return -1;
								}
							} else {

								if (r.getdFromNode() + getCurrentReaction() - r.getLenght() / 2
										- getLenght() / 2 < tempsMoyenReaction - r.getCurrentReaction()) {
									currentReaction = r.getCurrentReaction();
									System.out.println("cas pas sur 2");
									return 0.00001f; // pas sur
								
								} else {
									return -1;
								}
							}
						} else {
							return -1;
						}
					} else {
						return -1.0f;
					}
					
					
				} else {
					if (acce) {
						return -1;
					}
					if (plus==0) {
						ddd = getOnRoad().getLength() - getdFromNode();
					} else if (plus==1) {
						ddd = r.getdAvantIntersection() - getdFromNode() - r.getLenght() / 2 - getLenght() / 2;
					} else {
						ddd = r.getdFromNode() - getdFromNode() - r.getLenght() / 2 - getLenght() / 2;

					}
					float ttt = ddd / (getSpeed() - r.getSpeed());
					if (ttt <= tempsAcc()) {
						float tttt = tBreaking();
						if (tttt > ttt) {
							return 0.00001f;
						} else {
							return ttt - tttt;
						}

					} else {
						return -1;
					}
					
				}
				
				
			}
		} else {
			if (acce) {
				return -1;
			}
			if (isBreaking()) {
				return -1.0f;
			} else {
				if (plus==0) {

					di = getOnRoad().getLength() - getdFromNode() + r.getdIntersection() - r.getLenght() / 2
							- getLenght() / 2;
				} else if (plus==1) {
					di = r.getdAvantIntersection() - getdFromNode() - r.getLenght() / 2 - getLenght() / 2;
				} else {
					di = r.getdFromNode() - getdFromNode() - r.getLenght() / 2 - getLenght() / 2;
				}
				float dd = dBreaking();

				if (di - dd < 0) {

					return -1.0f;
				}

				if (speed < maxSpeed) {
					return tempsParcourue(di - dd);
				} else {

					return (di - dd) / speed;

				}
			}
			

		}

	}

	public float tempsParcourue(float d) {
		if (speed < maxSpeed) {
			if (speed > 0) {
				float deltaSQRT = (float) Math.sqrt(speed * speed + 4 * (27.7778f / 28.0f) * d); // disrimint
				return (-speed + deltaSQRT) / (2.0f * 27.7778f / 28.0f);
			} else {
				if (tempsMoyenReaction > currentReaction) {
					return ((float) Math.sqrt(2.0f * d * 14.0f / 27.7778f)) + tempsMoyenReaction - currentReaction;
				} else {
					return ((float) Math.sqrt(2.0f * d * 14.0f / 27.7778f));
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
			float[] rep = { dBreaking(deltaT),  vitesseBreak(deltaT)};
			return rep;
		}
		if (speed < maxSpeed) {
			float t = tempsAcc();
			if (t <= deltaT) {
				float[] rep = { dAcc(t) + getSpeed() * (deltaT - t), maxSpeed };
				return rep;
			} else {
				float[] rep = { dAcc(deltaT), vitesseAcc(deltaT) };
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
	}
	public void reAcc() {
		isBreaking = false;
		roule=true;
	}
	public boolean goNext() {

		if (itineraire.estArrive()) {
			return true;
		} else {
			String nomAvant = onRoad.getEnd().getNom();

			positionInitIntersection = onRoad.getEnd().roadToInt(onRoad);
			onRoad = itineraire.first();

			itineraire.goNext();
			System.out.println("La voiture " + nom + " est rrivé à " + nomAvant + " va maitenant dans la route "
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

	public ArrayList<Car> voituresDevant(Map p) {
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
}
