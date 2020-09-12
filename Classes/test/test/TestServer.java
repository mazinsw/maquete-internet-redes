package test;

import host.Computer;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import router.Router;
import server.ServerImpl;
import server.ServerListener;

public class TestServer implements ServerListener {

	public TestServer() throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
		ServerImpl servidor = new ServerImpl();
		System.out.println("Server started");
		servidor.addServerListener(this);
		System.out.println("Digite algo para continuar...");
		Scanner in = new Scanner(System.in);
		in.next();
		servidor.halt();
	}

	public static void main(String[] args) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
		new TestServer();
	}

	@Override
	public void addRouter(Router router) {
		System.out.println("Router " + router.getIP() + " added");
	}

	@Override
	public void removeRouter(Router router) {
		System.out.println("Router " + router.getIP() + " removed");
	}

	@Override
	public void assignHost(Router router, Computer computer) {
		System.err.println("The computer \"" + computer.getName() + "\" has been associated with the router " + router.getIP());

	}

	@Override
	public void detachHost(Router router, Computer computer) {
		System.err.println("The computer \"" + computer.getName() + "\" has been detached from the router " + router.getIP());

	}

}
