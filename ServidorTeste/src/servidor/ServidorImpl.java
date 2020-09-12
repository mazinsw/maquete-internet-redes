package servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import roteador.Roteador;

public class ServidorImpl extends UnicastRemoteObject implements Servidor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3017942086930533599L;

	private List<Roteador> roteadores;

	protected ServidorImpl() throws RemoteException {
		super();
		roteadores = new ArrayList<Roteador>();
	}

	@Override
	public boolean cadastrar(Roteador roteador) throws RemoteException {
		return roteadores.add(roteador);
	}

	@Override
	public List<Roteador> getRoteadores() throws RemoteException {
		List<Roteador> copia = new ArrayList<Roteador>();
		copia.addAll(roteadores);
		return copia;
	}

}
