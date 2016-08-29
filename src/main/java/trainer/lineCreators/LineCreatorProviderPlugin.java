package trainer.lineCreators;

/**
 * <p>This interface can be implemented by external jars to allow additional implementations of
 * <tt>LineCreator</tt> to be added to the program.
 * 
 * <p>After creation of an external implementation for this interface, the jar has to be set on
 * the classpath during start-up. The implementation will be found within the program by
 * a call to <code>getName()</code>.
 * 
 * @author Lasse Osterhagen
 *
 */

public interface LineCreatorProviderPlugin extends LineCreatorProvider {
	
	/**
	 * Get the name of the provider.
	 * @return the name
	 */
	public String getName();
	
}
