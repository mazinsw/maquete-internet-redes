package pkg;

import java.util.ArrayList;
import java.util.List;

import router.ReasonListener;

public class PackageQueue {
	public int size;
	private List<ReasonListener> listener;
	private List<Package> buffer;
	
	public PackageQueue() {
		setSizeLimit(1024);
		listener = new ArrayList<ReasonListener>();
		buffer = new ArrayList<Package>();
	}
	
	public void push(Package pkg) {
		buffer.add(pkg);
	}
	
	public Package front() {
		return buffer.get(0);
	}
	
	public Package pop() {
		return buffer.remove(0);
	}
	
	public void setSizeLimit(int size) {
		this.size = size;
	}
	
	public int getSizeLimit() {
		return size;
	}
	
	public int size() {
		return buffer.size();
	}
	
	public boolean isEmpty() {
		return buffer.isEmpty();
	}
	
	public void addReasonListener(ReasonListener listener) {
		this.listener.add(listener);
	}

}
