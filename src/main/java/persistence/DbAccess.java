package persistence;

public class DbAccess {
	
	public static DbBase getInstance() {
		return SqliteDbBase.getInstance();
	}
	
}

