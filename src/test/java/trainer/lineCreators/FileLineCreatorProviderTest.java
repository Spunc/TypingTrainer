package trainer.lineCreators;
import org.junit.Test;

public class FileLineCreatorProviderTest {

	@Test
	public void testExistentFile() throws InitException {
		String param = "isLocal=True;fileName=middleRow.txt";
		new FileLineCreatorProvider("wordList", is-> new WordListLineCreator(is))
			.getLineCreator(param, null);
	}
	
	@Test(expected = InitException.class)
	public void testNonExistentFile() throws InitException {
		String param = "isLocal=True;fileName=notExistent";
		new FileLineCreatorProvider("wordList", is-> new WordListLineCreator(is))
			.getLineCreator(param, null);
	}

}
