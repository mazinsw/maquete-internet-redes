package gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class RouterTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3654537642803167580L;
	public static final int DESTINATION_INDEX = 0;
	public static final int NEIGHBOR_INDEX = 1;
    public static final int HIDDEN_INDEX = 2;

	protected String[] columnNames;
	protected Vector<RouterRecord> dataVector;

	public RouterTableModel(String[] columnNames) {
		this.columnNames = columnNames;
		dataVector = new Vector<RouterRecord>();
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case DESTINATION_INDEX:
		case NEIGHBOR_INDEX:
			return String.class;
		default:
			return Object.class;
		}
	}

	public Object getValueAt(int row, int column) {
		RouterRecord record = (RouterRecord) dataVector.get(row);
		switch (column) {
		case DESTINATION_INDEX:
			return record.getDestination();
		case NEIGHBOR_INDEX:
			return record.getNeighbor();
		default:
			return new Object();
		}
	}

	public void setValueAt(Object value, int row, int column) {
		RouterRecord record = (RouterRecord) dataVector.get(row);
		switch (column) {
		case DESTINATION_INDEX:
			record.setDestination((String)value);
			break;
		case NEIGHBOR_INDEX:
			record.setNeighbor((String)value);
			break;
		default:
			throw new RuntimeException("Invalid index: " + column);
		}
		fireTableCellUpdated(row, column);
	}
	
	public void add(RouterRecord record) {
		dataVector.add(record);
		int row = dataVector.size() - 1;
		fireTableRowsInserted(row, row);
	}

	public int getRowCount() {
		return dataVector.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public boolean hasEmptyRow() {
		if (dataVector.size() == 0)
			return false;
		RouterRecord record = (RouterRecord) dataVector.get(dataVector
				.size() - 1);
		if (record.getDestination().trim().equals("")
				&& record.getNeighbor().trim().equals("")) {
			return true;
		} else
			return false;
	}

	public void addEmptyRow() {
		dataVector.add(new RouterRecord("", ""));
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}
}