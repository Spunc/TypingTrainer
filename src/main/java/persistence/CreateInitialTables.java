package persistence;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import persistence.FillTableParser.TableParserException;

public class CreateInitialTables {
	
	private static DbBase db = SqliteDbBase.getInstance();
	
	static void makeTables() {
		db.executeUpdate("CREATE TABLE exerciseGroups ("
				+ "id INTEGER PRIMARY KEY NOT NULL, "
				+ "name TEXT, "
				+ "editable INTEGER NOT NULL)");
		db.executeUpdate("CREATE TABLE exercises ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "name TEXT, "
				+ "groupId INTEGER NOT NULL, "
				+ "lineCreatorType TEXT NOT NULL, "
				+ "param TEXT NOT NULL, "
				+ "limitType TEXT NOT NULL, "
				+ "limitUnits INTEGER, "
				+ "FOREIGN KEY (groupId) REFERENCES exerciseGroups(id))");
		db.executeUpdate("CREATE TABLE sessions (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"idExercise INTEGER, " +
				"timeStamp INTEGER NOT NULL, " +	//as unix epoch
				"numHits INTEGER NOT NULL, " +
				"numErrors INTEGER NOT NULL, " +
				"requiredTime INTEGER NOT NULL, " +
				"FOREIGN KEY (idExercise) REFERENCES exercises(id) ON DELETE CASCADE)");
		db.executeUpdate("CREATE TABLE sessionResults (" +
				"idSession INTEGER, " +
				"element CHARACTER(1) NOT NULL, " +
				"numHits INTEGER NOT NULL, " +
				"numErrors INTEGER NOT NULL," +
				"FOREIGN KEY (idSession) REFERENCES sessions(id) ON DELETE CASCADE)");
	}
	
	public static void createTables(String path2DB) {
		try(InputStreamReader r = new InputStreamReader(
				CreateInitialTables.class.getResourceAsStream("fillTablesScript.txt"),
				Constants.PROJECT_CHARSET)) {
			db.connect(path2DB);
			makeTables();
			FillTableParser ftp = new FillTableParser(db);
			ftp.parse(r);
		} catch (IOException | TableParserException | SQLException e) {
			e.printStackTrace();
		} finally {
			db.disconnect();
		}
	}
	
}
