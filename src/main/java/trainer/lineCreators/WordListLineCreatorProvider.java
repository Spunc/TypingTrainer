package trainer.lineCreators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.ResourceBundle;

import trainer.PerformanceStats;


public class WordListLineCreatorProvider implements LineCreatorProvider {

	@Override
	public LineCreator getLineCreator(String param, PerformanceStats ps) throws InitException {
		Map<String, String> paramMap = StringCode.decode(param);
		boolean isLocal = Boolean.parseBoolean(paramMap.get("isLocal"));
		String fileName = paramMap.get("fileName");
		if(isLocal) {
			// File with words resides inside jar at "exerciseTxt"
			try(InputStream is = this.getClass().getClassLoader().
					getResourceAsStream("exerciseTxt/" + fileName)) {
				if(is == null)
					throw new InitException(InitException.Type.MISSING_FILE, fileName);
				try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
					return new WordListLineCreator(reader);
				}
			}
			catch (IOException e) {
				throw new InitException(InitException.Type.OTHER, e.getMessage());
			}
		}
		else {
			// File with words resides inside subdirectory of user defined save directory
			try(BufferedReader reader = new BufferedReader(new FileReader(
					install.Constants.getTextsDir().resolve(fileName).toFile()))) {
				return new WordListLineCreator(reader);
			}
			catch (FileNotFoundException e) {
				throw new InitException(InitException.Type.MISSING_FILE, fileName);
			}
			catch (IOException e) {
				throw new InitException(InitException.Type.OTHER, e.getMessage());
			}
		}
	}

	@Override
	public String description() {
		return ResourceBundle.getBundle("txtBundles.lineCreatorText")
				.getString("wordList");
	}
	
	@Override
	public String shortParam(String param) {
		Map<String, String> paramMap = StringCode.decode(param);
		String fileName = paramMap.get("fileName");
		if(Boolean.parseBoolean(paramMap.get("isLocal")))
			return ResourceBundle.getBundle("txtBundles.exerciseNameText").getString(fileName);
		else
			return fileName;
	}

}
