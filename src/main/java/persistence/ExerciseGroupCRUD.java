package persistence;

import java.sql.Statement;

import trainer.ExerciseGroup;

public class ExerciseGroupCRUD {
	
	public static void createExerciseGroup(ExerciseGroup e) {
		DbAccess.getInstance().executeUpdatePrepStm(
				"INSERT INTO exerciseGroups"
				+ "(name, editable) VALUES(?,?)",
				Statement.RETURN_GENERATED_KEYS,
				pstm->{
					pstm.setString(1, e.getName());
					pstm.setInt(2, e.isEditable() ? 1 : 0);
					pstm.executeUpdate();
					e.setId(pstm.getGeneratedKeys().getInt(1));
				});
	}
	
	public static ExerciseGroup loadExerciseGroup(int id) {
		ExerciseGroup e = new ExerciseGroup();
		e.setId(id);
		DbAccess.getInstance().processPrepResultSet(
				"SELECT name, editable FROM exerciseGroups WHERE id=?",
				pstm -> pstm.setInt(1, id),
				rs -> {
					if(!rs.next())
						throw new RuntimeException("Could not find record corresponding to id.");
					e.setName(rs.getString(1));
					e.setEditable(rs.getInt(2) == 1);
				});
		return e;
	}
	
	public static void deleteExerciseGroup(int id) {
		DbAccess.getInstance().executeUpdatePrepStm(
				"DELETE FROM exerciseGroups WHERE id=?",
				pstm-> {
					pstm.setInt(1, id);
					pstm.executeUpdate();
				});
		DbAccess.getInstance().executeUpdate("DELETE FROM exerciseGroups WHERE id=" + id);
	}

}
