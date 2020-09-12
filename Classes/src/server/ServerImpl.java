package server;

import host.Computer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import router.Router;
import router.RouterInterface;
import algorithm.NetworkInfo;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5645781987364571968L;

	private List<Router> routers;
	private List<ServerListener> listener;
	private boolean started;

	public ServerImpl() throws RemoteException {
		super();
		routers = new ArrayList<Router>();
		listener = new ArrayList<ServerListener>();
	}

	public void start() throws RemoteException, MalformedURLException {
		Naming.rebind("//localhost/server", this);
		started = true;
	}

	public void setAvgInterval(int interval) throws MalformedURLException,
			RemoteException, NotBoundException {
		for (Router r : routers) {
			RouterInterface config;
			config = (RouterInterface) Naming.lookup("//" + r.getIP()
					+ "/router");
			config.setAvgInterval(interval);
		}
	}

	public void setNetworkInfo(NetworkInfo netinfo) throws MalformedURLException,
			RemoteException, NotBoundException {
		for (Router r : routers) {
			RouterInterface config;
			config = (RouterInterface) Naming.lookup("//" + r.getIP()
					+ "/router");
			config.setNetworkInfo(netinfo);
		}
	}

	public double getQueueAvgSize(Router router) throws MalformedURLException,
			RemoteException, NotBoundException {
		RouterInterface config;
		config = (RouterInterface) Naming.lookup("//" + router.getIP()
				+ "/router");
		return config.getQueueAvgSize();
	}

	public int getLostPackageCount(Router router) throws MalformedURLException,
			RemoteException, NotBoundException {
		RouterInterface config;
		config = (RouterInterface) Naming.lookup("//" + router.getIP()
				+ "/router");
		return config.getLostPackageCount();
	}

	public double getAllQueueAvgSize() throws MalformedURLException,
			RemoteException, NotBoundException {
		double sum = 0;
		for (Router r : routers) {
			RouterInterface config;
			config = (RouterInterface) Naming.lookup("//" + r.getIP()
					+ "/router");
			sum = sum + config.getQueueAvgSize();
		}
		return sum;
	}

	public int getAllLostPackageCount() throws MalformedURLException,
			RemoteException, NotBoundException {
		int sum = 0;
		for (Router r : routers) {
			RouterInterface config;
			config = (RouterInterface) Naming.lookup("//" + r.getIP()
					+ "/router");
			sum = sum + config.getLostPackageCount();
		}
		return sum;
	}

	public double getAllLostPackageAvg() throws MalformedURLException,
			RemoteException, NotBoundException {
		double sum = 0;
		for (Router r : routers) {
			RouterInterface config;
			config = (RouterInterface) Naming.lookup("//" + r.getIP()
					+ "/router");
			sum = sum + config.getLostPackageAvg();
		}
		return sum;
	}

	@Override
	public void addRouter(Router router) throws RemoteException {
		routers.add(router);
		for (ServerListener server : listener) {
			server.addRouter(router);
		}
	}

	@Override
	public void assignHost(Router router, Computer computer)
			throws RemoteException {
		for (int i = 0; i < routers.size(); i++) {
			if (routers.get(i).equals(router)) {
				router = routers.get(i);
				router.setComputer(computer);
				for (ServerListener server : listener) {
					server.assignHost(router, computer);
				}
				return;
			}
		}
		throw new RuntimeException("Router " + router.getIP() + " not found");
	}

	@Override
	public void removeRouter(Router router) throws RemoteException {
		for (ServerListener server : listener) {
			server.removeRouter(router);
		}
		for (int i = 0; i < routers.size(); i++) {
			if (routers.get(i).equals(router)) {
				routers.remove(i);
				return;
			}
		}
		throw new RuntimeException("Router " + router.getIP() + " not found");
	}

	@Override
	public void detachHost(Router router, Computer computer)
			throws RemoteException {
		for (ServerListener server : listener) {
			server.detachHost(router, computer);
		}
		for (int i = 0; i < routers.size(); i++) {
			if (routers.get(i).equals(router)) {
				Router r = routers.get(i);
				r.setComputer(null);
				return;
			}
		}
		throw new RuntimeException("Router " + router.getIP() + " not found");
	}

	public void addServerListener(ServerListener listener) {
		this.listener.add(listener);
	}

	public void halt() throws MalformedURLException, RemoteException,
			NotBoundException {
		if (!started)
			return;
		for (Router r : routers) {
			RouterInterface config;
			config = (RouterInterface) Naming.lookup("//" + r.getIP()
					+ "/router");
			config.serverHalted();
		}
		Naming.unbind("//localhost/server");
		UnicastRemoteObject.unexportObject(this, true);
		started = false;
	}

}
