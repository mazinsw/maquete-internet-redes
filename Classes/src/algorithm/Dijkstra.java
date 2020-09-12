package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import router.Router;

public class Dijkstra extends Algorithm {

	private List<Link> edges;
	private List<Link> edges_back;
	private List<Router> routers;
	private Set<Router> settledNodes;
	private Set<Router> unSettledNodes;
	private Map<Router, Router> predecessors;
	private Map<Router, Double> distance;
	private Table table;

	public Dijkstra() {
		edges = new ArrayList<Link>();
		edges_back = new ArrayList<Link>();
		routers = new ArrayList<Router>();
	}

	private Router addRouter(Router router) {
		for (Router r : routers) {
			if (router.equals(r))
				return r;
		}
		routers.add(router);
		return router;
	}

	private boolean edgeExists(Link edge) {
		for (Link e : edges_back) {
			if (edge.equals(e))
				return true;
		}
		return false;
	}

	public void execute() {
		table = null;
		settledNodes = new HashSet<Router>();
		unSettledNodes = new HashSet<Router>();
		distance = new HashMap<Router, Double>();
		predecessors = new HashMap<Router, Router>();
		distance.put(getSource(), 0.0);
		unSettledNodes.add(getSource());
		while (unSettledNodes.size() > 0) {
			Router node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(Router node) {
		List<Router> adjacentNodes = getNeighbors(node);
		for (Router target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private double getDistance(Router node, Router target) {
		for (Link edge : edges_back) {
			if (edge.getRouterA().equals(node)
					&& edge.getRouterB().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Router> getNeighbors(Router node) {
		List<Router> neighbors = new ArrayList<Router>();
		for (Link edge : edges_back) {
			if (edge.getRouterA().equals(node) && !isSettled(edge.getRouterB())) {
				neighbors.add(edge.getRouterB());
			}
		}
		return neighbors;
	}

	private Router getMinimum(Set<Router> vertexes) {
		Router minimum = null;
		for (Router vertex : vertexes) {
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

	private boolean isSettled(Router vertex) {
		return settledNodes.contains(vertex);
	}

	private double getShortestDistance(Router destination) {
		Double d = distance.get(destination);
		if (d == null) {
			return Double.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<Router> getPath(Router target) {
		LinkedList<Router> path = new LinkedList<Router>();
		Router step = target;
		// Check if a path exists
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

	@Override
	public void addLink(Router routerA, Router routerB, double weight) {
		Link edge = new Link(addRouter(routerA), addRouter(routerB), weight);
		Link edge_back = new Link(addRouter(routerB), addRouter(routerA),
				weight);
		if (edgeExists(edge) || routerA.equals(routerB))
			return;
		if (edgeExists(edge_back) || routerB.equals(routerA))
			return;
		edges.add(edge);
		edges_back.add(edge);
		edges_back.add(edge_back);
	}

	@Override
	public List<Router> getRouterList() {
		List<Router> routers = new ArrayList<Router>();
		routers.addAll(this.routers);
		// TODO Auto-generated method stub
		return routers;
	}

	public List<Link> getLinkList() {
		List<Link> edges = new ArrayList<Link>();
		edges.addAll(this.edges);
		// TODO Auto-generated method stub
		return edges;
	}

	@Override
	public String getName() {
		return "Dijkstra";
	}

	public Table getTable() {
		if(table != null)
			return table;
		table = new Table(getSource());
		int pos = routers.indexOf(getSource());
		for (int i = 0; i < routers.size(); i++) {
			if (pos == i)
				continue;
			LinkedList<Router> path = getPath(routers.get(i));
			Router neighbor = path.get(1);
			table.setNeighbor(routers.get(i), neighbor);
		}
		return table;
	}

}
