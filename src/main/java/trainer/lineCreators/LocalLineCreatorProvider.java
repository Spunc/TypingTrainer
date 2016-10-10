package trainer.lineCreators;

import java.util.ResourceBundle;

/**
 * <p><tt>LineCreator</tt>s that are not provided by the plug-in mechanism
 * ({@link LineCreatorProviderPlugin}), but reside in classes that belong to
 * the type trainer, should extend this class.
 * 
 * <p>Classes that extend this class must have an entry in
 * <i>resources.lineCreatorText.properties</i> to provide an internationalized description.
 * 
 * @author Lasse Osterhagen
 *
 */

public abstract class LocalLineCreatorProvider implements LineCreatorProvider {
	
	private String descriptionKey;
	private ResourceBundle textBundle = ResourceBundle.getBundle("txtBundles.lineCreatorText");
	
	public LocalLineCreatorProvider(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}

	@Override
	public String description() {
		return textBundle.getString(descriptionKey);
	}

}
