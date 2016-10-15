package trainer.lineCreators;

import static org.junit.Assert.*;

import org.junit.Test;

public class WordListLineCreatorTest {
	
	private final String parameter = "isLocal=True;fileName=middleRow.txt";
	private final int maxLength = 30;

	private String createLine() {
		try {
			LineCreator lc = new WordListLineCreatorProvider().getLineCreator(parameter, null);
			return lc.create(maxLength);
		}
		catch(Exception e) {throw new RuntimeException(e);}
	}

	@Test
	public void testNotEmpty() {
		assertTrue(createLine().length() > 0);
	}
	
	@Test
	public void testMaxSize() {
		assertTrue(createLine().length() <= maxLength + 1);
	}
	
	@Test
	public void testNewLine() {
		String line = createLine();
		assertEquals('\n', line.charAt(line.length()-1));
	}
	
	@Test(expected = InitException.class)
	public void testNonExistentFile() throws InitException {
		String param = "isLocal=True;fileName=notExistent";
		new WordListLineCreatorProvider().getLineCreator(param, null);
	}

}
