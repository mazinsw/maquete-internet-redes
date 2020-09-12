package algorithm;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import router.Router;

public class BellmanFord extends Algorithm {

	private List<Link> links;
	private List<Router> routers;
	private DistanceVector distance;
	private Table table;

	public BellmanFord() {

		links = new ArrayList<Link>();
		routers = new ArrayList<Router>();
		distance = new DistanceVector();
	}

	private Router addRouter(Router router) {
		for (Router r : routers) {
			if (router.equals(r))
				return r;
		}
		routers.add(router);
		return router;
	}

	private boolean linkExists(Link link) {
		for (Link e : links) {
			if (link.equals(e))
				return true;
		}
		return false;
	}

	public void addLink(Router routerA, Router routerB, double weight) {
		// TODO Auto-generated method stub
		Link link = new Link(addRouter(routerA), addRouter(routerB), weight);
		if (linkExists(link) || routerA.equals(routerB))
			return;
		links.add(link);
	}

	@Override
	public void execute() {
		distance.setSource(getSource());
		try {
			distance.init(routers, links);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Router> getRouterList() {
		List<Router> routers = new ArrayList<Router>();
		routers.addAll(this.routers);
		// TODO Auto-generated method stub
		return routers;
	}

	public List<Link> getLinkList() {
		List<Link> links = new ArrayList<Link>();
		links.addAll(this.links);
		// TODO Auto-generated method stub
		return links;
	}

	@Override
	public String getName() {
		return "Bellman–Ford";
	}

	@Override
	public Table getTable() {
		if(table != null)
			return table;
		return distance.getTable(getSource(), routers);
	}

	public void updateDV(DistanceVector dv) throws MalformedURLException, RemoteException, NotBoundException {
		if(distance.update(dv))
			table = null;
	}

}
