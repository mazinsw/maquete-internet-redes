package test;

import host.HostImpl;
import host.HostListener;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import algorithm.NetworkInfo;

import common.Timer;
import common.TimerListener;

public class TestHost implements HostListener, TimerListener {
	private HostImpl host;
	private Timer timer;
	
	public TestHost() throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
		host = new HostImpl();
		System.out.println("Host started");
		host.addHostListener(this);
		timer = new Timer(1000, this);
		timer.start();
		System.out.println("Digite algo para continuar...");
		Scanner in = new Scanner(System.in);
		in.next();
		timer.cancel();
		host.halt();
	}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
		new TestHost();
	}

	@Override
	public void receive(pkg.Package pkg) {
		String s = new String(pkg.getData());
		System.out.println("Package received! data: " + s);
	}

	@Override
	public void onTimer() {
		String data = "Message sent by " + host.getComputer().getName();
		pkg.Package pkg = new pkg.Package(data.getBytes());
		try {
			host.sendPackage(pkg);
			System.out.println("Sent data: " + data);
			timer.reset();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void routerHalted() {
		timer.cancel();
		System.out.println("Router halted");
	}

	@Override
	public void updateNetworkInfo(NetworkInfo netinfo) {
		System.out.println("Update panel");
	}

}
