package trainer.lineCreators;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class TypeableConverterTest {
	
	static TypeableConverter germanyConverter = new TypeableConverter("DE");
	
	// ---- Germany test ----
	@Test
	public void testGermanyUmlaute() {
		assertEquals(germanyConverter.convert("äöüÄÖÜ"), "äöüÄÖÜ");
	}
	
	@Test
	public void testGermanySpecials() {
		assertEquals(germanyConverter.convert("ß§"), "ß§");
	}
	
	@Test
	public void testGermanyStrippedAccents() {
		// in no case comprehensive
		assertEquals(germanyConverter.convert("ąēȫ"), "aeö");
	}
	
	@Test
	public void testGermanyReplacedChars() {
		// in no case comprehensive
		assertEquals(germanyConverter.convert("ø†"), "??"); // the letter 'ø' cannot be converted to 'o'
	}
	
	@Ignore	// Implement later: Maybe use a map to make conversion like '\u2044' (fractional slash)
			// to '/'. Idea: put the mappings in an additional txt file
	@Test
	public void testGermanyFractions() {
		assertEquals(germanyConverter.convert("½"), "1/2");
	}
	
	// ---- Undefined country test ----
	@Test
	public void testUndefinedCountry() {
		TypeableConverter t = new TypeableConverter("XX");
		assertEquals(t.convert("aä"), "aa");
	}

}
