package persistence;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import trainer.DefaultObjectFactory;
import trainer.Exercise;
import trainer.ExerciseGroup;
import trainer.PerformanceRate;

/**
 * Tests {@link persistence.ExerciseCRUD}, {@link persistence.ExerciseGroupCRUD}, and
 * {@link persistence.SessionPersist}.
 *  
 * @author Lasse Osterhagen
 *
 */

public class CRUDTest {
	
	private static DbBase db = DbAccess.getInstance();
	private static ArrayList<Integer> createdExerciseGroupIds = new ArrayList<>();
	private static ArrayList<Integer> createdExerciseIds = new ArrayList<>();
	
	@BeforeClass
	public static void setUp() {
		db.connect(CreateInitialTestTables.PATH2TESTDB);
	}
	
	private ExerciseGroup createNewExerciseGroup() {
		ExerciseGroup eGNew = DefaultObjectFactory.getExerciseGroup();
		ExerciseGroupCRUD.createExerciseGroup(eGNew);
		createdExerciseGroupIds.add(eGNew.getId());
		return eGNew;
	}
	
	private Exercise createNewExercise() {
		Exercise eNew = DefaultObjectFactory.getExercise();
		eNew.setExerciseGroup(createNewExerciseGroup());
		ExerciseCRUD.createExercise(eNew);
		createdExerciseIds.add(eNew.getId());
		return eNew;
	}
	
	@Test
	public void testNewExerciseGroup() {
		ExerciseGroup eGNew = createNewExerciseGroup();
		ExerciseGroup eGLoad = ExerciseGroupCRUD.loadExerciseGroup(eGNew.getId());
		assertEquals(eGNew.getName(), eGLoad.getName());
		assertEquals(eGNew.isEditable(), eGLoad.isEditable());
	}
	
	@Test
	public void testNewExercise() {
		Exercise eNew = createNewExercise();
		Integer ideNew = eNew.getId();
		assertNotNull(ideNew);
		Exercise eLoad = ExerciseCRUD.loadExercise(ideNew);
		assertEquals(eNew.getName(), eLoad.getName());
		assertEquals(eNew.getExerciseGroup().getName(), eLoad.getExerciseGroup().getName());
		assertEquals(eNew.getLineCreatorType(), eLoad.getLineCreatorType());
		assertEquals(eNew.getParam(), eLoad.getParam());
		assertEquals(eNew.getLimitType(), eLoad.getLimitType());
		assertEquals(eNew.getLimitUnits(), eLoad.getLimitUnits());
	}
	
	@Test
	public void testSession() throws InterruptedException {
		Exercise e = createNewExercise();
		PerformanceRate pr1 = new PerformanceRate(10, 2);
		PerformanceRate pr2 = new PerformanceRate(8, 5);
		final long requiredTime = 360_002;
		SessionPersist sp = new SessionPersist();
		sp.saveSession2DB(e.getId(), pr1, requiredTime);
		Thread.sleep(1); // to guarantee different timeStamps
		sp.saveSession2DB(e.getId(), pr2, requiredTime);
		ArrayList<Session> sessions = sp.getSessions(e.getId());
		assertEquals(sessions.size(), 2);
		assertEquals(sessions.get(0).getPerformanceRate(), pr2); // ORDER BY timeStamp DESC
		assertEquals(sessions.get(1).getPerformanceRate(), pr1);
	}
	
	@AfterClass
	public static void tearDown() {
		for(int id : createdExerciseIds)
			ExerciseCRUD.deleteExercise(id);
		for(int id : createdExerciseGroupIds)
			ExerciseGroupCRUD.deleteExerciseGroup(id);
		db.disconnect();
	}

}
