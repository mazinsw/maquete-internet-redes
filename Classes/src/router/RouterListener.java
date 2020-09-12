package router;

import host.Computer;
import algorithm.NetworkInfo;

public interface RouterListener {
	public void receive(pkg.Package pkg);
	public void connect(Computer computer);
	public void serverHalted();
	public void hostHalted();
	public void networkInfoChanged(NetworkInfo netinfo);

}
