package algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import router.Router;

public class Table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6037021305691200095L;
	private Router source;
	private List<Router> routers;
	private Comparator<Router> routerComparator;
	private SortedMap<Router, Router> myMap;

	public Table(Router source) {
		setSource(source);
		routerComparator = new Comparator<Router>() {
			@Override
			public int compare(Router a, Router b) {
				return a.getIP().compareTo(b.getIP());
			}
		};
		myMap = new TreeMap<Router, Router>(routerComparator);
		routers = new ArrayList<Router>();
	}

	public Router getNeighbor(Router destination) {
		return myMap.get(destination);
	}
	
	public void setNeighbor(Router destination, Router neighbor) {
		myMap.put(destination, neighbor);
		routers.add(destination);
	}

	public Router getSource() {
		return source;
	}

	private void setSource(Router source) {
		this.source = source;
	}
	
	public List<Router> getRouters() {
		return routers;
	}

}
