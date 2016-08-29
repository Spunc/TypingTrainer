package persistence;


import trainer.PerformanceRate;

/**
 * <p>A session is a single run of an exercise that was completed regularly.
 * 
 * <p>After the exercise finishes, a record of the session will be written to the database.
 * The record holds information about the date of the session and performance statistics.
 * 
 * @author Lasse Osterhagen
 *
 */

final public class Session {
	
	long timeStamp;
	PerformanceRate performanceRate;
	long requiredTime;
	
	// Use package private access, because instances should only be created by SessionPersist
	Session() {}
	
	/**
	 * Get the time stamp of the Session as Unix time in seconds.
	 * @return the time stamp of the Session
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Get the <code>PerformanceRate</code> for this session.
	 * @return the PerformanceRate for this session.
	 */
	public PerformanceRate getPerformanceRate() {
		return performanceRate;
	}
	
	/**
	 * Get the required time until completion of the exercise in milliseconds.
	 * @return the required time in milliseconds
	 */
	public long getRequiredTime() {
		return requiredTime;
	}
	
	@Override
	public String toString() {
		return    "PerformanceRate=" + performanceRate + "\n"
				+ "RequiredTime=" + requiredTime + "\n"
				+ "TimeStamp=" + timeStamp;
	}
	
	
}
