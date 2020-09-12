package pkg;

import java.io.Serializable;

import router.Router;

public class Package implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -140972957817151485L;
	private Router destination;
	private Router source;
	private Router neighbor;

	byte[] data;
	
	public Package(byte[] data) {
		setData(data);
	}
	
	private void setData(byte[] data) {
		this.data = data.clone();
	}

	public byte[] getData() {
		return data;
	}
	
	public int getLength() {
		return data.length;
	}

	public Router getDestination() {
		return destination;
	}

	public void setDestination(Router destination) {
		this.destination = destination;
	}

	public Router getSource() {
		return source;
	}

	public void setSource(Router source) {
		this.source = source;
	}

	public Router getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(Router neighbor) {
		this.neighbor = neighbor;
	}
	
	
}
