package server;

import host.Computer;
import router.Router;

public interface ServerListener {
	
	public void addRouter(Router router);

	public void removeRouter(Router router);
	
	public void assignHost(Router router, Computer computer);

	public void detachHost(Router router, Computer computer);
}
