package trainer.lineCreators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the TestLineCreator.
 * @author Lasse Osterhagen
 *
 */
public class TestLineCreatorTest {
	
	private TestLineCreator testLineCreator;
	private static final String LINE = "1234567890";
	private static final int LINE_LEN = LINE.length();
	private static final int NUM_REP = 2;
	
	@Before
	public void init() {
		testLineCreator = new TestLineCreator(LINE, NUM_REP);
	}

	@Test
	public void testLine() {
		String resultingLine = LINE + '\n';
		assertTrue(testLineCreator.create(LINE_LEN).equals(resultingLine));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateArgumentException() {
		testLineCreator.create(LINE_LEN-1);
		testLineCreator.create(LINE_LEN+1);
	}
	
	private void doAllowedCalls2Create() {
		for(int i=0; i<NUM_REP; ++i) {
			assertTrue(testLineCreator.hasNext());
			testLineCreator.create(LINE_LEN);
		}
	}
	
	@Test
	public void testState() {
		doAllowedCalls2Create();
		assertFalse(testLineCreator.hasNext());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCreateStateException() {
		doAllowedCalls2Create();
		testLineCreator.create(LINE_LEN);
	}

}
