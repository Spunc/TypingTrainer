package trainer.lineCreators;
import static org.junit.Assert.*;

import org.junit.Test;

public class FileLineCreatorProviderTest {

	@Test
	public void testExistentFile() throws InitException {
		String param = "isLocal=True;fileName=middleRow_de.txt";
		new WordListLineCreatorProvider().getLineCreator(param, null);
	}
	
	@Test
	public void testNonExistentFile() {
		String param = "isLocal=True;fileName=notExistent";
		try {
			new WordListLineCreatorProvider().getLineCreator(param, null);
		}
		catch (InitException e) {
			System.out.println(e);
			assertTrue(e.getType() == InitException.Type.MISSING_FILE);
		}
	}

}
