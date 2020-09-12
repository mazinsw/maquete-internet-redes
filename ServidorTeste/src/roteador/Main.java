package roteador;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import servidor.Servidor;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String nome = "roteador" + getIntAleatorio() + "@" + getIP();
		System.out.println("Criando o roteador: " + nome);
		Roteador roteador = new Roteador(nome);
		try {
			Servidor servidor = (Servidor) Naming
					.lookup("//localhost/servidor");

			servidor.cadastrar(roteador);
			System.out.println("Digite algo para continuar...");
			Scanner in = new Scanner(System.in);
			in.next();
			List<Roteador> roteadores = servidor.getRoteadores();
			System.out.println("Imprimindo todos os roteadores:");
			for (Roteador r : roteadores) {
				System.out.println("Servidor: " + r.getNome());
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getIP() {
		try {
			java.net.InetAddress i = java.net.InetAddress.getLocalHost();
			String ip = i.getHostAddress();
			return ip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int getIntAleatorio() {
		Random gerador = new Random();

		return gerador.nextInt() % 100;
	}

}
