package gui;

import host.Computer;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import pkg.Package;
import router.RouterImpl;
import router.RouterListener;
import algorithm.Link;
import algorithm.NetworkInfo;


public class Roteador extends JFrame implements RouterListener, WindowListener {

	private static final long serialVersionUID = 1L;
	public static final String[] columnNames = {
        "Source", "Destination", "Data", "Flags", ""
    };
	
	private JPanel painelPrincipal;
	private RouterPanel painelDesenho;

	private JLabel labelImagem;
	private JLabel labelTamanhoDaFila;
	private JLabel labelTempoProcessamento;
	private JLabel labelAlgoritmo;
	
	private JTable table;
	private JScrollPane scroller;
	private InteractiveTableModel tableModel;
	
	private JTextField textFieldTamanhoDaFila;
	private JTextField textFieldTempoProcessamento;
	
	private JButton buttonTabela;
	private JButton buttonAjustar;
	private RouterImpl router;
	
	public Roteador(String ip) throws RemoteException, MalformedURLException, NotBoundException {
		super("Roteador");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentPane(getPainelPrincipal());
		initComponent();
		setResizable(false);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		router = new RouterImpl(ip);
		router.addRouterListener(this);
		addWindowListener(this);
		router.start();
		setVisible(true);
	}
	
	public void initComponent() {
        tableModel = new InteractiveTableModel(columnNames);
        tableModel.addTableModelListener(new Roteador.InteractiveTableModelListener());
        table = new JTable();
        table.setModel(tableModel);
        table.setSurrendersFocusOnKeystroke(true);
        if (!tableModel.hasEmptyRow()) {
            //tableModel.addEmptyRow();
        }

        scroller = new javax.swing.JScrollPane(table);
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
        TableColumn hidden = table.getColumnModel().getColumn(InteractiveTableModel.HIDDEN_INDEX);
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);
        hidden.setCellRenderer(new InteractiveRenderer(InteractiveTableModel.HIDDEN_INDEX));
        scroller.setBounds(10, 370, 775, 192);
        painelPrincipal.add(scroller);
    }
	
	public void highlightLastRow(int row) {
        int lastrow = tableModel.getRowCount();
        if (row == lastrow - 1) {
            table.setRowSelectionInterval(lastrow - 1, lastrow - 1);
        } else {
            table.setRowSelectionInterval(row + 1, row + 1);
        }

        table.setColumnSelectionInterval(0, 0);
    }
	
	class InteractiveRenderer extends DefaultTableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = -2474363575755134016L;
		protected int interactiveColumn;

        public InteractiveRenderer(int interactiveColumn) {
            this.interactiveColumn = interactiveColumn;
        }

        public Component getTableCellRendererComponent(JTable table,
           Object value, boolean isSelected, boolean hasFocus, int row,
           int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == interactiveColumn && hasFocus) {
                if ((Roteador.this.tableModel.getRowCount() - 1) == row &&
                   !Roteador.this.tableModel.hasEmptyRow())
                {
                	Roteador.this.tableModel.addEmptyRow();
                }

                highlightLastRow(row);
            }

            return c;
        }
    }

    public class InteractiveTableModelListener implements TableModelListener {
        public void tableChanged(TableModelEvent evt) {
            if (evt.getType() == TableModelEvent.UPDATE) {
                int column = evt.getColumn();
                int row = evt.getFirstRow();
                System.out.println("row: " + row + " column: " + column);
                table.setColumnSelectionInterval(column + 1, column + 1);
                table.setRowSelectionInterval(row, row);
            }
        }
    }
	
	
	public JPanel getPainelPrincipal(){

		if(painelPrincipal == null){
			painelPrincipal = new JPanel(null);
			
			getLabelImagem();
			getLabelTamanhoDaFila().setBounds(10, 117, 100, 22);
			getLabelTempoDeProcessamento().setBounds(10, 183, 162, 22);
			getTextFieldTamanhoDaFila().setBounds(10, 150, 100, 22);
			getTextFieldTempoDeProcessamento().setBounds(10, 216, 100, 22);
			getLabelAlgoritmo().setBounds(369, 10, 215, 22);
			getButtonAjustar().setBounds(10, 250, 132, 22);
			getButtonTabela().setBounds(10, 339, 132, 22);
			getPainelDesenho().setBounds(150, 45, 633, 316);
			
			painelPrincipal.add(getPainelDesenho());
			painelPrincipal.add(getLabelImagem());
			painelPrincipal.add(getLabelTamanhoDaFila());
			painelPrincipal.add(getLabelTempoDeProcessamento());
			painelPrincipal.add(getTextFieldTamanhoDaFila());
			painelPrincipal.add(getTextFieldTempoDeProcessamento());
			painelPrincipal.add(getLabelAlgoritmo());
			painelPrincipal.add(getButtonAjustar());
			painelPrincipal.add(getButtonTabela());
		}
		return painelPrincipal;
	}
	
	public RouterPanel getPainelDesenho() {

		if (painelDesenho == null) {
			painelDesenho = new RouterPanel();
			painelDesenho.setBorder(new TitledBorder(""));
			painelDesenho.setReadOnly(true);
		}
		return painelDesenho;
	}
	
	public JLabel getLabelImagem(){
		
		if(labelImagem == null){
			labelImagem = new JLabel();
			ImageIcon imgIcon = getImageIcon("router");
			Image img = imgIcon.getImage().getScaledInstance(
							(int) (imgIcon.getIconWidth() * 0.2),
							(int) (imgIcon.getIconHeight() * 0.2),
							Image.SCALE_SMOOTH);
			labelImagem.setIcon(new ImageIcon(img));
			labelImagem.setBounds(10, 10, img.getWidth(null), img.getHeight(null));
		}
		return labelImagem;
	}
	
	public ImageIcon getImageIcon(String name) {
		try {
			return new ImageIcon(getClass()
					.getResource("/img/" + name + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JLabel getLabelTamanhoDaFila(){
		
		if(labelTamanhoDaFila == null){
			
			labelTamanhoDaFila = new JLabel("Tamanho da Fila");
		}
		return labelTamanhoDaFila;
	}
	
	public JLabel getLabelTempoDeProcessamento(){
		
		if(labelTempoProcessamento == null){
			
			labelTempoProcessamento = new JLabel("Tempo de processamento");
		}
		return labelTempoProcessamento;
	}
	
	public JLabel getLabelAlgoritmo(){
		
		if(labelAlgoritmo == null){
			
			labelAlgoritmo = new JLabel("Algoritmo em uso no roteador");
		}
		return labelAlgoritmo;
	}
		
	public JTextField getTextFieldTamanhoDaFila(){
		
		if(textFieldTamanhoDaFila == null){
			textFieldTamanhoDaFila = new JTextField();
			textFieldTamanhoDaFila.setText("1024");
		}
		return textFieldTamanhoDaFila;
	}
	
	public JTextField getTextFieldTempoDeProcessamento(){
		
		if(textFieldTempoProcessamento == null){
			textFieldTempoProcessamento = new JTextField();
			textFieldTempoProcessamento.setText("500");
		}
		return textFieldTempoProcessamento;
	}
	
	public JButton getButtonTabela(){
		
		if(buttonTabela == null){
			
			buttonTabela = new JButton("Exibir Tabela");
			
			buttonTabela.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(router.getTable() != null)
						new Tabela(router.getTable());
				}
			});
		}
		return buttonTabela;
	}
	
	public JButton getButtonAjustar(){
		
		if(buttonAjustar == null){
			
			buttonAjustar = new JButton("Ajustar");
			
			buttonAjustar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//FICA AQUI A AÇÃO DO BOTÃO
					int tamFila = 0;
					String str = textFieldTamanhoDaFila.getText();
					try {
						tamFila = Integer.parseInt(str);
						router.setMaxQueueSize(tamFila);
					} catch (Exception e) {
						return;
					}
					int tempo = 0;
					str = textFieldTempoProcessamento.getText();
					try {
						tempo = Integer.parseInt(str);
						router.setTimeProcessing(tempo);
					} catch (Exception e) {
						return;
					}

				}
			});
		}
		return buttonAjustar;
	}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		if (args.length < 1) {
			System.err.println("Error: Server IP address not found!");
			return;
		}
		new Roteador(args[0]);
	}

	public void close() {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}

	@Override
	public void receive(Package pkg) {
		String data = new String(pkg.getData());
		PackageRecord record = new PackageRecord();
		record.setDestination(pkg.getDestination().getIP());
		record.setSource(pkg.getSource().getIP());
		record.setData(data);
		record.setFlags("Pacote recebido");
		tableModel.add(record);
		JScrollBar vertical = scroller.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}


	@Override
	public void networkInfoChanged(NetworkInfo netinfo) {
		try {
			router.updateHostRouterPanel(netinfo);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Link> links = netinfo.getLinks();
		painelDesenho.clear();
		for (Link link : links) {
			Node a = new Node();
			a.setComputer(link.getRouterA().getComputer() != null);
			a.setX(link.getRouterA().getX());
			a.setY(link.getRouterA().getY());
			a.setTag(link.getRouterA());
			if(a.hasComputer())
				a.setText(link.getRouterA().getComputer().getName());
			else				
				a.setText(link.getRouterA().getIP());
			Node b = new Node();
			b.setComputer(link.getRouterB().getComputer() != null);
			b.setX(link.getRouterB().getX());
			b.setY(link.getRouterB().getY());
			b.setTag(link.getRouterB());
			if(b.hasComputer())
				b.setText(link.getRouterB().getComputer().getName());
			else				
				b.setText(link.getRouterB().getIP());
			try {
				painelDesenho.addEdge(a, b, link.getWeight());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void connect(Computer computer) {
		try {
			painelDesenho.setComputer(computer.getName(), router.getRouter(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void serverHalted() {
		close();
	}


	@Override
	public void hostHalted() {
		try {
			painelDesenho.setComputer(router.getRouter().getIP(), router.getRouter(), false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			router.halt();
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

}
