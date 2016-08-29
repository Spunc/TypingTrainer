package persistence;

import java.util.ArrayList;

public class Util {
	
	/**
	 * <p>Create a matrix representation from a table of the database.
	 * 
	 * <p>The <code>ResultSet</code> obtained from a SQL select statement will
	 * be converted into a matrix of <code>Object</code>s. Such a matrix can for example
	 * be used by a {@link gui.MatrixTableModel} to display the contents of the
	 * <code>ResultSet</code>.
	 * 
	 * @param selectStatement SQL select statement
	 * @return the ResultsSet converted to a matrix of Objects
	 */
	public static ArrayList<Object[]> fillTableFromSelect(String selectStatement) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		DbAccess.getInstance().processResultSet(selectStatement, rs->{
			int columnSize = rs.getMetaData().getColumnCount();
			while(rs.next()) {
				Object[] columns = new Object[columnSize];
				for(int i=0; i<columnSize; ++i) {
					columns[i] = rs.getObject(i+1);
				}
				rows.add(columns);
			}
		});
		return rows;
	}
	
	/**
	 * Functional interface for transforming an <code>Object</code>
	 */
	@FunctionalInterface
	public static interface ObjectTransformer {
		Object transform(Object o);
	}
	
	/**
	 * <p>The same as {@link persistence.Util#fillTableFromSelect(String)}, but allows to
	 * transform column values.
	 * 
	 * <p>If a different representation of one or more columns of the matrix is desired,
	 * an array of {@link persistence.Util.ObjectTransformer} can be created. The size of
	 * the array must be equal to the number of columns in the matrix. Create an
	 * <code>ObjectTransformer</code> for each column whose values should be transformed.
	 * Assign the <code>ObjectTransformer</code>s to the corresponding indices of the array
	 * (attention: the index is the column number minus 1). For columns that should not
	 * be transformed, leave the array indices at <code>null</code>.
	 * 
	 * @param selectStatement SQL select statement
	 * @param ot array of ObjectTransformers, size equals column count of ResultSet
	 * @return the ResultsSet converted to a matrix of Objects
	 */
	public static ArrayList<Object[]> fillTableFromSelect(String selectStatement,
			ObjectTransformer[] ot) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		DbAccess.getInstance().processResultSet(selectStatement, rs->{
			int columnSize = rs.getMetaData().getColumnCount();
			while(rs.next()) {
				Object[] columns = new Object[columnSize];
				for(int i=0; i<columnSize; ++i) {
					if(ot[i] == null)
						columns[i] = rs.getObject(i+1);
					else
						columns[i] = ot[i].transform(rs.getObject(i+1));
				}
				rows.add(columns);
			}
		});
		return rows;
	}
	
}
