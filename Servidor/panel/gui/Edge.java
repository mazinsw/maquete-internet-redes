package gui;

public class Edge {
	private Node a;
	private Node b;
	private double weight;
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Node getA() {
		return a;
	}
	public void setA(Node a) {
		this.a = a;
	}
	public Node getB() {
		return b;
	}
	public void setB(Node b) {
		this.b = b;
	}
	
	@Override
	public boolean equals(Object arg0) {
		Edge other = (Edge)arg0;
		return ((a.equals(other.a)) && (b.equals(other.b))) || ((a.equals(other.b)) && (b.equals(other.a)));
	}
	
	public Edge copy() {
		Edge edge = new Edge();
		edge.a = a.copy();
		edge.b = b.copy();
		edge.weight = weight;
		return edge;
	}
	
}
