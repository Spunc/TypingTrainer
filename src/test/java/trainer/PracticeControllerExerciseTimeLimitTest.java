package trainer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import persistence.SessionPersist;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;

import static org.hamcrest.number.IsCloseTo.*;


public class PracticeControllerExerciseTimeLimitTest {
	
	static final int timeLimit = 1; //1 sec
	static final int maxLineLength = 20;
	
	private Exercise exercise;
	private @Mock SessionPersist sessionPersist;
	private @InjectMocks PracticeController pc;
	private LineMonitor lm;
	
	private void init() throws ImplementationNotFound {
		exercise = DefaultObjectFactory.getExercise();
		exercise.setLimitType(Exercise.LimitType.TIME);
		exercise.setLimitUnits(timeLimit);
		pc = new PracticeController(exercise, maxLineLength);
		MockitoAnnotations.initMocks(this);
		lm = pc.getLineMonitor();
	}
	
	private void writeLine() {
		char[] lineChars = pc.getLine1().toCharArray();
		for(char c : lineChars) {
			lm.advanceIfCorrect(c);
		}
	}
	
	@Before
	public void initAndRun() throws InterruptedException, ImplementationNotFound {
		init();
		pc.ready();
		pc.run();
		long startTime = System.currentTimeMillis();
		// Run the exercise as long as pc has not stopped or time exceeds timeLimit + 1 sec
		while(!(pc.getState() == PracticeController.State.REG_STOPPED) || 
				System.currentTimeMillis()-startTime > (timeLimit+1)*1000) {
			writeLine();
			Thread.sleep(timeLimit*100); // sleep 1/10 of timeLimit
		}
	}
	
	@Test
	public void testState() {
		assertTrue(pc.getState() == PracticeController.State.REG_STOPPED);
	}
	
	@Test
	public void testTiming() {
		assertThat((double) pc.getRequiredTime(), closeTo(timeLimit*1000, 20));
	}
	
	

}
