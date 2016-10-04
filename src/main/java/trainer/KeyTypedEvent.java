package trainer;

public class KeyTypedEvent {
	
	/**
	 * The character that has been typed.
	 */
	public char c;
	
	/**
	 * <code>true</code> if the correct key was typed, otherwise <code>false</code>
	 */
	public boolean correct;
	
	public KeyTypedEvent(char c, boolean correct) {
		this.c = c;
		this.correct = correct;
	}
}
