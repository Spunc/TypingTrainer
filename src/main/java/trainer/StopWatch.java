package trainer;

import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * A wrapper for a <tt>javax.swing.Timer</tt> that implements
 * <tt>SimpleTimerInterface</tt>.
 * 
 * @author Lasse Osterhagen
 */

@SuppressWarnings("serial")
public class StopWatch extends Timer implements SimpleTimerInterface {

	public StopWatch(int delay, ActionListener listener) {
		super(delay, listener);
	}

}
