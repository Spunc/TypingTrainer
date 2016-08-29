package trainer;

/**
 * A null object pattern implementation for <tt>SimpleTimerInterface</tt>.
 * 
 * @author Lasse Osterhagen
 *
 */
public class NullStopWatch implements SimpleTimerInterface {
	
	/**
	 * Does nothing.
	 */
	@Override
	public void start() {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void stop() {}

}
