package gui;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 * <p>This class provides a <tt>ComboModel</tt> for a <tt>JComboBox</tt>. Underneath the model lies a
 * data matrix that must be implemented by a <tt>List&lt;Object[]&gt;</tt> with elements of the
 * <tt>List</tt> containing the rows and <tt>Object[]</tt>, which must all have the same
 * length, containing the columns.
 * 
 * <p>During instantiation, one column of the table has to be
 * selected as the column to be displayed. But every entry of the table can later be retrieved
 * by calling {@link #getValue(int, int) getValue} method.
 * 
 * @author Lasse Osterhagen
 *
 */

public class MatrixComboBoxModel {
	
	private int displayColumn;
	private List<Object[]> rows;
	private ComboModel comboModel = new ComboModel();
	
	private class ComboModel implements ComboBoxModel<Object> {
		private int index = 0;

		@Override
		public Object getElementAt(int index) {
			return rows.get(index)[displayColumn];
		}

		@Override
		public int getSize() {
			return rows.size();
		}
		
		@Override
		public Object getSelectedItem() {
			if(index<0) return null;
			return rows.get(index)[displayColumn];
		}

		@Override
		public void setSelectedItem(Object obj) {
			for(int i=0; i<rows.size(); ++i) {
				if(obj.equals(rows.get(i)[displayColumn])) {
					index = i;
					break;
				}
			}	
		}
		
		@Override
		public void addListDataListener(ListDataListener arg0) {}
		@Override
		public void removeListDataListener(ListDataListener arg0) {}
	}
	
	/**
	 * Instantiate a MatrixComboBox.
	 * @param rows the content of the table. All <tt>Object[]</tt> must have the same size.
	 * @param displayColumn the column of the table to be displayed
	 */
	public MatrixComboBoxModel(List<Object[]> rows, int displayColumn) {
		this.rows = rows;
		this.displayColumn = displayColumn;
	}
	
	/**
	 * Get the implementation of <tt>ComboModel</tt>.
	 * @return the implementation of <tt>ComboModel</tt>
	 */
	public ComboModel getComboModel() {
		return comboModel;
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

}
