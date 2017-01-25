package map;

import sim.Simulation;

public class Tests {
	
	public static void main(String[] args) {
		
		Simulation sim = new Simulation();
		
		//sim.getSimuCreaCars().buildIt(sim.getMap().getNodes().get(0),sim.getMap().getNodes().get(2), sim.getMap());
		sim.simulate(24*3600);
	}

}
