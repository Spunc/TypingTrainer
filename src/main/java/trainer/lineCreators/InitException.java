package trainer.lineCreators;

@SuppressWarnings("serial")
public class InitException extends Exception {
	
	public enum Type {
		MISSING_FILE,
		OTHER
	}
	
	private Type type;
	private String param;
	
	public InitException(Type type, String param) {
		super(type.toString() + ": " + param);
	}

	public Type getType() {
		return type;
	}

	public String getParam() {
		return param;
	}

}
