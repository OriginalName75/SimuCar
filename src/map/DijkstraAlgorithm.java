package map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import node.Node;

public class DijkstraAlgorithm {
	
	private final ArrayList<Road> edges;
	private Map<Node, Integer> distance;
	private Set<Node> settledNodes;
	private Set<Node> unSettledNodes;
	private Map<Node, Node> predecessors;

	public DijkstraAlgorithm(Carte m) {
		super();
		
		this.edges = new ArrayList<Road>(m.getRoads());

	}

	public void execute(Node source) {
		settledNodes = new HashSet<Node>();
		unSettledNodes = new HashSet<Node>();
		distance = new HashMap<Node, Integer>();
		predecessors = new HashMap<Node, Node>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Node node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(Node node) {
		ArrayList<Node> adjacentNodes = getNeighbors(node);
		for (Node target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
				distance.put(target, getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(Node node, Node target) {
		for (Road edge : edges) {
			if (edge.getStart().equals(node) && edge.getEnd().equals(target)) {
				return (int) edge.getLength();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private int getShortestDistance(Node destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	private ArrayList<Node> getNeighbors(Node node) {
		ArrayList<Node> neighbors = new ArrayList<Node>();
		for (Road edge : edges) {
			if (edge.getStart().equals(node) && !isSettled(edge.getEnd())) {
				neighbors.add(edge.getEnd());
			}
		}
		return neighbors;
	}

	private boolean isSettled(Node vertex) {
		return settledNodes.contains(vertex);
	}

	private Node getMinimum(Set<Node> vertexes) {
		Node minimum = null;
		for (Node vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}
	public LinkedList<Node> getPath(Node target) {
        LinkedList<Node> path = new LinkedList<Node>();
        Node step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
                return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
                step = predecessors.get(step);
                path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
	}

}
