package gui;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JDialog;
import javax.swing.JLabel;

import server.ServerImpl;

public class Relatorio extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1066367924686404042L;
	private JLabel labelQueueAvg;
	private JLabel labelLostAvg;
	private JLabel labelLostPkg;

	public Relatorio(ServerImpl servidor) {
		setSize(480, 320);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		JLabel label = new JLabel("Media da fila de pacotes: ");
		label.setBounds(10, 10, 160, 25);
		add(label);
		labelQueueAvg = new JLabel("0");
		labelQueueAvg.setBounds(140, 10, 100, 25);

		label = new JLabel("Media de pacotes perdidos: ");
		label.setBounds(10, 45, 160, 25);
		add(label);
		labelLostAvg = new JLabel("0");
		labelLostAvg.setBounds(150, 45, 100, 25);

		label = new JLabel("Quantidade de pacotes perdidos: ");
		label.setBounds(10, 80, 180, 25);
		add(label);
		labelLostPkg = new JLabel("0");
		labelLostPkg.setBounds(180, 80, 100, 25);
		add(labelQueueAvg);
		add(labelLostAvg);
		add(labelLostPkg);		
		try {
			labelQueueAvg.setText(Double.toString(servidor.getAllQueueAvgSize()));
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			labelLostAvg.setText(Double.toString(servidor.getAllLostPackageAvg()));
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			labelLostPkg.setText(Integer.toString(servidor.getAllLostPackageCount()));
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVisible(true);
	}
	
}
