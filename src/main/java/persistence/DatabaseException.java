package persistence;

@SuppressWarnings("serial")
public class DatabaseException extends Exception {
	
	public DatabaseException(String msg) {
		super(msg);
	}
	
	public DatabaseException(String msg, Exception e) {
		super(msg, e);
	}
	
	public DatabaseException(Exception e) {
		super(e);
	}

}
