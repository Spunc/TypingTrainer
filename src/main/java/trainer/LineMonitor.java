package trainer;

/**
 * This class checks the correctness of the typist's typing behavior, thereby
 * monitoring the progress of the practice unit.
 * 
 * It directly talks to a {@link trainer.PracticeController PracticeController}
 * to inform it about the number of correctly typed chars and requests for a new
 * line. Furthermore, it actualizes the {@link trainer.PerformanceStats
 * PerformanceStats} of the <tt>PracticeController</tt>.
 * 
 * @author Lasse Osterhagen
 *
 */

public class LineMonitor {
	
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
	
	public char getCurrentChar() {
		return line.charAt(position);
	}
	
	private boolean compareCurrentChar(char c) {
		return getCurrentChar() == c;
	}
	
	/**
	 * Checks whether the the typed char is correct and if so, advances the
	 * current char position by one. Notifies the <tt>PracticeController</tt>
	 * about correctly typed char and requests a new line, if needed.
	 * Also actualizes the <tt>PerformanceStats</tt>.
	 * @param c the typed char
	 * @return true if typed char was correct, otherwise false
	 */
	public boolean advanceIfCorrect(char c) {
		if(!(pc.getState() == PracticeController.State.RUNNING))
			throw new IllegalStateException("Illegal state: " + pc.getState());
		if(compareCurrentChar(c)) {
			pc.incrementCorrectTypedChars();
			performanceStats.addHit(c);
			if(c == '\n') {
				pc.newLine();
			}
			else {
				++position;
			}
			return true;
		}
		performanceStats.addError(getCurrentChar());
		performanceStats.addWrongTyped(c);
		return false;
	}
}
