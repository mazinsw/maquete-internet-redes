package algorithm;

import java.io.Serializable;

import router.Router;

public class Link implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4238099706370812197L;
	private Router routerA;
	private Router routerB;
	private double weight;

	public Link(Router routerA, Router parentB, double weight)
	{
		setRouterA(routerA);
		setRouterB(parentB);
		setWeight(weight);
	}
	
	public Router getRouterA() {
		return routerA;
	}
	
	public double getWeight() {
		return weight;
	}
	
	private void setRouterA(Router router) {
		this.routerA = router;
	}
	
	private void setWeight(double weight) {
		this.weight = weight;
	}

	public Router getRouterB() {
		return routerB;
	}

	private void setRouterB(Router router) {
		this.routerB = router;
	}
	
	@Override
	public boolean equals(Object arg0) {
		Link edge = (Link)arg0;
		return (edge.routerB.equals(routerB) && edge.routerA.equals(routerA)) ||
				(edge.routerB.equals(routerA) && edge.routerA.equals(routerB));
	}
}
