package trainer.lineCreators;

import static org.junit.Assert.*;

import org.junit.Test;

import trainer.PerformanceStats;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;
import trainer.lineCreators.InitException;

public class GenericWordLineCreatorTest {

	@Test
	public void testRandWordCreator() throws ImplementationNotFound, InitException {
		testLineCreator(LineCreatorFactory.getLineCreator("GENERIC_RAND", "abcde", null));
	}
	
	@Test
	public void testRandLangWordCreator() throws ImplementationNotFound, InitException {
		testLineCreator(LineCreatorFactory.getLineCreator("GENERIC_RAND_LANG",
				"abcABC.", null));
	}
	
	@Test
	public void testAdaptRandWordCreator() throws ImplementationNotFound, InitException {
		testLineCreator(LineCreatorFactory.getLineCreator("ADAPT_RAND", "abcde",
				new PerformanceStats()));
	}
	
	public void testLineCreator(LineCreator l) {
		String line = l.create(30);
		assertTrue(line.length() <= 30+1);
		assertTrue(line.charAt(line.length()-1) == ('\n'));
		assertFalse(line.charAt(line.length()-2) == (' '));
	}

}
