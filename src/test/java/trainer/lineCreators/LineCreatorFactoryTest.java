package trainer.lineCreators;

import static org.junit.Assert.*;

import org.junit.Test;

import trainer.PerformanceStats;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;
import trainer.lineCreators.InitException;


public class LineCreatorFactoryTest {
	
	@Test
	public void testGetAvailableLineCreatorTypes() {
		String[] names = LineCreatorFactory.getAvailableLineCreatorTypes();
		assertTrue(names.length > 0);
	}
	
	@Test
	public void findLocalLineCreator() throws ImplementationNotFound {
		LineCreatorProvider lcp = LineCreatorFactory.getLineCreatorProvider("GENERIC_RAND");
		assertNotNull(lcp);
	}
	
	/**
	 * Find the <i>TestLineCreatorPlugin</i>, which resides inside <i>testLineCreator.jar</i>.
	 * @throws ImplementationNotFound an implementation with the specified name could not be found.
	 */
	@Test
	public void findPluginLineCreatorProvider() throws ImplementationNotFound {
		LineCreatorProvider lcp = LineCreatorFactory.getLineCreatorProvider(
				TestPluginTest.NAME);
		assertNotNull(lcp);
	}
	
	/**
	 * Try to find a plugin with a name ("NotDefinedPlugin") that does not exist.
	 * @throws ImplementationNotFound an implementation with the specified name could not be found.
	 */
	@Test(expected = ImplementationNotFound.class)
	public void dontFindPlugin() throws ImplementationNotFound {
		LineCreatorFactory.getLineCreatorProvider("NotDefinedPlugin");
	}
	
	@Test
	public void initALocalLineCreator() throws ImplementationNotFound, InitException {
		String param = "abc";
		PerformanceStats ps = new PerformanceStats();
		LineCreator lc = LineCreatorFactory.getLineCreator("GENERIC_RAND", param, ps);
		assertNotNull(lc);
	}
	
	@Test
	public void initAPluginLineCreator() throws ImplementationNotFound, InitException {
		LineCreator lc = LineCreatorFactory.getLineCreator("TestLineCreatorPlugin",
				null, null);
		assertNotNull(lc);
	}

}
