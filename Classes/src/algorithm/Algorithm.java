package algorithm;

import java.util.List;

import router.Router;

abstract public class Algorithm {
	private Router source;
	
	abstract public void addLink(Router routerA, Router routerB, double weight);
	abstract public void execute();
	abstract public List<Router> getRouterList();
	abstract public List<Link> getLinkList();
	abstract public String getName();
	abstract public Table getTable();
	
	
	public Router getSource() {
		return source;
	}
	
	public void setSource(Router source) {
		this.source = source;
	}
}
