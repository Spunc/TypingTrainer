package install;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

public class Constants {
	
	/**
	 * Name of directory where words exercises are saved
	 */
	static final String WORDS_DIR = "words";
	/**
	 * Name of directory where text exercises are saved
	 */
	static final String TEXTS_DIR = "texts";
	/**
	 * Name of directory where plugins reside
	 */
	static final String PLUGINS_DIR = "plugins";
	/**
	 * Key of {@link java.util.prefs.Preferences} to full path to the user-defined data
	 * directory
	 */
	static final String PREF_USERSAVE_DIR = "userSaveDir";
	/**
	 * This is the (hopefully) unique id that identifies the application properties
	 */
	static final String APP_PREF_NODE = "/typeWriter108639";
	
	private static Preferences prefs = Preferences.userRoot().node(APP_PREF_NODE);
	
	/**
	 * Get path to the directory where words exercises are saved.
	 * @return path to words directory
	 */
	public static Path getWordsDir() {
		return Paths.get(prefs.get(Constants.PREF_USERSAVE_DIR, null)).resolve(WORDS_DIR);
	}
	
	/**
	 * Get path to the directory where text exercises are saved.
	 * @return path to texts directory
	 */
	public static Path getTextsDir() {
		return Paths.get(prefs.get(Constants.PREF_USERSAVE_DIR, null)).resolve(TEXTS_DIR);
	}
	
	/**
	 * Get path to the directory where plugins reside
	 * @return path to plugins directory
	 */
	public static Path getPluginsDir() {
		return Paths.get(prefs.get(Constants.PREF_USERSAVE_DIR, null)).resolve(PLUGINS_DIR);
	}
	
	/**
	 * Get path to the user-defined data directory
	 * @return path to user-defined data directory
	 */
	public static Path getUserSaveDir() {
		return Paths.get(prefs.get(Constants.PREF_USERSAVE_DIR, null));
	}

}
