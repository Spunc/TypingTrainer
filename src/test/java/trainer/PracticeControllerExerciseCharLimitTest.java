package trainer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import persistence.SessionPersist;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;
import trainer.lineCreators.InitException;

public class PracticeControllerExerciseCharLimitTest {
	
	static final int charLimit = 100;
	static final int maxLineLength = 20;
	
	private Exercise exercise;
	private @Mock SessionPersist sessionPersist;
	private @InjectMocks PracticeController pc;
	private LineMonitor lm;
	
	private int countCorrect;
	
	private void init() throws ImplementationNotFound, InitException {
		exercise = DefaultObjectFactory.getExercise();
		exercise.setLimitType(Exercise.LimitType.CHARS);
		exercise.setLimitUnits(charLimit);
		pc = new PracticeController(exercise, maxLineLength);
		MockitoAnnotations.initMocks(this);
		lm = pc.getLineMonitor();
	}
	
	@Before
	public void initAndRun() throws ImplementationNotFound, InitException {
		init();
		pc.ready();
		pc.run();
		// Run the exercise as long as pc has not stopped or number of typed chars
		// exceeds the number of allowed chars by 10
		while(pc.getState() == PracticeController.State.RUNNING ||
				countCorrect > charLimit+10) {
			char[] testChars = pc.getLine1().toCharArray();
			for(int i=0; i<testChars.length; ++i) {
				// Simulate a wrongly typed char
				lm.advanceIfCorrect( (char) (testChars[i]+1) );
				// Simulate a correctly typed char
				lm.advanceIfCorrect(testChars[i]);
				++countCorrect;
				if(pc.getState() == PracticeController.State.REG_STOPPED)
					break;
			}
		}
	}
	
	@Test
	public void testState() {
		assertTrue(pc.getState() == PracticeController.State.REG_STOPPED);
	}
	
	@Test
	public void testCountCorrect() {
		assertTrue(countCorrect == charLimit);
	}

}
