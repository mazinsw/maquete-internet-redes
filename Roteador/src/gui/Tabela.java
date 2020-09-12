package gui;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import router.Router;

import algorithm.Table;

public class Tabela extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2138581510318915957L;
	public static final String[] columnNames = {
        "Destino", "Vizinho", ""
    };
	
	private JTable table;
	private JScrollPane scroller;
	private RouterTableModel tableModel;

	public Tabela(Table table) {
		// TODO Auto-generated constructor stub
		setModal(true);
		setResizable(false);
		setLayout(null);
		setSize(480, 320);
		setLocationRelativeTo(null);
		JLabel label;
		if(table.getSource().getComputer() != null)
			label = new JLabel("Origem: " + table.getSource().getComputer().getName());
		else
			label = new JLabel("Origem: " + table.getSource().getIP());
		label.setBounds(200, 10, 200, 25);
		add(label);
		initComponent();
		String destination;
		String neighborStr;
		for (Router router : table.getRouters()) {
			Router neighbor = table.getNeighbor(router);
			if(neighbor.getComputer() != null)
				destination = neighbor.getComputer().getName();
			else
				destination = neighbor.getIP();
			if(neighbor.getComputer() != null)
				destination = neighbor.getComputer().getName();
			else
				destination = neighbor.getIP();
			if(router.getComputer() != null)
				neighborStr = router.getComputer().getName();
			else
				neighborStr = router.getIP();
			tableModel.add(new RouterRecord(destination, neighborStr));
		}
		setTitle("Tabela de roteamento");
		setVisible(true);
	}
	

	public void initComponent() {
        tableModel = new RouterTableModel(columnNames);
        tableModel.addTableModelListener(new Tabela.RouterTableModelListener());
        table = new JTable();
        table.setModel(tableModel);
        table.setSurrendersFocusOnKeystroke(true);
        if (!tableModel.hasEmptyRow()) {
            //tableModel.addEmptyRow();
        }

        scroller = new javax.swing.JScrollPane(table);
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
        TableColumn hidden = table.getColumnModel().getColumn(RouterTableModel.HIDDEN_INDEX);
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);
        hidden.setCellRenderer(new RouterRenderer(RouterTableModel.HIDDEN_INDEX));
        scroller.setBounds(10, 50, 455, 233);
        add(scroller);
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
	
	class RouterRenderer extends DefaultTableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = -2474363575755134016L;
		protected int interactiveColumn;

        public RouterRenderer(int interactiveColumn) {
            this.interactiveColumn = interactiveColumn;
        }

        public Component getTableCellRendererComponent(JTable table,
           Object value, boolean isSelected, boolean hasFocus, int row,
           int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == interactiveColumn && hasFocus) {
                if ((Tabela.this.tableModel.getRowCount() - 1) == row &&
                   !Tabela.this.tableModel.hasEmptyRow())
                {
                	Tabela.this.tableModel.addEmptyRow();
                }

                highlightLastRow(row);
            }

            return c;
        }
    }

    public class RouterTableModelListener implements TableModelListener {
        public void tableChanged(TableModelEvent evt) {
            if (evt.getType() == TableModelEvent.UPDATE) {
                int column = evt.getColumn();
                int row = evt.getFirstRow();
                table.setColumnSelectionInterval(column + 1, column + 1);
                table.setRowSelectionInterval(row, row);
            }
        }
    }
	
}
