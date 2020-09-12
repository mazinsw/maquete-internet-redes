package test;

import host.Computer;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import router.RouterImpl;
import router.RouterListener;
import algorithm.NetworkInfo;

public class TestRouter extends Thread implements Runnable, RouterListener {
	private RouterImpl router;

	public TestRouter(String ip) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
		router = new RouterImpl(ip);
		System.out.println("Router started");
		router.addRouterListener(this);
		System.out.println("Digite algo para continuar...");
		Scanner in = new Scanner(System.in);
		in.next();
		router.halt();
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
		if (args.length != 1) {
			System.err.println("Error: Server IP address not found!");
			return;
		}
		new TestRouter(args[0]);
	}

	@Override
	public void receive(pkg.Package pkg) {
		String s = new String(pkg.getData());
		System.out.println("Package received! data: " + s);
	}

	@Override
	public void connect(Computer computer) {
		System.err.println("The computer \"" + computer.getName() + "\" has been associated with this router");
	}

	@Override
	public void serverHalted() {
		System.out.println("Server halted");
		try {
			router.halt();
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void hostHalted() {
		System.out.println("Host halted");		
	}

	@Override
	public void networkInfoChanged(NetworkInfo netinfo) {
		try {
			router.updateHostRouterPanel(netinfo);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
