package router;

import host.Computer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import algorithm.DistanceVector;
import algorithm.NetworkInfo;

public interface RouterInterface extends Remote {
	// configuration
	public void setNetworkInfo(NetworkInfo netinfo) throws RemoteException;
	public double getQueueAvgSize() throws RemoteException;
	public void setAvgInterval(int interval) throws RemoteException;
	public int getLostPackageCount() throws RemoteException;
	public double getLostPackageAvg() throws RemoteException;
	public void serverHalted() throws RemoteException;
	
	public void receive(pkg.Package pkg) throws RemoteException;
	public void connect(Computer computer) throws RemoteException;
	public void receiveDV(DistanceVector dv) throws RemoteException;
	public void hostHalted() throws RemoteException;
}
