package gui;

import host.Computer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import router.Router;
import server.ServerImpl;
import server.ServerListener;
import algorithm.Link;
import algorithm.NetworkInfo;

public class Servidor extends JFrame implements RouterChangeListener,
		ServerListener, WindowListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private JPanel painelPrincipal;
	private RouterPanel painelDesenho;
	private JPanel painelOpcoes;
	private JPanel painelAlgoritmos;
	private JPanel painelAtualizar;
	private ServerImpl servidor;
	private JButton buttonAtualizarRoteadores;
	// NÃO PRECISA MECHER AQUI
	private ButtonGroup buttonGroup;

	// DIZ QUAL ALGORITMO DE ROTEAMENTO ESTÁ EM EXECUÇÃO
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JCheckBox checkBoxAutoAtualizar;

	private JButton buttonRelatorio;

	private JPanel painelRelatorio;



	public Servidor() throws RemoteException, MalformedURLException {
		super("Servidor");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentPane(getPainelPrincipal());
		// setResizable(false);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		servidor = new ServerImpl();
		servidor.addServerListener(this);
		addWindowListener(this);
		servidor.start();
		setVisible(true);
	}

	public JPanel getPainelPrincipal() {

		if (painelPrincipal == null) {

			painelPrincipal = new JPanel();
			GridBagLayout layout = new GridBagLayout();
			GridBagConstraints cons = new GridBagConstraints();
			painelPrincipal.setLayout(layout);

			cons.insets = new Insets(5, 5, 5, 5);
			cons.fill = GridBagConstraints.BOTH;
			cons.weightx = 1.0;
			cons.weighty = 1.0;

			cons.gridx = 0;
			cons.gridx = 0;
			painelPrincipal.add(getPainelDesenho(), cons);

			cons.anchor = GridBagConstraints.WEST;
			cons.fill = GridBagConstraints.NONE;
			cons.weightx = 0.0;
			cons.weighty = 0.0;

			cons.gridx = 1;
			cons.gridx = 0;
			painelPrincipal.add(getPainelOpcoes(), cons);
		}
		return painelPrincipal;
	}

	// ESSE VAI SER O PAINEL ONDE VAI FICAR O DESENHO DA TOPOLOGIA
	public RouterPanel getPainelDesenho() {

		if (painelDesenho == null) {
			painelDesenho = new RouterPanel();
			painelDesenho.setBorder(new TitledBorder(""));
			painelDesenho.addChangeListener(this);
		}
		return painelDesenho;
	}

	public JPanel getPainelOpcoes() {

		if (painelOpcoes == null) {
			painelOpcoes = new JPanel();
			painelOpcoes.setLayout(new BoxLayout(painelOpcoes, BoxLayout.X_AXIS));

			painelOpcoes.setBorder(new TitledBorder(""));
			painelOpcoes.add(getPainelAlgoritmos());
			painelOpcoes.add(getPainelRelatorio());
			painelOpcoes.add(getPainelAtualizar());
		}
		return painelOpcoes;

	}
	
	public JPanel getPainelAlgoritmos() {

		if (painelAlgoritmos == null) {
			painelAlgoritmos = new JPanel();
			painelAlgoritmos.setLayout(new BoxLayout(painelAlgoritmos,
					BoxLayout.Y_AXIS));

			painelAlgoritmos.setBorder(new TitledBorder(null, "Algoritmos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));

			getButtonGroup();

			painelAlgoritmos.add(getRadioButton1());
			painelAlgoritmos.add(getRadioButton2());
		}
		return painelAlgoritmos;

	}
	
	public JPanel getPainelRelatorio() {


		if (painelRelatorio == null) {
			painelRelatorio = new JPanel();
			painelRelatorio.setLayout(new BoxLayout(painelRelatorio,
					BoxLayout.X_AXIS));
			painelRelatorio.setBorder(new TitledBorder("Relatório"));
			painelRelatorio.add(getButtonRelatorio());
		}
		return painelRelatorio;

	}

	public JPanel getPainelAtualizar() {


		if (painelAtualizar == null) {
			painelAtualizar = new JPanel();
			painelAtualizar.setLayout(new BoxLayout(painelAtualizar,
					BoxLayout.X_AXIS));
			painelAtualizar.setBorder(new TitledBorder("Algoritmos"));
			painelAtualizar.add(getCheckBoxAutoAtualizar());
			painelAtualizar.add(getButtonAtualizarRoteadores());
		}
		return painelAtualizar;

	}

	public ButtonGroup getButtonGroup() {

		if (buttonGroup == null) {
			buttonGroup = new ButtonGroup();
			buttonGroup.add(getRadioButton1());
			buttonGroup.add(getRadioButton2());
		}
		return buttonGroup;
	}

	public JRadioButton getRadioButton1() {

		if (radioButton1 == null) {
			radioButton1 = new JRadioButton("Dijkstra");
			radioButton1.setSelected(true);
			radioButton1.addItemListener(this);
		}
		return radioButton1;
	}

	public JRadioButton getRadioButton2() {

		if (radioButton2 == null) {
			radioButton2 = new JRadioButton("Bellman–Ford");
			radioButton2.addItemListener(this);
		}
		return radioButton2;
	}

	public JCheckBox getCheckBoxAutoAtualizar() {

		if (checkBoxAutoAtualizar == null) {
			checkBoxAutoAtualizar = new JCheckBox("Atualizar automaticamente");
			checkBoxAutoAtualizar.setSelected(true);
			checkBoxAutoAtualizar.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					if (arg0.getStateChange() == ItemEvent.SELECTED) {
						buttonAtualizarRoteadores.setVisible(false);
					} else {
						buttonAtualizarRoteadores.setVisible(true);
					}
				}
			});
		}
		return checkBoxAutoAtualizar;
	}

	
	public JButton getButtonRelatorio(){
		
		if(buttonRelatorio == null){
			
			buttonRelatorio = new JButton("Gráficos");
			
			buttonRelatorio.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					new Relatorio(servidor);
				}
			});
		}
		return buttonRelatorio;
	}
	
	public JButton getButtonAtualizarRoteadores() {

		if (buttonAtualizarRoteadores == null) {
			buttonAtualizarRoteadores = new JButton("Atualizar roteadores");
			buttonAtualizarRoteadores.setVisible(false);
			buttonAtualizarRoteadores.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						servidor.setNetworkInfo(makeNetworkInfo());
					} catch (MalformedURLException | RemoteException | NotBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		return buttonAtualizarRoteadores;
	}

	public static void main(String[] args) throws RemoteException,
			MalformedURLException {
		// para exibir a janela basta chamar o contrutor;
		new Servidor();
	}

	public NetworkInfo makeNetworkInfo() {
		List<Link> links = new ArrayList<Link>();

		for (Edge edge : painelDesenho.getEdges()) {
			Router a = (Router) edge.getA().getTag();
			Router b = (Router) edge.getB().getTag();
			a.setX(edge.getA().getX());
			a.setY(edge.getA().getY());
			b.setX(edge.getB().getX());
			b.setY(edge.getB().getY());
			Link link = new Link(a, b, edge.getWeight());
			links.add(link);
		}
		if (radioButton1.isSelected())
			return new NetworkInfo(links, NetworkInfo.ALGORITHM_DIJKSTRA);
		else
			return new NetworkInfo(links, NetworkInfo.ALGORITHM_BELLMANFORD);
	}

	@Override
	public void weightChanged(Object tagA, Object tagB, double weight) {
		if(!checkBoxAutoAtualizar.isSelected())
			return;
		try {
			servidor.setNetworkInfo(makeNetworkInfo());
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void posChanged(Object tag, int x, int y) {
		if(!checkBoxAutoAtualizar.isSelected())
			return;
		try {
			servidor.setNetworkInfo(makeNetworkInfo());
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void edgeAdded(Object tagA, Object tagB, double weight) {
		if(!checkBoxAutoAtualizar.isSelected())
			return;
		try {
			servidor.setNetworkInfo(makeNetworkInfo());
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void edgeDeleted(Object tagA, Object tagB) {
		if(!checkBoxAutoAtualizar.isSelected())
			return;
		try {
			servidor.setNetworkInfo(makeNetworkInfo());
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void nodeDeleted(Object tag) {
		if(!checkBoxAutoAtualizar.isSelected())
			return;
		try {
			servidor.setNetworkInfo(makeNetworkInfo());
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addRouter(Router router) {
		try {
			painelDesenho.addNode(router.getIP(), router);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void removeRouter(Router router) {
		try {
			painelDesenho.removeNode(router);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void assignHost(Router router, Computer computer) {
		try {
			painelDesenho.setComputer(computer.getName(), router, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void detachHost(Router router, Computer computer) {
		try {
			painelDesenho.setComputer(router.getIP(), router, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		if (!((arg0.getSource() == radioButton1 && arg0.getStateChange() == ItemEvent.SELECTED) || (arg0
				.getSource() == radioButton2 && arg0.getStateChange() == ItemEvent.SELECTED))) {
			return;
		}
		if(!checkBoxAutoAtualizar.isSelected())
			return;
		try {
			servidor.setNetworkInfo(makeNetworkInfo());
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			servidor.halt();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nodeSelected(Object tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nodeUnSelected(Object tag) {
		// TODO Auto-generated method stub

	}

}
