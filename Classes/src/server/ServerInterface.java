package server;

import host.Computer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import router.Router;

public interface ServerInterface extends Remote {
	
	public void addRouter(Router router) throws RemoteException;

	public void removeRouter(Router router) throws RemoteException;
	
	public void assignHost(Router router, Computer computer) throws RemoteException;

	public void detachHost(Router router, Computer computer) throws RemoteException;

}
