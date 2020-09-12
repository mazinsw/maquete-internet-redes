package host;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import router.RouterInterface;
import algorithm.NetworkInfo;

import common.Util;

public class HostImpl extends UnicastRemoteObject implements HostInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8254186405788681087L;
	private List<HostListener> listener;
	private RouterInterface router;
	private Computer computer;
	private boolean started;

	public HostImpl() throws RemoteException, MalformedURLException,
			NotBoundException {
		super();
		computer = new Computer(Util.getComputerName(), Util.getLocalIP());
		listener = new ArrayList<HostListener>();
		router = (RouterInterface) Naming.lookup("//localhost/router");
	}

	public void start() throws RemoteException, MalformedURLException {
		Naming.rebind("//localhost/host", this);
		started = true;
		router.connect(computer);
	}

	@Override
	public void receive(pkg.Package pkg) throws RemoteException {
		for (HostListener host : listener) {
			host.receive(pkg);
		}
	}

	public void addHostListener(HostListener listener) {
		this.listener.add(listener);
	}

	public void sendPackage(pkg.Package pkg) throws RemoteException {
		router.receive(pkg);
	}

	public Computer getComputer() {
		return computer;
	}

	@Override
	public void routerHalted() throws RemoteException {
		for (HostListener host : listener) {
			host.routerHalted();
		}
		router = null;
	}

	public void halt() throws RemoteException, MalformedURLException,
			NotBoundException {
		if (!started)
			return;
		if (router != null) {
			router.hostHalted();
		}
		Naming.unbind("//localhost/host");
		UnicastRemoteObject.unexportObject(this, true);
		started = false;
	}

	@Override
	public void updateNetworkInfo(NetworkInfo netinfo) throws RemoteException {
		for (HostListener host : listener) {
			host.updateNetworkInfo(netinfo);
		}
	}

}
