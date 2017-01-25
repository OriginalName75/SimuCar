/**
 * 
 */
package node;

import java.util.ArrayList;
import java.util.Iterator;

import car.Car;
import inter.InterToRoad;
import map.Carte;
import map.Road;

/**
 * @author Original Name
 *
 */
public class Intersection extends Node {
	protected Road[] roads;
	protected InterToRoad[] inters;
	protected ArrayList<ArrayList<Car>> occupied;

	protected float d;

	public void setInter(int i, InterToRoad r) {
		inters[i] = r;
	}

	public Intersection(String nom) {
		super(nom);
		roads = new Road[8];
		this.d = 10;
		occupied = new ArrayList<ArrayList<Car>>();
		for (int i = 0; i < 4; i++) {
			occupied.add(new ArrayList<Car>());
		}

		inters = new InterToRoad[8];
		for (int i = 0; i < inters.length; i++) {
			inters[i] = new InterToRoad();
		}

	}

	public int roadToInt(Road r) {
		int i = 0;
		for (Road road : roads) {
			if (r.equals(road)) {
				return i;
			}
			i++;
		}
		System.err.println("Erreur 767TE");
		System.exit(1);
		return -1;
	}

	public ArrayList<Car> carssInNode(Carte m, Road ro) {
		ArrayList<Car> r = new ArrayList<Car>();
		for (Car c : m.getCars()) {
			try {
				if (c.isInIntersection() && c.getOnRoad().getStart().equals(this)
						&& roads[c.getPositionInitIntersection()].equals(ro) && c.getOnRoad().getStart().equals(this))
					;
			} catch (Exception e) {
				System.err.println(
						"Erreur 70007070 " + c.getNom() + " " + getNom() + " " + c.getPositionInitIntersection());
				System.exit(1);
			}
			if (c.isInIntersection() && c.getOnRoad().getStart().equals(this)
					&& roads[c.getPositionInitIntersection()].equals(ro) && c.getOnRoad().getStart().equals(this)) {
				r.add(c);
			}
		}
		return r;
	}

	public boolean checkGo(Car r, Carte m, boolean in, Road nextRoad, float plusD, float t) {
		
		int entree = connexion(r.getPositionInitIntersection());
		int sortie = connexion(roadToInt(nextRoad));

		if (inters[r.getPositionInitIntersection()].isTraffic()) {

			float tt = t;
			if (plusD > 0) {
				r.timeD(plusD, false);
			}
			if (!trafficSim(tt, r.getPositionInitIntersection())) {
				return false;
			}

		}
		if (inters[r.getPositionInitIntersection()].hasToStop() && plusD > 0) {
			return false;
		}
	
		/*
		 * 
		 * 
		 * 
		 * il veut tourner � droite
		 * 
		 * 
		 */
		if (entree == sortie) {
			

			if (occupied.get(entree).size() != 0 && !occupied.get(entree).contains(r)) {
				for (Car ccc : occupied.get(entree)) {
					if (!checkOccupation(ccc, r, in)) {
						return false;
					}

				}

			}
			// on regarde si destination bloguant
			if (!destinationBloquante(r, in, m)) {
				return false;
			}

			// onregardesiprioritaire
			if (!inters[r.getPositionInitIntersection()].asPrioriteDroite()) {

				// si ps prioritaire onregarde � gauche
				int par = parrallele(roadToInt(r.getOnRoad()));
				if (inters[par].asPrioriteDroite() && !obstacle(r, d / 2, in, m, par, plusD)) {
					return false;
				}
				if (in) {
					if (!r.isRoule()) {
						r.startTheCar();
					}
					System.out.println(r.getNom() + " peut passer");
					occupied.get(entree).add(r);

				}
				return true;
			} else {
				if (in) {
					if (!r.isRoule()) {
						r.startTheCar();
					}
					System.out.println(r.getNom() + " peut passer");
					occupied.get(entree).add(r);

				}
				return true;

			}
		} else {
			/*
			 * 
			 * 
			 * 
			 * il veut aller tout droit
			 * 
			 * 
			 */

			if (closeTo(entree, sortie)) {
				
				if ((occupied.get(entree).size() != 0 && !occupied.get(entree).contains(r))
						|| (occupied.get(sortie).size() != 0 && !occupied.get(sortie).contains(r))) {

					for (Car ccc : occupied.get(entree)) {
						if (!checkOccupation(ccc, r, in)) {
							return false;
						}
					}
					for (Car ccc : occupied.get(sortie)) {
						if (!checkOccupation(ccc, r, in)) {
							return false;
						}
					}

				}
				if (!destinationBloquante(r, in, m)) {
					return false;
				}

				// onregardesiprioritaire
				if (!inters[r.getPositionInitIntersection()].asPrioriteDroite()) {

					// on regarde � droite

					if (!obstacle(r, d, in, m, droite(r.getPositionInitIntersection()), plusD)) {

						return false;
					}
					
					if (inters[gauche(r.getPositionInitIntersection())].asPrioriteDroite()
							&& !obstacle(r, d, in, m, gauche(r.getPositionInitIntersection()), plusD)) {

						return false;
					}

					if (in) {
						if (!r.isRoule()) {
							r.startTheCar();
						}
						System.out.println(r.getNom() + " peut passer");
						occupied.get(entree).add(r);

					}
					return true;
				} else {
					// on regarde � droite

					if (inters[droite(r.getPositionInitIntersection())].asPrioriteDroite()
							&& !obstacle(r, d, in, m, droite(r.getPositionInitIntersection()), plusD)) {

						return false;
					}

					if (in) {
						if (!r.isRoule()) {
							r.startTheCar();
						}
						System.out.println(r.getNom() + " peut passer 33è-è-è-è");
						occupied.get(entree).add(r);
						occupied.get(sortie).add(r);
					}
					return true;

				}
			} else {
				/*
				 * 
				 * 
				 * 
				 * il veut aller � droite
				 * 
				 * 
				 */
			
				int oth = other(entree);
				if ((occupied.get(entree).size() != 0 && !occupied.get(entree).contains(r))
						|| (occupied.get(sortie).size() != 0 && !occupied.get(sortie).contains(r))
						|| (occupied.get(oth).size() != 0 && !occupied.get(oth).contains(r))) {
					for (Car ccc : occupied.get(entree)) {
						if (!checkOccupation(ccc, r, in)) {
							
							return false;
						}
					}
					for (Car ccc : occupied.get(sortie)) {
						if (!checkOccupation(ccc, r, in)) {
							
							return false;
						}
					}
					for (Car ccc : occupied.get(oth)) {
						if (!checkOccupation(ccc, r, in)) {
							
							return false;
						}
					}

				}
				// on regarde si destination bloguant
				if (!destinationBloquante(r, in, m)) {
					
					return false;
				}
				// onregardesiprioritaire
				if (!inters[r.getPositionInitIntersection()].asPrioriteDroite()) {

					// on regarde � droite

					if (!obstacle(r, 3 * d / 2, in, m, droite(r.getPositionInitIntersection()), plusD)) {
						
						return false;
					}
					if (!obstacle(r, 3 * d / 2, in, m, face(r.getPositionInitIntersection()), plusD)) {

						return false;
					}
					if (inters[gauche(r.getPositionInitIntersection())].asPrioriteDroite()
							&& !obstacle(r, 3 * d / 2, in, m, gauche(r.getPositionInitIntersection()), plusD)) {

						return false;
					}

					if (in) {
						if (!r.isRoule()) {
							r.startTheCar();
						}
						System.out.println(r.getNom() + " peut passer");
						occupied.get(entree).add(r);

					}
					return true;
				} else {

					if (inters[droite(r.getPositionInitIntersection())].asPrioriteDroite()
							&& !obstacle(r, 3 * d / 2, in, m, droite(r.getPositionInitIntersection()), plusD)) {

						return false;
					}
					if (inters[face(r.getPositionInitIntersection())].asPrioriteDroite()
							&& !obstacle(r, 3 * d / 2, in, m, face(r.getPositionInitIntersection()), plusD)) {

						return false;
					}

					if (in) {
						if (!r.isRoule()) {
							r.startTheCar();
						}
						System.out.println(r.getNom() + " peut passer");
						occupied.get(entree).add(r);
						occupied.get(sortie).add(r);
						occupied.get(oth).add(r);

					}
					return true;

				}

			}
		}

	}

	public void clear(Car r) {
		for (ArrayList<Car> i : occupied) {
			Iterator<Car> ite = i.iterator();
			while (ite.hasNext()) {
				Car c = ite.next(); // must be called before you can call
									// i.remove()
				// Do something
				if (c.equals(r)) {

					ite.remove();

				}

			}
		}

	}

	private boolean obstacle(Car r, float dd, boolean in, Carte m, int par, float plusD) {
		
		Road roa = roads[par];
		//
		if (roa == null) {
			
			return true;
		}

		for (Car c : carssInNode(m, roa)) {
			if (c.getdIntersection() == 0.0f) {
				if (in) {
					if (r.getSpeed() > 0.1f) {
						System.err.println(r.getNom() + " s'arrete cas 33 " + par);
						System.exit(0);
					}
					r.stopTheCar();
				}
				
				return false;
			}

		}

		for (Car c : roa.carsInRoad(m)) {
			//boolean a = c.timeD(roa.getLength() - c.getdFromNode(), false) <= r.timeD(plusD + dd, true);
			//System.out.println(r.getNom() + " " + c.getNom() + " " + c.timeD(roa.getLength() - c.getdFromNode(), false)
			//		+ " " + r.timeD(plusD + dd, true) + " " + a);

			if (c.timeD(roa.getLength() - c.getdFromNode(), false) <= r.timeD(plusD + dd, true)) {

				if (in) {
					if (r.getSpeed() > 0.1f) {
						System.err.println(r.getNom() + " s'arrete cas 3 " + par);
						System.exit(0);
					}
					r.stopTheCar();

				}
				
				return false;
			}
		}
		
		return true;
	}

	private boolean destinationBloquante(Car r, boolean in, Carte m) {
		for (Car c : r.getOnRoad().carsInRoad(m)) {

			if (!c.isInIntersection() && c.getSpeed() == 0
					&& c.getdFromNode() - c.getLenght() / 2 < r.getLenght() / 2) {

				if (in) {

					System.out.println(r.getNom() + " s'arrete cas 2");
					r.stopTheCar();
				}
				return false;
			}
		}
		return true;
	}

	private int gauche(int i) {
		if (i == 0) {
			return 2;
		} else if (i == 6) {
			return 0;
		} else if (i == 4) {
			return 6;
		} else {
			return 4;
		}
	}

	private int face(int i) {
		if (i == 0) {
			return 4;
		} else if (i == 4) {
			return 0;
		} else if (i == 2) {
			return 6;
		} else {
			return 2;
		}
	}

	private int droite(int i) {
		if (i == 2) {
			return 0;
		} else if (i == 4) {
			return 2;
		} else if (i == 6) {
			return 4;
		} else {
			return 6;
		}
	}

	private boolean checkOccupation(Car ccc, Car r, boolean in) {
		if (!(ccc.getPositionInitIntersection() == r.getPositionInitIntersection()
				&& ccc.getdIntersection() - ccc.getLenght() / 2 > r.getLenght() / 2)) {
			if (in) {
				if (r.getSpeed() > 1.0f) {
					System.err.println(r.getNom() + " Erreur  325 " + ccc.getSpeed() + " " + ccc.getNom() + " "
							+ ccc.getPositionInitIntersection() + " " + r.getPositionInitIntersection());
					System.exit(0);
				}
				r.stopTheCar();
			}
			return false;
		} else {
			return true;
		}
	}

	private int parrallele(int a) {
		if (a == 0) {
			return 5;
		} else if (a == 1) {
			return 4;
		} else if (a == 2) {
			return 7;
		} else if (a == 3) {
			return 6;
		} else if (a == 4) {
			return 1;
		} else if (a == 5) {
			return 0;
		} else if (a == 6) {
			return 3;
		} else {
			return 2;
		}
	}

	public float getD(Car r) {

		return getD(r.getPositionInitIntersection(), roadToInt(r.getOnRoad())) - r.getdIntersection();
	}

	public boolean isInter() {
		return true;
	}

	private int other(int a) {
		if (a == 0) {
			return 3;
		} else {
			return a - 1;
		}
	}

	private float getD(int start, int end) {
		int s = connexion(start);
		int e = connexion(end);

		if (s == e) {

			return d / 2;

		} else {

			if (closeTo(s, e)) {

				return d;

			} else {
				return 3 * d / 2;
			}

		}

	}

	private boolean closeTo(int a, int b) {
		if (a == 0) {
			return b == 3 || b == 1;
		} else if (a == 1) {
			return b == 0 || b == 4;
		} else if (a == 2) {
			return b == 1 || b == 3;
		} else {
			return b == 0 || b == 2;
		}
	}

	private int connexion(int i) {
		if (i == 0 || i == 7) {
			return 0;
		} else if (i == 1 || i == 2) {
			return 1;
		} else if (i == 3 || i == 4) {
			return 2;
		} else {
			return 3;
		}
	}

	public Intersection(String nom, float d) {
		super(nom);
		this.d = d;

	}

	public void set(Road d, int i) {
		roads[i] = d;
	}

	public boolean trafficSim(float t, int cible) {

		int nbOfTRaffic = 0;
		for (InterToRoad interToRoad : inters) {
			if (interToRoad.isTraffic()) {
				nbOfTRaffic++;
			}
		}
		float periode = t % (nbOfTRaffic * 38);
		return cible * 38 <= periode && periode <= cible * 38 + 35;

	}
}
