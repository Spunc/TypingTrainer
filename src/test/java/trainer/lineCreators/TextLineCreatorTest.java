package trainer.lineCreators;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class TextLineCreatorTest {
	
	private static TextLineCreator createTextLineCreator(String content) throws IOException {
		InputStream is = new ByteArrayInputStream(content.getBytes(
				persistence.Constants.PROJECT_CHARSET));
		return new TextLineCreator(is);
	}
	
	// counts:							0    5    0      5    0    5    0 2
	private static final String text1 = "Example text,\n some example text.";
	@Test
	public void testText1() throws IOException {
		TextLineCreator tlc = createTextLineCreator(text1);
		String line = tlc.create(20);
		assertEquals("1st line differs.", "Example text, some\n", line);
		line = tlc.create(20);
		assertEquals("2nd line differs.", "example text.\n", line);
		assertFalse(tlc.hasNext());
		tlc.stop();
	}
	
	// counts:							0    5    0    5    0    5    0 2
	private static final String text2 = "A_very_long_line_without spaces.";
	@Test
	public void testText2() throws IOException {
		TextLineCreator tlc = createTextLineCreator(text2);
		String line = tlc.create(10);
		assertEquals("1st line differs.", "A_very_lo-\n", line);
		line = tlc.create(10);
		assertEquals("2nd line differs.", "ng_line_w-\n", line);
		line = tlc.create(10);
		assertEquals("3rd line differs.", "ithout\n", line);
		line = tlc.create(10);
		assertEquals("4th line differs.", "spaces.\n", line);
		tlc.stop();
	}
	
	// counts:							0    5    0    5    0    5
	private static final String text3 = "A_long_li-ne with hyphens";
	@Test
	public void testText3() throws IOException {
		TextLineCreator tlc = createTextLineCreator(text3);
		String line = tlc.create(10);
		assertEquals("1st line differs.", "A_long_li-\n", line);
		line = tlc.create(10);
		assertEquals("2nd line differs.", "ne with\n", line);
		line = tlc.create(10);
		assertEquals("3rd line differs.", "hyphens\n", line);
	}

}
