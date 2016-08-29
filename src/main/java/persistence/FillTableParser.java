package persistence;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * <p>Class to parse a <tt>Reader</tt> that contains instructions how to fill tables in a relational
 * database.
 * 
 * <p>The format of the text file is as follows:
 * 
 * <p>{ table_name [ column_name_1, column_name_2, column_name_n ]
 * 		{ value_column_1, value_column_2, value_column_n }
 * 		{ value_column_1, value_column_2, value_column_n }
 * 		...
 * }
 * 
 * <p>If a string value includes space characters, the value has to be enclosed in quotation marks
 * (default = '"').
 * 
 * @author Lasse Osterhagen
 *
 */

public class FillTableParser {
	
	private char quoteChar = '"';
	private DbBase db;
	private StreamTokenizer tokenizer;
	
	/**
	 * Create a <tt>FillTableParser</tt> instance.
	 * @param db Reference to a connected <tt>DbBase</tt>
	 */
	public FillTableParser(DbBase db) {
		this.db = db;
	}
	
	/**
	 * Change the char that functions as quotation mark for string values.
	 * @param quoteChar The char that functions as quotation mark
	 */
	public void setQuoteChar(char quoteChar) {
		this.quoteChar = quoteChar;
	}
	
	/**
	 * A <tt>TableParserException</tt> is thrown by <tt>FillTableParser</tt> if the text
	 * file to be parsed does not adhere the specifications as mention in the introduction.
	 */
	@SuppressWarnings("serial")
	public static class TableParserException extends Exception {
		TableParserException(String msg) {
			super(msg);
		}
	}
	
	/**
	 * Parses the <tt>Reader</tt>
	 * @param reader <tt>Reader</tt> to be parsed. Can be a file, e.g.:
	 * 		{@code Reader r = new BufferedReader(new FileReader("fillTablesScript.txt"))) }
	 * @throws IOException Exception from Reader
	 * @throws TableParserException Reader content does not adhere to the specifications
	 * @throws SQLException Database error
	 */
	public void parse(Reader reader) throws IOException, TableParserException, SQLException {
		tokenizer = new StreamTokenizer(reader);
		tokenizer.wordChars('_', '_');
		tokenizer.quoteChar(quoteChar);
		
		// read table statement
		while(readExpectedCharOrEnd('{')) {
			String tableName = readWord();
			final ColumnNames columnData = readColumnNames();
			String selectStm = "SELECT " + columnData.columnStm + " FROM " + tableName;
			String prepStm = "INSERT INTO " + tableName + " (" + columnData.columnStm
					+ ") " + "VALUES"
					+ createPrepStmPlaceHolder(columnData.numColumns);
			// Determine columns sql types
			int[] columnSQLTypes = db.processResultSet2Val(selectStm, r-> {
				ResultSetMetaData meta = r.getMetaData();
				int[] types = new int[columnData.numColumns];
				for(int i=0; i<columnData.numColumns; ++i) {
					types[i] = meta.getColumnType(i+1);
				}
				return types;
			});
			// fill the table
			db.executeUpdatePrepStm(prepStm, p-> {
				try {
					// read row data
					while(readAlternativeChars('{', '}')) {
						Object[] columnValues = readColumnValues(columnData.numColumns);
						for(int i=0; i<columnData.numColumns; ++i) {
							p.setObject(i+1, columnValues[i], columnSQLTypes[i]);
						}
						p.executeUpdate();
					}
				} catch (IOException | TableParserException e) {
					throw new DatabaseException(e);
				}
			});
		}
		
	}
	
	// Create placeholder for PreparedStatement, e. g. "(?, ?, ?)"
	private static String createPrepStmPlaceHolder(int num) {
		char[] c_ar = new char[num*3];
		c_ar[0] = '(';
		c_ar[1] = '?';
		for(int i=1; i<num; ++i) {
			c_ar[3*i-1] = ',';
			c_ar[3*i] = ' ';
			c_ar[3*i+1] = '?';
		}
		c_ar[3*num-1] = ')';
		return new String(c_ar);
	}

	// Holds column names in comma separated string and a count variable for number of columns
	private final class ColumnNames {
		private final String columnStm;
		private final int numColumns;
		ColumnNames(String columnStm, int numColumns) {
			this.columnStm = columnStm;
			this.numColumns = numColumns;
		}
	}
	
	private ColumnNames readColumnNames() throws IOException, TableParserException {
		StringBuffer columnStm = new StringBuffer(64);
		readExpectedChar('[');
		columnStm.append(readWord());
		int numCols = 1;
		while(readAlternativeChars(',', ']')) {
			columnStm.append(", ");
			columnStm.append(readWord()); // read more column names
			++numCols;
		}
		return new ColumnNames(columnStm.toString(), numCols);
	}
	
	private Object[] readColumnValues(int expectedColumns) throws IOException, TableParserException {
		Object[] vals = new Object[expectedColumns];
		vals[0] = readObject();
		int numReadVals = 1;
		while(readAlternativeChars(',' , '}')) {
			vals[numReadVals] = readObject();
			++numReadVals;
			if(numReadVals > expectedColumns) // more values provided than columns specified
				throw new RuntimeException(singleCharErrorMsg('}'));
		}
		return vals;
	}
	
	private Object readObject() throws IOException, TableParserException {
		int tokenType = tokenizer.nextToken();
		if(tokenType == StreamTokenizer.TT_WORD || tokenType == quoteChar)
			return tokenizer.sval;
		if(tokenType == StreamTokenizer.TT_NUMBER)
			return tokenizer.nval;
		throw new TableParserException("Expected word or number token " + getDetailedError());
	}
	
	private String readWord() throws IOException, TableParserException {
		int tokenType = tokenizer.nextToken();
		if(tokenType == StreamTokenizer.TT_WORD || tokenType == quoteChar)
			return tokenizer.sval;
		throw new TableParserException("Expected word token " + getDetailedError());
	}
	
	private boolean readAlternativeChars(char alt1, char alt2) throws IOException, TableParserException {
		if( tokenizer.nextToken() == StreamTokenizer.TT_EOF ||
				( !(tokenizer.ttype == alt1) && !(tokenizer.ttype == alt2) ) )
			throw new TableParserException("Expected chars: '" + alt1 + "' or '" + alt2 + "' "
					+ getDetailedError());
		return tokenizer.ttype == alt1;
	}
	
	private void readExpectedChar(char c) throws IOException, TableParserException {
		if( tokenizer.nextToken() == StreamTokenizer.TT_EOF ||
				( !(tokenizer.ttype == c) ) )
			throw new TableParserException(singleCharErrorMsg(c));
	}
	
	private boolean readExpectedCharOrEnd(char c) throws IOException, TableParserException {
		if(tokenizer.nextToken() == StreamTokenizer.TT_EOF)
			return false;
		if(tokenizer.ttype == c)
			return true;
		else
			throw new TableParserException(singleCharErrorMsg(c));
	}
	
	private String singleCharErrorMsg(char c) {
		return "Expected char: '" + c + "' " + getDetailedError();
	}
	
	private String getDetailedError() {
		return "not found; found: " + tokenizer.toString();
	}
	
}
