package servidor;

import java.rmi.Remote;
import java.util.List;

import roteador.Roteador;

public interface Servidor extends Remote {
	public boolean cadastrar(Roteador roteador) throws java.rmi.RemoteException;
	public List<Roteador> getRoteadores() throws java.rmi.RemoteException;
}