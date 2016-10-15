package trainer.lineCreators;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringCodeTest {
	
	private static final String expectedEncoding = "a=1;b=2;";
	
	private static Map<String, String> getTestMap() {
		HashMap<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		return map;
	}

	@Test
	public void testEncode() {
		Map<String, String> map = getTestMap();
		String encoded = StringCode.encode(map);
		assertEquals(expectedEncoding, encoded);
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testEncodeException() {
		Map<String, String> m = getTestMap();
		m.put("a;b", "1");
	    thrown.expect(RuntimeException.class);
	    thrown.expectMessage("Delimiter in key string.");
	    StringCode.encode(m);
	}
	
	@Test
	public void testDecode() {
		Map<String, String> map = StringCode.decode(expectedEncoding);
		assertEquals(getTestMap(), map);
	}

}
