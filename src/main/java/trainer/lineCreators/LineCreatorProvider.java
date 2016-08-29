package trainer.lineCreators;

import trainer.PerformanceStats;

/**
 * <p>An abstract factory to provide <tt>LineCreator</tt>s.
 * 
 * @author Lasse Osterhagen
 * @see LineCreator
 *
 */

public interface LineCreatorProvider {
	
	/**
	 * Provide an implementation for <tt>LineCreator</tt>.
	 * @param param gives access to a configuration parameter that can be used to specify
	 * the behavior of the <tt>LineCreator</tt>.
	 * @param ps gives access to the performance statistics of a running practice unit
	 * to allow adaptive behavior of the <tt>LineCreator</tt>.
	 * @return the <tt>LineCreator</tt> implementation.
	 * @see LineCreator
	 * @see PerformanceStats
	 */
	public LineCreator getLineCreator(String param, PerformanceStats ps);
	
	/**
	 * A short description of the <tt>LineCreator</tt> provided by this class.
	 * The description will be displayed by the dialog for choosing an exercise.
	 * @return a description for the <tt>LineCreator</tt>
	 */
	public String description();
	
	/**
	 * By default, the dialog that allows the user to choose an exercise will display the
	 * value of <i>param</i> in one column of the exercises table. Because <i>param</i> might be
	 * quite long, it is possible to override this function to provide a shorter version of
	 * <i>param</i>.
	 * @param param the original configuration parameter <tt>String</tt>.
	 * @return a shorter version of <tt>param</tt> or by default <tt>param</tt>.
	 */
	public default String shortParam(String param) {return param;}
	
}
