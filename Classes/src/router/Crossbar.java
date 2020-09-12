package router;

import pkg.Package;


public class Crossbar extends Commutator implements DispatcherListener {
	private Dispatcher dispatcher;

	public Crossbar() {
		dispatcher = new Dispatcher();
		dispatcher.addListener(this);
	}

	public void receive(pkg.Package pkg) {
		if(getTable() == null) {
			lostPackage(pkg);
			throw new RuntimeException("A tabela de roteamento não está pronta");
		}
		pkg.setNeighbor(getTable().getNeighbor(pkg.getDestination()));
		dispatcher.give(pkg);
	}

	@Override
	public int getQueueSize() {
		return dispatcher.getQueueSize();
	}

	@Override
	public void setMaxQueueSize(int size) {
		dispatcher.setMaxQueueSize(size);
	}

	@Override
	public void setTimeProcessing(int time) {
		dispatcher.setTimeProcessing(time);
	}

	@Override
	public void sentPackage(Package buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lostData(Package data) {
		lostPackage(data);
	}

}
