package trainer;

/**
 * Data class that represents a key typed event during the execution of a training session.
 * Holds:
 * <ul>
 * 	<li>the typed character</li>
 * 	<li>the classification whether the typed character was correct</li>
 * </ul>
 * 
 * @author Lasse Osterhagen
 *
 */

public class KeyTypedEvent {
	
	/**
	 * The character that has been typed.
	 */
	public char c;
	
	/**
	 * <code>true</code> if the correct key was typed, otherwise <code>false</code>
	 */
	public boolean correct;
	
	/**
	 * Create a KeyTyedEvent.
	 * @param c the typed character
	 * @param correct the classification whether the typed character was correct
	 */
	public KeyTypedEvent(char c, boolean correct) {
		this.c = c;
		this.correct = correct;
	}
}
