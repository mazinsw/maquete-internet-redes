package algorithm;

import java.io.Serializable;
import java.util.List;

public class NetworkInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -934104443115552417L;
	public static final int ALGORITHM_DIJKSTRA = 0;
	public static final int ALGORITHM_BELLMANFORD = 1;
	private int algorithm;
	private List<Link> links;
	
	public NetworkInfo(List<Link> links, int algorithm) {
		if(algorithm != ALGORITHM_BELLMANFORD && algorithm != ALGORITHM_DIJKSTRA)
			throw new RuntimeException("Invalid routing algorithm");
		this.algorithm = algorithm;
		this.links = links;
	}
	
	public List<Link> getLinks() {
		return links;
	}
	
	public int getAlgorithm() {
		return algorithm;
	}
}
