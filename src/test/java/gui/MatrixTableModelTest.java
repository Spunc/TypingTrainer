package gui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;

/**
 * Test filling a <tt>MatrixTableModel</tt> with data.
 * 
 * @author Lasse Osterhagen
 *
 */

public class MatrixTableModelTest {
	
	private List<Object[]> testList;
	private Object[] row1;
	private Object[] row2;
	// a MatrixTablelModel which displays the columns at indices 1 and 2 (column 2 and 3)
	private MatrixTableModel mtm;
	
	@Before
	public void initTestList() {
		row1 = new Object[] {1, "r1c1", 11};
		row2 = new Object[] {2, "r2c1", 21};
		testList = new ArrayList<>();
		testList.add(row1);
		testList.add(row2);
		mtm = new MatrixTableModel(testList, 1, 3, new String[] {"str", "int"});
	}
	
	/**
	 * Test for correct values to be retrieved.
	 */
	@Test
	public void testValues() {
		assertTrue(mtm.getValue(0, 0).equals(row1[0]));
		assertTrue(mtm.getValue(1, 1).equals(row2[1]));
	}
	
	/**
	 * Test for correct values to be displayed by the <tt>TableModel</tt> of
	 * <tt>MatrixTableModel</tt>
	 */
	@Test
	public void testTableModelValues() {
		TableModel tm = mtm.getTableModel();
		assertTrue(tm.getValueAt(0, 1).equals(row1[2]));
		assertTrue(tm.getValueAt(1, 0).equals(row2[1]));
	}
	
}
