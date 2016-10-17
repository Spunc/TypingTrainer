package trainer.lineCreators;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;

import trainer.PerformanceStats;

public class FileLineCreatorProvider implements LineCreatorProvider {
	
	private String description;
	private LineCreatorSupplier lcs;
	
	@FunctionalInterface
	public interface LineCreatorSupplier {
		LineCreator create(InputStream is) throws IOException;
	}
	
	public FileLineCreatorProvider(String descriptionKey, LineCreatorSupplier lcs) {
		description = ResourceBundle.getBundle("txtBundles.lineCreatorText")
				.getString(descriptionKey);
		this.lcs = lcs;
	}

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
				return lcs.create(is);
			}
			catch (IOException e) {
				throw new InitException(InitException.Type.OTHER, e.getMessage());
			}
		}
		else {
			// File with words/texts resides inside subdirectory of user defined save directory
			try(FileInputStream fis = new FileInputStream(install.Constants.getTextsDir()
					.resolve(fileName).toFile())) {
				return lcs.create(fis);
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
		String fileName = paramMap.get("fileName");
		if(Boolean.parseBoolean(paramMap.get("isLocal")))
			return ResourceBundle.getBundle("txtBundles.exerciseNameText").getString(fileName);
		else
			return fileName;
	}

	@Override
	public String description() {
		return description;
	}

}
