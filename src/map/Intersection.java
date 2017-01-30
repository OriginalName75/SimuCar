/**
 * 
 */
package map;

import java.util.ArrayList;
import java.util.Iterator;

import car.Car;
import inter.InterToRoad;

/**
 * @author Original Name
 *
 */
public class Intersection extends Node {
	protected Road[] roads;
	protected InterToRoad[] inters;
	protected ArrayList<ArrayList<Car>> occupied;
	protected int[] ligne;
	protected float d;
	

	
	/**
	 * @param nom
	 */
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
		ligne = new int[2];
		inters = new InterToRoad[8];
		for (int i = 0; i < inters.length; i++) {
			inters[i] = new InterToRoad();
		}

	}

	public void setligne(int a, int b) {
		ligne[0] = a;
		ligne[1] = b;
	}

	public int roadToInt(Road r) {
		int i = 0;
		for (Road road : roads) {
			if (r.equals(road)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public ArrayList<Car> carssInNode(Map m, Road ro) {
		ArrayList<Car> r = new ArrayList<Car>();
		for (Car c : m.getCars()) {
			if (c.isInIntersection() && roads[c.getPositionInitIntersection()].equals(ro)
					&& c.getOnRoad().getStart().equals(this)) {
				r.add(c);
			}
		}
		return r;
	}

	public boolean checkGo(Car r, Map m, boolean in) {
		int entree = connexion(r.getPositionInitIntersection());
		int sortie = connexion(roadToInt(r.getOnRoad()));
		/*
		 * 
		 * 
		 * 
		 * il veut tourner à droite
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

				if (in) {
					System.out.println(r.getNom() + " peut passer cas 6");
					occupied.get(entree).add(r);

				}
				return true;

			} else {
				// on regarde si destination bloguant
				if (!destinationBloquante(r, in, m)) {
					return false;
				}

				// onregardesiprioritaire
				if (!inters[r.getPositionInitIntersection()].asPrioriteDroite()) {

					// si ps prioritaire onregarde à gauche
					int par = parrallele(roadToInt(r.getOnRoad()));
					if (inters[par].asPrioriteDroite() && !obstacle(r, d / 2, in, m, par)) {
						return false;
					}
					if (in) {
						System.out.println(r.getNom() + " peut passer");
						occupied.get(entree).add(r);

					}
					return true;
				} else {
					if (in) {
						System.out.println(r.getNom() + " peut passer");
						occupied.get(entree).add(r);

					}
					return true;
				}

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
					if (in) {
						System.out.println(r.getNom() + " peut passer cas 6");
						occupied.get(entree).add(r);
						occupied.get(sortie).add(r);
					}
					return true;
				} else {
					if (!destinationBloquante(r, in, m)) {
						return false;
					}
					// onregardesiprioritaire
					if (!inters[r.getPositionInitIntersection()].asPrioriteDroite()) {

						// on regarde à droite

						if (obstacle(r, d, in, m, droite(r.getPositionInitIntersection()))) {

							return false;
						}
						if (inters[gauche(r.getPositionInitIntersection())].asPrioriteDroite()
								&& obstacle(r, d, in, m, gauche(r.getPositionInitIntersection()))) {

							return false;
						}

						if (in) {
							System.out.println(r.getNom() + " peut passer");
							occupied.get(entree).add(r);

						}
						return true;
					} else {
						// on regarde à droite

						if (inters[droite(r.getPositionInitIntersection())].asPrioriteDroite()
								&& !obstacle(r, d, in, m, droite(r.getPositionInitIntersection()))) {

							return false;
						}

						if (in) {
							System.out.println(r.getNom() + " peut passer");
							occupied.get(entree).add(r);
							occupied.get(sortie).add(r);
						}
						return true;
					}
				}
			} else {
				/*
				 * 
				 * 
				 * 
				 * il veut aller à droite
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
					if (in) {
						System.out.println(r.getNom() + " peut passer cas 6");
						occupied.get(entree).add(r);
						occupied.get(sortie).add(r);
						occupied.get(oth).add(r);
					}
					return true;
				} else {
					// on regarde si destination bloguant
					if (!destinationBloquante(r, in, m)) {
						return false;
					}
					// onregardesiprioritaire
					if (!inters[r.getPositionInitIntersection()].asPrioriteDroite()) {

						// on regarde à droite

						if (obstacle(r, 3 * d / 2, in, m, droite(r.getPositionInitIntersection()))) {

							return false;
						}
						if (obstacle(r, 3 * d / 2, in, m, face(r.getPositionInitIntersection()))) {

							return false;
						}
						if (inters[gauche(r.getPositionInitIntersection())].asPrioriteDroite()
								&& obstacle(r, 3 * d / 2, in, m, gauche(r.getPositionInitIntersection()))) {

							return false;
						}

						if (in) {
							System.out.println(r.getNom() + " peut passer");
							occupied.get(entree).add(r);

						}
						return true;
					} else {

						if (inters[droite(r.getPositionInitIntersection())].asPrioriteDroite()
								&& !obstacle(r, 3 * d / 2, in, m, droite(r.getPositionInitIntersection()))) {

							return false;
						}
						if (inters[face(r.getPositionInitIntersection())].asPrioriteDroite()
								&& !obstacle(r, 3 * d / 2, in, m, face(r.getPositionInitIntersection()))) {
							
							return false;
						}

						if (in) {

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

	private boolean obstacle(Car r, float dd, boolean in, Map m, int par) {

		Road roa = roads[par];
		//
		if (roa == null) {
			return true;
		}

		for (Car c : carssInNode(m, roa)) {
			if (c.getdIntersection() == 0.0f) {
				if (in) {

					System.out.println(r.getNom() + " s'arrete cas 33" + par);
					
					r.stopTheCar();
				}
			
				return false;
			}
		}

		for (Car c : roa.carsInRoad(m)) {

			if (c.getSpeed() > 0 && dd / r.getMaxSpeed() >= (roa.getLength() - c.getdFromNode()) / c.getSpeed()) {

				if (in) {

					System.out.println(r.getNom() + " s'arrete cas 3" + par);
					r.stopTheCar();

				}
				return false;
			}
		}
		return true;
	}

	private boolean destinationBloquante(Car r, boolean in, Map m) {
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
			return 3;
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
			return 4;
		} else if (i == 4) {
			return 6;
		} else if (i == 6) {
			return 0;
		} else {
			return 2;
		}
	}

	private boolean checkOccupation(Car ccc, Car r, boolean in) {
		if (!(ccc.getPositionInitIntersection() == r.getPositionInitIntersection()
				&& ccc.getdIntersection() - ccc.getLenght() / 2 > r.getLenght() / 2)) {
			if (in) {
				System.out.println(r.getNom() + " s'arrete");
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

}
