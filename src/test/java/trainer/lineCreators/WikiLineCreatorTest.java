package trainer.lineCreators;

import static org.junit.Assert.*;

import org.junit.Test;

public class WikiLineCreatorTest {

	@Test
	public void testEnglishWiki() throws InitException {
		// Uses keyboard specification for Germany
		WikiLineCreator wlc = new WikiLineCreator("DE", "en", 100);
		assertTrue(wlc.hasNext());
		assertFalse(wlc.create(40).isEmpty());
	}
	
	@Test(expected=InitException.class)
	public void testWrongWikiLanguagePrefix() throws InitException {
		@SuppressWarnings("unused")
		// Uses keyboard specification for Germany
		WikiLineCreator wlc = new WikiLineCreator("DE", "qq", 100);
	}
	

}
