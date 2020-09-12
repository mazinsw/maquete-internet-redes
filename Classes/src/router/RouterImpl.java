package router;

import host.Computer;
import host.HostInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import server.ServerInterface;
import algorithm.Algorithm;
import algorithm.BellmanFord;
import algorithm.Dijkstra;
import algorithm.DistanceVector;
import algorithm.Link;
import algorithm.NetworkInfo;
import algorithm.Table;

import common.Average;
import common.Timer;
import common.TimerListener;
import common.Util;

public class RouterImpl extends UnicastRemoteObject implements RouterInterface,
		ReasonListener, TimerListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7214769746243324574L;
	private int pkglostcount;
	private int prevlostcount;
	private int avginterval;
	private Average avgpkg;
	private Average avgqueue;
	private Timer timer;
	private Commutator comutator;
	private ServerInterface server;
	private Router router;
	private Algorithm algorithm;
	private NetworkInfo netinfo;
	private List<RouterListener> listener;
	private boolean started;

	public RouterImpl(String serverIP) throws RemoteException,
			MalformedURLException, NotBoundException {
		super();
		listener = new ArrayList<RouterListener>();
		pkglostcount = 0;
		prevlostcount = 0;
		avgpkg = new Average();
		avgqueue = new Average();
		avginterval = 1000;
		server = (ServerInterface) Naming.lookup("//" + serverIP + "/server");
		comutator = new Crossbar();
		comutator.addReasonListener(this);
		timer = new Timer(avginterval, this);
		router = new Router(Util.getLocalIP());
		algorithm = null;
		netinfo = null;
	}

	public void start() throws RemoteException, MalformedURLException {
		Naming.rebind("//localhost/router", this);
		started = true;
		server.addRouter(router);
	}

	@Override
	public void receive(pkg.Package pkg) throws RemoteException {
		for (RouterListener router : listener) {
			router.receive(pkg);
		}
		if (pkg.getDestination().equals(router)) {
			if (router.getComputer() != null
					&& router.getComputer().getIP()
							.equals(pkg.getDestination().getIP())) {
				HostInterface host;
				try {
					host = (HostInterface) Naming.lookup("//"
							+ pkg.getDestination().getIP() + "/host");
					host.receive(pkg);
				} catch (MalformedURLException | NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return;
		}
		comutator.setTable(algorithm.getTable());
		comutator.receive(pkg);
	}

	@Override
	public void connect(Computer computer) throws RemoteException {
		router.setComputer(computer);
		for (RouterListener router : listener) {
			router.connect(computer);
		}
		server.assignHost(router, computer);
		if (algorithm != null) {
			try {
				updateHostRouterPanel(netinfo);
			} catch (MalformedURLException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void lostPackage(pkg.Package pkg) {
		pkglostcount++;
	}

	/**
	 * É Executado por um timer para contar a média de pacotes na fila
	 */
	public void onTimer() {
		int pkgcount = pkglostcount - prevlostcount;
		int queuecount = comutator.getQueueSize();

		prevlostcount = pkglostcount;
		avgpkg.add(pkgcount);
		avgqueue.add(queuecount);
		timer.reset();
	}

	@Override
	public void setNetworkInfo(NetworkInfo netinfo) throws RemoteException {
		this.netinfo = netinfo;
		if(netinfo.getAlgorithm() == NetworkInfo.ALGORITHM_DIJKSTRA)
			this.algorithm = new Dijkstra();
		else
			this.algorithm = new BellmanFord();
		for (RouterListener router : listener) {
			router.networkInfoChanged(netinfo);
		}
		this.algorithm.setSource(router);
		for (Link link : netinfo.getLinks()) {
			this.algorithm.addLink(link.getRouterA(), link.getRouterB(), link.getWeight());
		}
		this.algorithm.execute();
		timer.start();
	}

	public void updateHostRouterPanel(NetworkInfo netinfo)
			throws MalformedURLException, RemoteException, NotBoundException {
		if (router.getComputer() != null) {
			HostInterface host = (HostInterface) Naming.lookup("//"
					+ router.getComputer().getIP() + "/host");
			host.updateNetworkInfo(netinfo);
		}
	}

	@Override
	public double getQueueAvgSize() throws RemoteException {
		return avgqueue.get();
	}

	@Override
	public void setAvgInterval(int interval) throws RemoteException {
		avginterval = interval;
		timer.cancel();
		timer = new Timer(avginterval, this);
		if (algorithm != null)
			timer.start();
	}

	@Override
	public int getLostPackageCount() throws RemoteException {
		return pkglostcount;
	}

	@Override
	public double getLostPackageAvg() throws RemoteException {
		return avgpkg.get();
	}

	public void addRouterListener(RouterListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void serverHalted() throws RemoteException {
		server = null;
		for (RouterListener router : listener) {
			router.serverHalted();
		}
	}

	public void halt() throws RemoteException, MalformedURLException,
			NotBoundException {
		if (!started)
			return;
		if (router.getComputer() != null) {
			HostInterface host = (HostInterface) Naming.lookup("//"
					+ router.getComputer().getIP() + "/host");
			host.routerHalted();
		}
		if (server != null) {
			server.removeRouter(router);
		}
		Naming.unbind("//localhost/router");
		UnicastRemoteObject.unexportObject(this, true);
		started = false;
	}

	@Override
	public void hostHalted() throws RemoteException {

		if (server != null && router.getComputer() != null) {
			try {
				server.detachHost(router, router.getComputer());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			router.setComputer(null);
			for (RouterListener router : listener) {
				router.hostHalted();
			}
		}
	}

	public Router getRouter() {
		return router;
	}

	public void setMaxQueueSize(int size) {
		comutator.setMaxQueueSize(size);
	}

	public void setTimeProcessing(int time) {
		comutator.setTimeProcessing(time);
	}

	public Table getTable() {
		if(algorithm == null)
			return null;
		// TODO Auto-generated method stub
		return algorithm.getTable();
	}

	@Override
	public void receiveDV(DistanceVector dv) throws RemoteException {
		if(algorithm == null || !(algorithm instanceof BellmanFord))
			throw new RuntimeException("Invalid message for algorithm");
		try {
			((BellmanFord)algorithm).updateDV(dv);
		} catch (MalformedURLException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
