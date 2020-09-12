package gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class InteractiveTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3654537642803167580L;
	public static final int SOURCE_INDEX = 0;
	public static final int DESTINATION_INDEX = 1;
	public static final int DATA_INDEX = 2;
	public static final int FLAGS_INDEX = 3;
    public static final int HIDDEN_INDEX = 4;

	protected String[] columnNames;
	protected Vector<PackageRecord> dataVector;

	public InteractiveTableModel(String[] columnNames) {
		this.columnNames = columnNames;
		dataVector = new Vector<PackageRecord>();
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case SOURCE_INDEX:
		case DESTINATION_INDEX:
		case DATA_INDEX:
		case FLAGS_INDEX:
			return String.class;
		default:
			return Object.class;
		}
	}

	public Object getValueAt(int row, int column) {
		PackageRecord record = (PackageRecord) dataVector.get(row);
		switch (column) {
		case SOURCE_INDEX:
			return record.getSource();
		case DESTINATION_INDEX:
			return record.getDestination();
		case DATA_INDEX:
			return record.getData();
		case FLAGS_INDEX:
			return record.getFlags();
		default:
			return new Object();
		}
	}

	public void setValueAt(Object value, int row, int column) {
		PackageRecord record = (PackageRecord) dataVector.get(row);
		switch (column) {
		case SOURCE_INDEX:
			record.setSource((String)value);
			break;
		case DESTINATION_INDEX:
			record.setDestination((String)value);
			break;
		case DATA_INDEX:
			record.setData((String)value);
			break;
		case FLAGS_INDEX:
			record.setFlags((String)value);
			break;
		default:
			System.out.println("invalid index");
		}
		fireTableCellUpdated(row, column);
	}
	
	public void add(PackageRecord record) {
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
		PackageRecord record = (PackageRecord) dataVector.get(dataVector
				.size() - 1);
		if (record.getSource().trim().equals("")
				&& record.getDestination().trim().equals("")
				&& record.getSource().trim().equals("")
				&& record.getFlags().trim().equals("")) {
			return true;
		} else
			return false;
	}

	public void addEmptyRow() {
		dataVector.add(new PackageRecord());
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}
}