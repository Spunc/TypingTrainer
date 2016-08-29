package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

public class SqliteDbBase implements DbBase {
	
	private Connection con;
	
	// Singleton pattern
	private static SqliteDbBase instance = new SqliteDbBase();	
	public static SqliteDbBase getInstance() {return instance;}
	private SqliteDbBase() {}
	
	@Override
	public void connect(String path2DB) {
		try {
			//Class.forName("org.sqlite.JDBC");
			SQLiteConfig config = new SQLiteConfig();  
	        config.enforceForeignKeys(true);
			con = DriverManager.getConnection("jdbc:sqlite:" + path2DB, config.toProperties());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void disconnect() {
		if(con != null) {
			try {
				if(!con.isClosed())
					con.close();
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@Override
	public int executeUpdate(String sql) {
		int numChanged = 0;
		try (Statement stat = con.createStatement()) {
			numChanged = stat.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return numChanged;
	}
	
	@Override
	public void executeUpdatePrepStm(String sql, PstmProcessor pstmProcessor) {
		executeUpdatePrepStm(sql, Statement.NO_GENERATED_KEYS, pstmProcessor);
	}
	
	@Override
	public void executeUpdatePrepStm(String sql, int autoGeneratedKeys,
			PstmProcessor pstmProcessor) {
		try (PreparedStatement pstm = con.prepareStatement(sql, autoGeneratedKeys)){
			con.setAutoCommit(false);
			pstmProcessor.process(pstm);
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException | DatabaseException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void processResultSet(String sqlSelect, ResultSetProcessor rsProcessor) {
		try(ResultSet rs = con.createStatement().executeQuery(sqlSelect)) {
			rsProcessor.process(rs);
		} catch (SQLException | DatabaseException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public <T> T processResultSet2Val(String sqlSelect,
			ResultSetReturnProcessor<T> rsReturnProcessor) {
		T returnVal = null;
		try(ResultSet rs = con.createStatement().executeQuery(sqlSelect)) {
			returnVal = rsReturnProcessor.process(rs);
		} catch (SQLException | DatabaseException e) {
			throw new RuntimeException(e);
		}
		return returnVal;
	}
	
	@Override
	public void processPrepResultSet(String sqlSelect, PstmProcessor pstmProcessor,
			ResultSetProcessor rsProcessor) {
		try(PreparedStatement pstm = con.prepareStatement(sqlSelect)) {
			pstmProcessor.process(pstm);
			try(ResultSet rs = pstm.executeQuery()) {
				rsProcessor.process(rs);
			}
		} catch (SQLException | DatabaseException e) {
			throw new RuntimeException(e);
		}
		
	}
	@Override
	public <T> T processPrepResultSet2Val(String sqlSelect, PstmProcessor pstmProcessor,
			ResultSetReturnProcessor<T> rsReturnProcessor) {
		T returnVal = null;
		try(PreparedStatement pstm = con.prepareStatement(sqlSelect)) {
			pstmProcessor.process(pstm);
			try(ResultSet rs = pstm.executeQuery()) {
				returnVal = rsReturnProcessor.process(rs);
			}
		} catch (SQLException | DatabaseException e) {
			throw new RuntimeException(e);
		}
		return returnVal;
	}
	
}