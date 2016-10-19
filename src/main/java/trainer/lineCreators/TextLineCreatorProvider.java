package trainer.lineCreators;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;


public class TextLineCreatorProvider extends FileLineCreatorProvider  {
	
	@Override
	public String description() {
		return ResourceBundle.getBundle("txtBundles.lineCreatorText")
				.getString("text");
	}
	
	@Override
	protected LineCreator createLineCreator(InputStream is) throws IOException {
		return new TextLineCreator(is);
	}

}
