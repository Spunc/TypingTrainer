package gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * MatrixTable provides a <tt>TableModel</tt> for a <tt>JTable</tt>. Underneath the model lies a
 * data matrix that must be implemented by a <tt>List&lt;Object[]&gt;</tt> with elements of the
 * <tt>List</tt> containing the rows and <tt>Object[]</tt>, which must all have the same
 * length, containing the columns.
 * 
 * @author Lasse Osterhagen
 *
 */

public class MatrixTableModel {
	
	private int colBegin;
	private int colEnd;
	private String[] colLabels;
	private List<Object[]> rows;
	private TableModel tableModel = new TableModel();

	private class TableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			return colEnd-colBegin;
		}

		@Override
		public int getRowCount() {
			return rows.size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			return colLabels[columnIndex];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return rows.get(rowIndex)[columnIndex+colBegin];
		}
		
		@Override
	    public Class<?> getColumnClass(int columnIndex) {
	        return getValueAt(0, columnIndex).getClass();
	    }
		
	}
	
	/**
	 * Create a MatrixTable.
	 * @param rows the content of the table. All <tt>Object[]</tt> must have the same size.
	 * @param colBegin index of the first column that should be displayed (starting at 0)
	 * @param colEnd one behind the last column index that should be displayed
	 * @param colLabels the labels for the columns to be displayed
	 */
	public MatrixTableModel(List<Object[]> rows, int colBegin, int colEnd, String[] colLabels) {
		if(colEnd - colBegin != colLabels.length)
			throw new IllegalArgumentException("The number of column labels does not match "
					+ "the number of columns to be displayed.");
		this.colBegin = colBegin;
		this.colEnd = colEnd;
		this.colLabels = colLabels;
		setRows(rows);
	}
	
	/**
	 * Return the implementation of <tt>TableModel</tt> for a <tt>JTable</tt>
	 * @return the <tt>TableModel</tt> interface
	 */
	public TableModel getTableModel() {
		return tableModel;
	}
	
	/**
	 * Get the value of the underlying data matrix at the specified position
	 * @param row row index (starting at 0)
	 * @param column column index (starting at 0)
	 * @return the value
	 */
	public Object getValue(int row, int column) {
		return rows.get(row)[column];
	}
	
	/**
	 * Set a new data matrix. This will let the <tt>TableModel</tt> fire a
	 * <tt>fireTableDataChanged()</tt> event.
	 * @param rows the new data matrix
	 */
	public void setRows(List<Object[]> rows) {
		this.rows = rows;
		tableModel.fireTableDataChanged();
	}
	
}
