package persistence;

import java.sql.Statement;

import trainer.Exercise;

public class ExerciseCRUD {
	
	public static void createExercise(Exercise e) {
		DbAccess.getInstance().executeUpdatePrepStm(
				"INSERT INTO exercises"
				+ "(name, groupId, lineCreatorType, param, limitType, limitUnits) VALUES"
				+ "(?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS,
				pstm->{
					pstm.setString(1, e.getName());
					pstm.setInt(2, e.getExerciseGroup().getId());
					pstm.setString(3, e.getLineCreatorType());
					pstm.setString(4, e.getParam());
					pstm.setString(5, e.getLimitType().name());
					pstm.setInt(6, e.getLimitUnits());
					pstm.executeUpdate();
					e.setId(pstm.getGeneratedKeys().getInt(1));
				});
	}
	
	public static Exercise loadExercise(int id) {
		Exercise e = new Exercise();
		e.setId(id);
		DbAccess.getInstance().processPrepResultSet(
				"SELECT name, groupId, lineCreatorType, param, limitType, limitUnits "
				+ "FROM exercises WHERE id=?",
				pstm -> pstm.setInt(1, id),
				rs -> {
					if(!rs.next())
						throw new RuntimeException("Could not find record corresponding to id.");
					e.setName(rs.getString(1));
					e.setExerciseGroup(ExerciseGroupCRUD.loadExerciseGroup(rs.getInt(2)));
					e.setLineCreatorType(rs.getString(3));
					e.setParam(rs.getString(4));
					e.setLimitType(Exercise.LimitType.valueOf(rs.getString(5)));
					e.setLimitUnits(rs.getInt(6));
				});
		return e;
	}
	
	public static void updateExercise(Exercise e) {
		DbAccess.getInstance().executeUpdatePrepStm("UPDATE exercises SET name=?, groupId=?, "
				+ "lineCreatorType=?, param=?, limitType=?, limitUnits=? "
				+ "WHERE id=" + e.getId(),
				pstm->{
					pstm.setString(1, e.getName());
					pstm.setInt(2, e.getExerciseGroup().getId());
					pstm.setString(3, e.getLineCreatorType());
					pstm.setString(4, e.getParam());
					pstm.setString(5, e.getLimitType().name());
					pstm.setInt(6, e.getLimitUnits());
					pstm.executeUpdate();
				});
	}
	
	public static void deleteExercise(int id) {
		DbAccess.getInstance().executeUpdatePrepStm(
				"DELETE FROM exercises WHERE id=?",
				pstm-> {
					pstm.setInt(1, id);
					pstm.executeUpdate();
				});
	}
	
}
