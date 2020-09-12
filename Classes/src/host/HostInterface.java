package host;

import java.rmi.Remote;
import java.rmi.RemoteException;

import algorithm.NetworkInfo;

public interface HostInterface extends Remote {
	public void receive(pkg.Package pkg) throws RemoteException;
	public void routerHalted() throws RemoteException;
	public void updateNetworkInfo(NetworkInfo netinfo) throws RemoteException;
}
