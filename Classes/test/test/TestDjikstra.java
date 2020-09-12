package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import router.Router;
import algorithm.Dijkstra;
import algorithm.Table;

public class TestDjikstra {

	private List<Router> nodes;
	private Dijkstra dijkstra;

	@Test
	public void testAll() {
		for (int i = 0; i < 6; i++) {
			testExcute3(i);
		}
	}

	public void testExcute3(int source) {
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

		/*
		 * u - 0 v - 1 w - 2 x - 3 y - 4 z - 5
		 */
		dijkstra = new Dijkstra();
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
		dijkstra.setSource(nodes.get(source));
		dijkstra.execute();
		Table table = dijkstra.getTable();
		for (int i = 0; i < nodes.size(); i++) {
			if (i == source)
				continue;
			System.out.println(nodes.get(source).getIP() + " -> "
					+ nodes.get(i).getIP() + " = "
					+ table.getNeighbor(nodes.get(i)).getIP());
		}
		System.out.println();
	}

	private void addLink(int sourceLocNo, int destLocNo, int duration) {
		dijkstra.addLink(nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
	}
}
