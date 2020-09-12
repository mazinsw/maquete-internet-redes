package servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Servidor servidor = new ServidorImpl();
			Naming.rebind("//localhost/servidor", servidor);
		} catch (RemoteException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
