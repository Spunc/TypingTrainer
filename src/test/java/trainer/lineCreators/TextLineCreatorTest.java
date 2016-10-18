package trainer.lineCreators;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class TextLineCreatorTest {
	
	// counts:							0    5    0      5    0    5    0 2 = 32
	private static final String text1 = "Example text,\n some example text.";
	
	private static TextLineCreator createTextLineCreator(String content) throws IOException {
		InputStream is = new ByteArrayInputStream(content.getBytes(
				persistence.Constants.PROJECT_CHARSET));
		return new TextLineCreator(is);
	}

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

}
