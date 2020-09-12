package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import router.Router;
import router.RouterInterface;

public class DistanceVector implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6867202981873617971L;
	HashMap<String, Pair> map;
	String self;

	public DistanceVector(Router router) {
		map = new HashMap<String, Pair>();
		self = router.getIP();
	}

	public DistanceVector() {
		map = new HashMap<String, Pair>();
	}
	
	public void setSource(Router router) {
		self = router.getIP();
	}

	public void init(List<Router> routers, List<Link> links, boolean test) throws MalformedURLException, RemoteException, NotBoundException {
		for (Router router : routers) {
			map.put(router.getIP(), getPair(router.getIP(), links));
		}
		if(!test)
			sendToAll();
	}

	public void init(List<Router> routers, List<Link> links) throws MalformedURLException, RemoteException, NotBoundException {
		init(routers, links, false);
	}
	
	private void sendToAll() throws MalformedURLException, RemoteException, NotBoundException {
		Set<Entry<String, Pair>> set = map.entrySet();
		for (Entry<String, Pair> entry : set) {
			if(entry.getKey().equals(self))
				continue;
			RouterInterface router = (RouterInterface)Naming.lookup("//" + entry.getKey() + "/router");
			router.receiveDV(this);
		}
	}


	private Pair getPair(String ip, List<Link> links) {
		for (Link link : links) {
			if ((self.equals(link.getRouterA().getIP()) && ip.equals(link
					.getRouterB().getIP()))
					|| (ip.equals(link.getRouterA().getIP()) && self
							.equals(link.getRouterB().getIP()))) {
				return new Pair(ip, link.getWeight());
			}
		}
		if(self.equals(ip))
			return new Pair(ip, 0.0);
		return new Pair(null, Double.MAX_VALUE);
	}

	public boolean update(DistanceVector sd, boolean test) throws MalformedURLException, RemoteException, NotBoundException {
		boolean changed = false;
		Pair pab = map.get(sd.self);
		if(pab == null) {
			System.out.println("Node " + self + " has received a distance vector of " + sd.self + " but the your has not been initialized yet");
			return false;
		}
		double ab = pab.cost;
		Set<Entry<String, Pair>> set = sd.map.entrySet();
		for (Entry<String, Pair> entry : set) {
			if(entry.getKey().equals(sd.self))
				continue;
			Pair pa = map.get(entry.getKey());
			double aa = pa.cost;
			if(aa > ab + entry.getValue().cost && pab.hop != null) {
				pa.cost = ab + entry.getValue().cost;
				pa.hop = pab.hop;
				changed = true;
			}
		}
		if(changed && !test)
			sendToAll();
		return changed;
	}
	
	public boolean update(DistanceVector sd) throws MalformedURLException, RemoteException, NotBoundException {
		return update(sd, false);
	}
	
	private Router getRouter(List<Router> routers, String ip) {
		for (Router router : routers) {
			if(router.getIP().equals(ip))
			{
				return router;
			}
		}
		throw new RuntimeException("Router " + ip + " not found");
	}

	public Table getTable(Router source, List<Router> routers) {
		Table table = new Table(source);
		Set<Entry<String, Pair>> set = map.entrySet();
		for (Entry<String, Pair> entry : set) {
			table.setNeighbor(getRouter(routers, entry.getKey()), getRouter(routers, entry.getValue().hop));
		}
		return table;
	}

}

class Pair implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4718589125659589646L;
	public String hop;
	public double cost;

	public Pair(String hop, double cost) {
		this.hop = hop;
		this.cost = cost;
	}
}
