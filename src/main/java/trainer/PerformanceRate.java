package trainer;

/**
 * <p>Class to keep track of hits and errors. The class provides some basic statistics:
 * <ul>
 * <li>number of hits</li>
 * <li>number of errors</li>
 * <li>hit rate</li>
 * <li>error rate</li>
 * </ul>
 * 
 * <p>Within the typewriter program, every characters that appears during a practice
 * unit will be associated with one instance of this class. All <tt>PerformanceRate</tt>s
 * of a practice unit are managed by {@link PerformanceStats}.
 * 
 * @author Lasse Osterhagen
 *
 */
public class PerformanceRate {
	private int hits;
	private int errors;
	
	/**
	 * Default constructor initializes hits and errors with zero.
	 */
	public PerformanceRate() {
		hits = 0;
		errors = 0;
	}
	
	/**
	 * Construct a <code>PerformanceRate</code> with predefined number of hits and errors.
	 * @param hits number of hits
	 * @param errors number of errors
	 */
	public PerformanceRate(int hits, int errors) {
		this.hits = hits;
		this.errors = errors;
	}
	
	/**
	 * Add a hit, that is a correctly typed character.
	 */
	public void addHit() {
		++hits;
	}
	
	/**
	 * Add an error, that is another than the required character was typed.
	 */
	public void addError() {
		++errors;
	}
	
	/**
	 * Get the error rate.
	 * @return the error rate
	 */
	public double getErrorRate() {
		int total = hits + errors;
		if(total == 0) return 0;
		return (double) errors / total;
	}
	
	/**
	 * Get the hit rate.
	 * @return the hit rate
	 */
	public double getHitRate() {
		int total = hits + errors;
		if(total == 0) return 0;
		return (double) hits / total;
	}
	
	/**
	 * Get the total number of hits.
	 * @return the number of hits
	 */
	public int getHits() {
		return hits;
	}
	
	/**
	 * Get the total number of errors.
	 * @return the number of errors.
	 */
	public int getErrors() {
		return errors;
	}
	
	@Override
	public String toString() {
		return "[Hits=" + hits + ";Errors=" + errors + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof PerformanceRate)) return false;
		PerformanceRate other = (PerformanceRate) obj;
		return other.hits == this.hits && other.errors == this.errors;
	}
	
}
