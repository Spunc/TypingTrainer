package trainer.lineCreators;

/**
 * This interface creates a line of words. A line must always end with a newline char.
 * The char before newline must not be a space.
 * 
 * LineCreator is the interface that must be implemented to provide different kinds of
 * texts that the typist should type.
 * 
 * @author Lasse Osterhagen
 *
 */

public interface LineCreator {
	
	/**
	 * Creates a line of words string.
	 * @param length the maximum length of <strong>printable</strong> chars.
	 * That means, the string may be one char longer than length, because the last
	 * char is always the newline char.
	 * @return the created line string. The last character is always the newline char.
	 */
	public String create(int length);
	
	/**
	 * To check if there are more lines to type.
	 * @return true if the <tt>LineCreator</tt> has at least one more line.
	 */
	public default boolean hasNext() {return true;}
	
	/**
	 * This method will be called by the interface user when the interface won't be used
	 * any longer. Override this method to release any open resources.
	 */
	public default void stop() {};
	
}
