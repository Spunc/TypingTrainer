package trainer.lineCreators;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import trainer.PerformanceStats;

public abstract class FileLineCreatorProvider implements LineCreatorProvider {
	
	protected abstract LineCreator createLineCreator(InputStream is) throws IOException;

	@Override
	public LineCreator getLineCreator(String param, PerformanceStats ps) throws InitException {
		Map<String, String> paramMap = StringCode.decode(param);
		boolean isLocal = Boolean.parseBoolean(paramMap.get("isLocal"));
		String fileName = paramMap.get("fileName");
		if(isLocal) {
			// File with words/texts resides inside jar at "exerciseTxt"
			try(InputStream is = this.getClass().getClassLoader().
					getResourceAsStream("exerciseTxt/" + fileName)) {
				if(is == null)
					throw new InitException(InitException.Type.MISSING_FILE, fileName);
				return createLineCreator(is);
			}
			catch (IOException e) {
				throw new InitException(InitException.Type.OTHER, e.getMessage());
			}
		}
		else {
			// File with words/texts resides inside subdirectory of user defined save directory
			try(FileInputStream fis = new FileInputStream(install.Constants.getTextsDir()
					.resolve(fileName).toFile())) {
				return createLineCreator(fis);
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
	public String shortParam(String param) {
		Map<String, String> paramMap = StringCode.decode(param);
		return paramMap.get("fileName");
	}

}
