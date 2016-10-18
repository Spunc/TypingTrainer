package trainer.lineCreators;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class WordListLineCreatorTest {
	
	private static final int maxLength = 30;
	private static final String word1 = "word1";
	private static final String word2 = "word2";
	private String line;
	
	@Before
	public void createLine() throws IOException {
		String content = word1 + '\n' + word2 + '\n';
		InputStream is = new ByteArrayInputStream(content.getBytes(
				persistence.Constants.PROJECT_CHARSET));
		WordListLineCreator wllc = new WordListLineCreator(is);
		line = wllc.create(maxLength);
		wllc.stop();
	}

	@Test
	public void testNotEmpty() {
		assertTrue(line.length() > 0);
	}
	
	@Test
	public void testMaxSize() {
		assertTrue(line.length() <= maxLength + 1);
	}
	
	@Test
	public void testNewLine() {
		assertEquals('\n', line.charAt(line.length()-1));
	}
	
	@Test
	public void testContent() {
		String[] words = line.split(" ");
		String lastWord = words[words.length-1];
		lastWord = lastWord.substring(0, lastWord.length()-1);
		words[words.length-1] = lastWord; // remove newline at end
		assertTrue( Arrays.stream(words).allMatch(s-> s.equals(word1) || s.equals(word2)) );
	}

}
