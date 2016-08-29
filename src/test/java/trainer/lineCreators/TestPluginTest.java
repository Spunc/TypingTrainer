package trainer.lineCreators;

import static org.junit.Assert.*;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.Before;
import org.junit.Test;


/**
 * <p><tt>plugin.TestPlugin</tt> is a test implementation for <tt>LineCreatorProviderPlugin</tt>.
 * 
 * <p>This class tests that the <tt>LineCreatorProviderPlugin</tt> interface is implemented
 * correctly, so that <tt>plugin.TestPlugin</tt> can be used in other unit tests.
 * 
 * <p>The static fields of this class specify the behavior that can be expected from
 * <tt>plugin.TestPlugin</tt>.
 * 
 * @author Lasse Osterhagen
 *
 */
public class TestPluginTest {
	
	public static final String NAME = "TestLineCreatorPlugin";
	public static final String DESCRIPTION = "LineCreator for testing";
	public static final UnaryOperator<String> shortParamFunc = s -> s.substring(0, 3);
	public static final String LINECREATOR_CREATE = "Test line";
	public static final Function<Integer, String> createFunc = i ->
		LINECREATOR_CREATE.length() < i ? LINECREATOR_CREATE : LINECREATOR_CREATE.substring(0, i);
	
	private LineCreatorProviderPlugin lcp;
	
	@Before
	public void setUp() {
		lcp = new plugin.TestPlugin();
	}

	@Test
	public void testName() {
		assertEquals(lcp.getName(), NAME);
	}
	
	@Test
	public void testDescription() {
		assertEquals(lcp.description(), DESCRIPTION);
	}
	
	@Test
	public void testShortParam() {
		String testParam = "123456789";
		assertEquals(lcp.shortParam("1234567"), shortParamFunc.apply(testParam));
	}
	
	@Test
	public void testProvidedLineCreator() {
		LineCreator lc = lcp.getLineCreator(null, null);
		int strLen = LINECREATOR_CREATE.length();
		assertEquals(lc.create(strLen), createFunc.apply(strLen));
		assertEquals(lc.create(strLen-1), createFunc.apply(strLen-1));
	}


}
