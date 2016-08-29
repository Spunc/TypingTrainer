package persistence;

/**
 * <p>Create a copy of original database for testing purposes.
 * 
 * <p>Adds an additional table "test" with the following columns:
 * 	"id" (INTEGER PRIMARY KEY AUTOINCREMENT),
 * 	"str" (TEXT),
 * 	"int" (INTEGER).
 * The table is filled with two rows:
 * 	1, "row1", 100;
 * 	2, "row2", 200.
 * 
 * @author Lasse Osterhagen
 *
 */
public class CreateInitialTestTables {
	
	/**
	 * Name of the test database file.
	 */
	public static final String PATH2TESTDB = "src/test/resources/test.db";
	public static final String ROW1_STR = "row1";
	public static final int ROW1_INT = 100;
	public static final String ROW2_STR = "row2";
	public static final int ROW2_INT = 200;

	/**
	 * Create the test database file.
	 * @param args (not used)
	 */
	public static void main(String[] args) {
		CreateInitialTables.createTables(PATH2TESTDB);
		createAdditionalTables();
	}

	private static void createAdditionalTables() {
		DbBase db = DbAccess.getInstance();
		db.connect(PATH2TESTDB);
		createTestTable(db);
		db.disconnect();
	}
	
	private static void createTestTable(DbBase db) {
		db.executeUpdate("CREATE TABLE test ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "str TEXT, "
				+ "int INTEGER)");
		db.executeUpdatePrepStm("INSERT INTO test (str, int) VALUES(?,?)", p-> {
			p.setString(1, ROW1_STR);
			p.setInt(2, ROW1_INT);
			p.executeUpdate();
			p.setString(1, ROW2_STR);
			p.setInt(2, ROW2_INT);
			p.executeUpdate();
		});
	}

}
