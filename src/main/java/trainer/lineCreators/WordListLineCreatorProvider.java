package trainer.lineCreators;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;


public class WordListLineCreatorProvider extends FileLineCreatorProvider {
	
	@Override
	public String description() {
		return ResourceBundle.getBundle("txtBundles.lineCreatorText")
				.getString("wordList");
	}

	@Override
	protected LineCreator createLineCreator(InputStream is) throws IOException {
		return new WordListLineCreator(is);
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
