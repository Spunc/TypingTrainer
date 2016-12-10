package trainer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

/**
 * This class checks the correctness of the typist's typing behavior, thereby
 * monitoring the progress of the practice unit.
 * 
 * <p>The class depends on a <code>PracticeController</code> that manages practice execution.
 * 
 * @author Lasse Osterhagen
 *
 */

public class LineMonitor extends Observable implements KeyListener {
	
	private String line;
	private int position;
	private PracticeController pc;
	private PerformanceStats performanceStats;

	public LineMonitor(PracticeController pc) {
		this.pc = pc;
		this.performanceStats = pc.getPerformanceStats();
	}
	
	/**
	 * Monitor a new string line that the typist should type.
	 * @param line the new string line.
	 */
	public void setLine(String line) {
		this.line = line;
		position = 0;
	}
	
	/**
	 * Get the current position within the string line.
	 * @return the current position
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Get the current char that needs to be typed next. If there is no char to be typed
	 * left, the <i>'\0'</i> will be returned.
	 * @return the char that needs to be typed next or <i>'\0'</i>
	 */
	public char getCurrentChar() {
		return position == line.length() ? '\0' : line.charAt(position);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// for the moment: do nothing
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// for the moment: do nothing
	}
	
	/**
	 * Checks whether the the typed char is correct and if so, advances the
	 * current char position by one. Notifies the {@link trainer.PracticeController} directly
	 * about a correctly typed char and requests a new line, if needed. Besides, notifies
	 * registered observers with a {@link trainer.KeyTypedEvent}. Actualizes the
	 * {@link trainer.PerformanceStats}.
	 * @param c the typed char
	 * @return true if typed char was correct, otherwise false
	 */
	public boolean advanceIfCorrect(char c) {
		// atm return type is used for test only
		boolean correct;
		setChanged();
		if(!(pc.getState() == PracticeController.State.RUNNING))
			throw new IllegalStateException("Illegal state: " + pc.getState());
		if(getCurrentChar() == c) {
			pc.incrementCorrectTypedChars();
			performanceStats.addHit(c);
			if(c == '\n') {
				pc.newLine();
			}
			else {
				++position;
			}
			notifyObservers(new KeyTypedEvent(c, true));
			correct = true;
		}
		else {
			performanceStats.addError(getCurrentChar());
			performanceStats.addWrongTyped(c);
			notifyObservers(new KeyTypedEvent(c, false));
			correct = false;
		}
		// This state happens only if at the end of an Exercise with LimitType.None
		if(position == line.length()) {
			pc.regStop();
		}
		return correct;
	}
	
	/**
	 * Handles keyboard input from the user. Will call {@link trainer.PracticeController#run()}
	 * if the <code>PracticeController</code> is in the <code>READY</code> state. Will then
	 * trigger the classification into correctly and wrongly typed chars.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		if(pc.getState() == PracticeController.State.READY)
			//start timer at first key press
			pc.run();	
		advanceIfCorrect(e.getKeyChar());
	}
}
