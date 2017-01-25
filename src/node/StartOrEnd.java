package node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import sim.Horaire;

public class StartOrEnd extends Node {
	private Horaire h;
	private float wait;
	private ArrayList<ProbaArrive> probAArrive;
	private Random r;

	public StartOrEnd(String nom, Horaire h) {
		super(nom);
		this.h = h;
		r = new Random();
		geneRep();

		setNext();
		probAArrive = new ArrayList<ProbaArrive>();

		// TODO Auto-generated constructor stub
	}

	public void addProbaDesti(int proba, StartOrEnd desti) {
		probAArrive.add(new ProbaArrive(proba, desti));

	}

	public Node chooseRandomDesti() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
		//int randomNum=50;
		int ran = 0;
		for (ProbaArrive probaArrive2 : probAArrive) {
			ran += probaArrive2.getProba();
			if (randomNum < ran) {
				return probaArrive2.getCible();
			}
		}
		System.err.println("Erreur de proba");
		System.exit(0);
		return null;

	}

	public float getWait() {
		return wait;
	}

	public void setNext() {
		wait = repartition.get(0);
		repartition.remove(0);
	}

	public void setWait(float wait) {
		this.wait = wait;
	}

	public void geneRep() {
		int nbVoitureMoy = h.get_0to7() * 7;
		int nbVoiture = nbVoitureMoy + ((int) ((((double) nbVoitureMoy) / 10.0) * r.nextGaussian()));
		//int nbVoiture =nbVoitureMoy;

		for (int i = 0; i < nbVoiture; i++) {
			repartition.add(ThreadLocalRandom.current().nextInt(0, 7 * 3600));
			//repartition.add(7 * 3600*(i+1)/nbVoiture);
			
		}
		
		nbVoitureMoy = h.get_7to9() * 2;
		nbVoiture = nbVoitureMoy + ((int) ((((double) nbVoitureMoy) / 10.0) * r.nextGaussian()));
		

		for (int i = 0; i < nbVoiture; i++) {
			repartition.add(ThreadLocalRandom.current().nextInt(7 * 3600, 9 * 3600));
		
		}
		
		nbVoitureMoy = h.get_9to17() * 8;
		nbVoiture = nbVoitureMoy + ((int) ((((double) nbVoitureMoy) / 10.0) * r.nextGaussian()));
		

		for (int i = 0; i < nbVoiture; i++) {
			repartition.add(ThreadLocalRandom.current().nextInt(9 * 3600, 17 * 3600));
		}
		
		nbVoitureMoy = h.get_17to19() * 2;
		nbVoiture = nbVoitureMoy + ((int) ((((double) nbVoitureMoy) / 10.0) * r.nextGaussian()));
		

		for (int i = 0; i < nbVoiture; i++) {
			repartition.add(ThreadLocalRandom.current().nextInt(17 * 3600, 19 * 3600));
		}
		
		nbVoitureMoy = h.get_19to0()* 5;
		nbVoiture = nbVoitureMoy + ((int) ((((double) nbVoitureMoy) / 10.0) * r.nextGaussian()));
		

		for (int i = 0; i < nbVoiture; i++) {
			repartition.add(ThreadLocalRandom.current().nextInt(19 * 3600, 24 * 3600));
		}
		Collections.sort(repartition);
		
		
	}
}
