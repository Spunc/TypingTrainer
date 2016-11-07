package trainer.lineCreators;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeableConverterTest {
	
	static TypeableConverter germanConverter = new TypeableConverter("DE");
	
	@Test
	public void testGermanUmlaute() {
		assertEquals(germanConverter.convert("äöüÄÖÜ"), "äöüÄÖÜ");
	}
	
	@Test
	public void testGermanSpecials() {
		assertEquals(germanConverter.convert("ß"), "ß");
	}
	
	@Test
	public void testGermanStrippedAccents() {
		// in no case comprehensive
		assertEquals(germanConverter.convert("ąēȫ"), "aeö");
	}
	
	@Test
	public void testGermanSubstitution() {
		// in no case comprehensive
		assertTrue(germanConverter.convert("†").length() == 0);
	}

}
