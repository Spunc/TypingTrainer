package trainer;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>Class to monitor the performance statistics of a typist during a practice unit.
 * 
 * <p>The class keeps track of the performance statistics of every appearing character
 * that has to be typed in a <tt>Map&lt;Character,{@link PerformanceRate}&gt;</tt>. It
 * also provides a <tt>PerformanceRate</tt> for the total performance.
 * 
 * @author Lasse Osterhagen
 *
 */

public class PerformanceStats {
	
	private PerformanceRate totalPerformanceRate = new PerformanceRate();
	protected Map<Character, PerformanceRate> hits_errors = new TreeMap<Character, PerformanceRate>();
	protected Map<Character, Integer> wrongTyped = new TreeMap<Character, Integer>();
	
	/**
	 * Add a correctly typed char.
	 * @param c the char that was correctly typed
	 */
	public void addHit(char c) {
		totalPerformanceRate.addHit();
		hits_errors.putIfAbsent(c, new PerformanceRate());
		hits_errors.get(c).addHit();
	}
	
	/**
	 * Add an error. That is the char that should have been typed was missed and the
	 * typist typed a different char.
	 * @param c the char that should have been typed
	 */
	public void addError(char c) {
		totalPerformanceRate.addError();
		hits_errors.putIfAbsent(c, new PerformanceRate());
		hits_errors.get(c).addError();
	}
	
	/**
	 * Add a wrongly typed char. That is a char, that has been typed instead of
	 * the target char.
	 * @param c the wrongly typed char
	 */
	public void addWrongTyped(char c) {
		wrongTyped.compute(c, (k,v)-> v==null ? 0 : ++v);
	}
	
	/**
	 * Get the performance rate for all chars together
	 * @return the performance rate for all chars
	 */
	public PerformanceRate getTotalPerformanceRate() {
		return totalPerformanceRate;
	}
	
	/**
	 * Get the set of chars that appeared during the practice unit and their
	 * corresponding {@link PerformanceRate}s.
	 * @return the set of appeared chars and their corresponding <tt>PerformanceRate</tt>s
	 */
	public Set<Map.Entry<Character,PerformanceRate>> getHits_errors() {
		return Collections.unmodifiableSet(hits_errors.entrySet());
	}

}
