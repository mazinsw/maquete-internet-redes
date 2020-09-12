package router;

import java.util.ArrayList;
import java.util.List;

import algorithm.Table;

abstract public class Commutator {
	private List<ReasonListener> listener;
	private Table table;
	
	public Commutator() {
		listener = new ArrayList<ReasonListener>();
	}
	
	abstract public void receive(pkg.Package pkg);
	
	protected void lostPackage(pkg.Package pkg) {
		for (ReasonListener item : listener) {
			item.lostPackage(pkg);
		}
	}
	
	public void addReasonListener(ReasonListener listener) {
		this.listener.add(listener);
	}
	
	public Table getTable() {
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	
	abstract public int getQueueSize();

	abstract public void setMaxQueueSize(int size);

	abstract public void setTimeProcessing(int time);
}
