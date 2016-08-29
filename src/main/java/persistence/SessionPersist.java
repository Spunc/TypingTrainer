package persistence;

import java.time.Instant;
import java.util.ArrayList;

import trainer.PerformanceRate;

public class SessionPersist {
	
	/**
	 * Get all sessions from the database that belong to a single Exercise.
	 * Sessions will be sorted by their time stamps in <strong>descending</strong> order.
	 * The maximum number of records returned is limited to 1000.
	 * @param exerciseID the id of the Exercise as specified in the database
	 * @return all sessions of the Exercise that were completed
	 */
	public ArrayList<Session> getSessions(int exerciseID) {
		return getSessions(exerciseID, 1000);
	}
	
	/**
	 * Get the last <i>n</i> sessions for the specified Exercise from the database.
	 * Sessions will be sorted by their time stamps in <strong>descending</strong> order.
	 * @param exerciseID exerciseID the id of the Exercise as specified in the database
	 * @param n the maximum number of sessions to be returned. The actual number may be smaller.
	 * @return the last <i>n</i> session of the Exercise the were completed
	 */
	public ArrayList<Session> getSessions(int exerciseID, int n) {
		ArrayList<Session> result = new ArrayList<>();
		DbAccess.getInstance().processPrepResultSet(
				"SELECT timeStamp, numHits, numErrors, requiredTime FROM "
				+ "sessions WHERE idExercise=? ORDER BY timeStamp DESC LIMIT ?",
				pstm -> {
					pstm.setInt(1, exerciseID);
					pstm.setInt(2, n);
				},
				rs->{
					while(rs.next()) {
						Session s = new Session();
						s.timeStamp = rs.getLong(1);
						s.performanceRate = new PerformanceRate(rs.getInt(2), rs.getInt(3));
						s.requiredTime = rs.getLong(4);
						result.add(s);
					}
				}
				);
		return result;
	}
	
	
	/**
	 * <p>Save the results of a training session to the database.
	 * 
	 * <p>The {@link trainer.PracticeController PracticeController} will call this method
	 * at a regular end of a training session to save the performance results.
	 * 
	 * @param exerciseID ID of the performed <code>Exercise</code>
	 * @param pr the performance rate for the session
	 * @param requiredTime the time required to finish the exercise
	 */
	public void saveSession2DB(int exerciseID, PerformanceRate pr,
			long requiredTime) {
		DbAccess.getInstance().executeUpdatePrepStm(
				"INSERT INTO sessions"
				+ "(idExercise, timeStamp, numHits, numErrors, requiredTime) VALUES"
				+ "(?, ?, ?, ?, ?)",
				pstm->{
					pstm.setInt(1, exerciseID);
					pstm.setLong(2, Instant.now().toEpochMilli());
					pstm.setInt(3, pr.getHits());
					pstm.setInt(4, pr.getErrors());
					pstm.setLong(5, requiredTime);
					pstm.executeUpdate();
				});
	}

}
