package host;

import algorithm.NetworkInfo;

public interface HostListener {
	public void receive(pkg.Package pkg);
	public void routerHalted();
	public void updateNetworkInfo(NetworkInfo netinfo);
}
