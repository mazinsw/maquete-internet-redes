package gui;

public class RouterRecord {
	private String destination;
	private String neighbor;
	
	public RouterRecord(String destination, String neighbor) {
		setDestination(destination);
		setNeighbor(neighbor);
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(String neighbor) {
		this.neighbor = neighbor;
	}
}
