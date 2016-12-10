package trainer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import trainer.Exercise.LimitType;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;
import trainer.lineCreators.InitException;

/**
 * Test the interplay of a {@link  trainer.PracticeController PracticeController} and the
 * belonging {@link trainer.LineMonitor LineMonitor}.
 * 
 * @author Lasse Osterhagen
 *
 */

public class PracticeController_LineMonitorTest {
	
	private Exercise exercise;
	private PracticeController pc;
	private LineMonitor lm;
	
	@Before
	public void init() throws ImplementationNotFound, InitException {
		final int maxLineLength = 20;
		exercise = DefaultObjectFactory.getExercise();
		exercise.setLimitType(LimitType.NONE);
		pc = new PracticeController(exercise, maxLineLength);
		lm = pc.getLineMonitor();
		pc.ready();
		pc.run();
	}
	
	private void testLettersInLine() {
		// get line to be written
		char[] testChars = pc.getLine1().toCharArray();
		for(int i=0; i<testChars.length;) {
			// simulate a wrongly typed char
			assertFalse(lm.advanceIfCorrect((char) (testChars[i]+1)));
			// simulate a correctly typed char
			assertTrue(lm.advanceIfCorrect(testChars[i++]));
		}
	}
	
	/**
	 * Test three lines to be written.
	 */
	@Test
	public void testLines() {
		final int numLines = 3;
		for(int i=0; i<numLines; ++i) {
			testLettersInLine();
		}
	}
	
	

}
