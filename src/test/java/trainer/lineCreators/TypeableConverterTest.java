package trainer.lineCreators;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeableConverterTest {
	
	static TypeableConverter germanyConverter = new TypeableConverter("DE");
	
	@Test
	public void testGermanUmlaute() {
		assertEquals(germanyConverter.convert("äöüÄÖÜ"), "äöüÄÖÜ");
	}
	
	@Test
	public void testGermanSpecials() {
		assertEquals(germanyConverter.convert("ß§"), "ß§");
	}
	
	@Test
	public void testGermanStrippedAccents() {
		// in no case comprehensive
		assertEquals(germanyConverter.convert("ąēȫ"), "aeö");
	}
	
	@Test
	public void testReplacedChars() {
		// in no case comprehensive
		assertEquals(germanyConverter.convert("ø†"), "??"); // the letter 'ø' cannot be converted to 'o'
	}

}
