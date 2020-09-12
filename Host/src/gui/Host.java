package gui;

import host.HostImpl;
import host.HostListener;

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
import router.Router;
import algorithm.Link;
import algorithm.NetworkInfo;

import common.Timer;
import common.TimerListener;
import common.Util;


public class Host extends JFrame implements HostListener, WindowListener, TimerListener, RouterChangeListener {

	private static final long serialVersionUID = 1L;
	public static final String[] columnNames = {
        "Origem", "Destino", "Dados", "Status", ""
    };
	
	private JPanel painelPrincipal;
	
	private JLabel labelImagem;
	private JLabel labelQuantidadeDePacotes;

	private JLabel labelNomeDoHost;
	
	private JTextField textFieldQuantidadeDePacotes;
	private RouterPanel painelDesenho;

	private JTable table;
	private JScrollPane scroller;
	private InteractiveTableModel tableModel;
	
	private JButton buttonEnviar;
	private HostImpl host;
	private Timer timer;
	private int sendCount;
	private Router destination;
	
	public Host() throws RemoteException, MalformedURLException, NotBoundException {
		super("Host");
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
		timer = new Timer(10, this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(this);
		host = new HostImpl();
		host.addHostListener(this);
		labelNomeDoHost.setText(host.getComputer().getName());
		host.start();
		setVisible(true);
	}
	
	public void initComponent() {
        tableModel = new InteractiveTableModel(columnNames);
        tableModel.addTableModelListener(new Host.InteractiveTableModelListener());
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
                if ((Host.this.tableModel.getRowCount() - 1) == row &&
                   !Host.this.tableModel.hasEmptyRow())
                {
                	Host.this.tableModel.addEmptyRow();
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
			getLabelNomeDoHost().setBounds(40, 110, 121, 22);
			getLabelPacotes().setBounds(10, 140, 100, 22);			
			getTextFieldQuantidadeDePacotes().setBounds(10, 160, 100, 22);
			getButtonEnviar().setBounds(10, 200, 132, 22);
			getPainelDesenho().setBounds(150, 45, 633, 316);

			painelPrincipal.add(getPainelDesenho());
			painelPrincipal.add(getLabelImagem());
			painelPrincipal.add(getLabelPacotes());
			painelPrincipal.add(getTextFieldQuantidadeDePacotes());
			painelPrincipal.add(getLabelNomeDoHost());
			painelPrincipal.add(getButtonEnviar());
		}
		return painelPrincipal;
	}
	
	public RouterPanel getPainelDesenho() {

		if (painelDesenho == null) {
			painelDesenho = new RouterPanel();
			painelDesenho.setBorder(new TitledBorder(""));
			painelDesenho.setReadOnly(true);
			painelDesenho.addChangeListener(this);
		}
		return painelDesenho;
	}
	
	public JLabel getLabelImagem(){
		
		if(labelImagem == null){
			labelImagem = new JLabel("A imegem fica nessa label");
			ImageIcon imgIcon = getImageIcon("computer");
			Image img = imgIcon.getImage().getScaledInstance(
							(int) (imgIcon.getIconWidth() * 1),
							(int) (imgIcon.getIconHeight() * 1),
							Image.SCALE_SMOOTH);
			labelImagem.setIcon(new ImageIcon(img));
			labelImagem.setBounds(10, 0, img.getWidth(null), img.getHeight(null));

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

	public JLabel getLabelPacotes(){
		
		if(labelQuantidadeDePacotes == null){
			
			labelQuantidadeDePacotes = new JLabel("Pacotes");
		}
		return labelQuantidadeDePacotes;
	}
	
	public JLabel getLabelNomeDoHost(){
		
		if(labelNomeDoHost == null){
			
			labelNomeDoHost = new JLabel("Nome do Host");
		}
		return labelNomeDoHost;
	}
		
	public JTextField getTextFieldQuantidadeDePacotes(){
		
		if(textFieldQuantidadeDePacotes == null){
			textFieldQuantidadeDePacotes = new JTextField();
		}
		return textFieldQuantidadeDePacotes;
	}

	
	public JButton getButtonEnviar(){
		
		if(buttonEnviar == null){
			
			buttonEnviar = new JButton("Enviar");
			buttonEnviar.setEnabled(false);
			buttonEnviar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int qtd = 0;
					String str = textFieldQuantidadeDePacotes.getText();
					try {
						qtd = Integer.parseInt(str);
					} catch (Exception e) {
						return;
					}
					sendCount += qtd;
					if(qtd > 0) {
						destination = (Router)painelDesenho.getSelected();
						buttonEnviar.setEnabled(false);
						timer.start();
					}
				}
			});
		}
		return buttonEnviar;
	}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		new Host();
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
	public void updateNetworkInfo(NetworkInfo netinfo) {
		List<Link> links = netinfo.getLinks();
		painelDesenho.clear();
		buttonEnviar.setEnabled(false);
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

	public void close() {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}

	@Override
	public void routerHalted() {
		timer.cancel();
		close();
	}
	

	@Override
	public void onTimer() {
		if(sendCount == 0) {
			buttonEnviar.setEnabled(painelDesenho.getSelected() != null);
			return;
		}
		String data = "Message sent by " + host.getComputer().getName();
		pkg.Package pkg = new pkg.Package(data.getBytes());
		try {
			pkg.setSource(new Router(Util.getLocalIP()));
			pkg.setDestination(destination);
			host.sendPackage(pkg);
			PackageRecord record = new PackageRecord();
			record.setDestination(pkg.getDestination().getIP());
			record.setSource(pkg.getSource().getIP());
			record.setData(data);
			record.setFlags("Pacote enviado");
			tableModel.add(record);
			JScrollBar vertical = scroller.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
			sendCount--;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(sendCount > 0)
			timer.reset();
	}
	
	@Override
	public void nodeSelected(Object tag) {
		
		if(sendCount > 0)
			return;
		buttonEnviar.setEnabled(true);
	}

	@Override
	public void nodeUnSelected(Object tag) {
		buttonEnviar.setEnabled(false);
	}

	@Override
	public void weightChanged(Object tagA, Object tagB, double weight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void posChanged(Object tag, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edgeAdded(Object tagA, Object tagB, double weight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edgeDeleted(Object tagA, Object tagB) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nodeDeleted(Object tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			timer.cancel();
			host.halt();
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
