package trainer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import persistence.SessionPersist;
import trainer.lineCreators.TestLineCreator;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;
import trainer.lineCreators.InitException;

/**
 * Tests interplay between <tt>PracticeController</tt> and <tt>LineCreator</tt> in case
 * that the <tt>LineCrator</tt> controls the termination of a practice unit, i. e.
 * LineCreator.hasNext() will return false at some point.
 * 
 * Test verifies correct state behavior.
 * 
 * @author Lasse Osterhagen
 *
 */
public class PracticeControllerLineCreatorLimitTest {
	
	private static final String LINE = "1234567890";
	private static final int LINE_LEN = LINE.length();
	private static final int NUM_REP = 3; // number of available lines in LineCreator
	private TestLineCreator testLineCreator;
	private Exercise exercise = DefaultObjectFactory.getExercise();
	private @Mock SessionPersist sessionPersist;
	private @InjectMocks TestPracticeController pc;

	@Before
	public void init() throws ImplementationNotFound, InitException {
		testLineCreator = new TestLineCreator(LINE, NUM_REP);
		exercise.setLimitType(Exercise.LimitType.NONE);
		pc = new TestPracticeController(exercise, LINE_LEN);
		MockitoAnnotations.initMocks(this);
		pc.setLineCreator(testLineCreator); //overwrite LineCreator with TestLineCreator
	}
	
	private void writeLine() {
		String line2Write = LINE + '\n';
		LineMonitor lm = pc.getLineMonitor();
		for(char c : line2Write.toCharArray())
			lm.advanceIfCorrect(c);
	}
	
	/**
	 * Test correct state behavior: write as many lines as are LineCreator provides.
	 */
	@Test
	public void testCorrectUsageState() {
		pc.ready();
		pc.run();
		for(int i=0; i<NUM_REP; ++i) {
			assertTrue(pc.getState() == PracticeController.State.RUNNING);
			writeLine();
		}
		assertTrue(pc.getState() == PracticeController.State.REG_STOPPED);
	}
	
	/**
	 * Test correct state behavior: write more than LineCreator provides.
	 */
	@Test(expected = IllegalStateException.class)
	public void testWrongUsageState() {
		pc.ready();
		pc.run();
		for(int i=0; i<NUM_REP; ++i) {
			writeLine();
		}
		LineMonitor lm = pc.getLineMonitor();
		lm.advanceIfCorrect('\n'); // write an additional char
	}
	
	/**
	 * Test that the second line is empty if line1 displays the last line.
	 */
	@Test
	public void testEmpty2ndLine() {
		pc.ready();
		pc.run();
		for(int i=0; i<NUM_REP-1; ++i) {
			writeLine();
		}
		assertTrue(pc.getLine2().equals(""));
	}

}
