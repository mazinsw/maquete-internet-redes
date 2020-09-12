package test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import router.Router;
import algorithm.BellmanFord;
import algorithm.DistanceVector;
import algorithm.Table;

public class TestBellmanFord {

	private List<Router> nodes;
	private BellmanFord bellmanFord;
	private HashMap<String, DistanceVector> dv;
	private HashMap<String, List<String>> ds;

	@Test
	public void testAll() throws MalformedURLException, RemoteException,
			NotBoundException {
		dv = new HashMap<String, DistanceVector>();
		ds = new HashMap<String, List<String>>();
		nodes = new ArrayList<Router>();
		/*Router location = new Router("u");
		nodes.add(location);
		location = new Router("v");
		nodes.add(location);
		location = new Router("w");
		nodes.add(location);
		location = new Router("x");
		nodes.add(location);
		location = new Router("y");
		nodes.add(location);
		location = new Router("z");
		nodes.add(location);*/
		Router location = new Router("A"); // 0
		nodes.add(location);
		location = new Router("B"); // 1
		nodes.add(location);
		location = new Router("C"); // 2
		nodes.add(location);
		location = new Router("D"); // 3
		nodes.add(location);
		location = new Router("E"); // 4
		nodes.add(location);
		location = new Router("F"); // 5
		nodes.add(location);
		for (int i = 0; i < nodes.size(); i++) {
			testExcute3(i);
		}
		List<String> ls = new ArrayList<String>();
		List<String> _ls = new ArrayList<String>();
		for (int i = 0; i < nodes.size(); i++)
			ls.add(nodes.get(i).getIP());
		boolean changed = true;
		while (changed) {
			changed = false;
			_ls.clear();
			for (String node : ls) {
				List<String> send = ds.get(node);
				DistanceVector sd = dv.get(node);
				for (String string : send) {
					DistanceVector dd = dv.get(string);
					if (dd.update(sd, true)) {
						_ls.add(string);
						changed = true;
					}
				}
			}
			List<String> temp = ls;
			ls = _ls;
			_ls = temp;
		}
		for (int source = 0; source < nodes.size(); source++) {
			DistanceVector sd = dv.get(nodes.get(source).getIP());
			Table table = sd.getTable(nodes.get(source), nodes);
			for (int i = 0; i < nodes.size(); i++) {
				if (i == source)
					continue;
				System.out.println(nodes.get(source).getIP() + " -> "
						+ nodes.get(i).getIP() + " = "
						+ table.getNeighbor(nodes.get(i)).getIP());
			}
			System.out.println();
		}
	}

	public void testExcute3(int source) throws MalformedURLException, RemoteException, NotBoundException {
		bellmanFord = new BellmanFord();
		/*addLink(0, 1, 2);
		addLink(0, 3, 1);
		addLink(0, 2, 5);
		addLink(1, 2, 3);
		addLink(1, 3, 2);
		addLink(2, 3, 3);
		addLink(2, 4, 1);
		addLink(2, 5, 5);
		addLink(5, 4, 2);
		addLink(4, 3, 1);*/
		addLink(0, 1, 4);
		addLink(0, 5, 6);
		addLink(0, 4, 2);
		addLink(4, 5, 3);
		addLink(5, 2, 1);
		addLink(5, 1, 1);
		addLink(1, 3, 3);
		addLink(2, 3, 1);
		List<String> send = new ArrayList<String>();
		DistanceVector d = new DistanceVector(nodes.get(source));
		d.init(bellmanFord.getRouterList(), bellmanFord.getLinkList(), true);
		for (int i = 0; i < nodes.size(); i++) {
			if (i == source)
				continue;
			send.add(nodes.get(i).getIP());
		}
		dv.put(nodes.get(source).getIP(), d);
		ds.put(nodes.get(source).getIP(), send);
	}

	private void addLink(int sourceLocNo, int destLocNo, int duration) {
		bellmanFord.addLink(nodes.get(sourceLocNo), nodes.get(destLocNo),
				duration);
	}
}
