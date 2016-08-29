package install;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import install.PathUtils.PathType;

import static gui.Util.getGUIText;

public class StartEnd {
	
	/**
	 * This is the (hopefully) unique id that identifies the application properties
	 */
	private static final String appPrefNode = "/typeWriter108639";
	private static final String pref_userSaveDir = "userSaveDir";
	private static final String dbName = "typeWriter.db";
	
	/**
	 * Entry method of the applications. Handles administrative tasks:
	 * <ul>
	 * 	<li>Install</li>
	 * 	<li>Uninstall</li>
	 * 	<li>Regular start</li>
	 * </ul>
	 * <p>Allowed command line arguments:
	 * <ul>
	 * 	<li>Uninstall - uninstall the application</li>
	 * 	<li>Force - in combination with uninstall; uninstalls without prompting for confirmation</li>
	 * </ul>
	 * <p>Tasks are executed in the following steps:
	 * <ol>
	 * <li>Check if <i>uninstall</i> argument is set;
	 * 	uninstall if true and terminate the application.</li>
	 * <li>Check if the application needs to be installed; install if true.</li>
	 * <li>Start the application by opening the {@link gui.MainWindow}.
	 * @param command line arguments
	 * @throws BackingStoreException if access to Java preferences failed
	 */
	static void start(String[] args) throws BackingStoreException {
		Options options = new Options();
		options.addOption("uninstall", "delete user data and registry entries");
		options.addOption("force", "do not ask for confirmations");
		try {
			CommandLine cmd = new DefaultParser().parse(options, args);
			// ------------Uninstall option------------
			if(cmd.hasOption("uninstall")) {
				uninstall(cmd);
				return;
			}
			// ------------Usual start-----------------
			if(!Preferences.userRoot().nodeExists(appPrefNode)) {
				// Install
				if(!install()) {
					JOptionPane.showMessageDialog(null, getGUIText("aborted"), getGUIText("setup"),
							JOptionPane. INFORMATION_MESSAGE);
					return;
				}
			}
			// Open database connection
			Preferences pref = Preferences.userRoot().node(appPrefNode);
			Path dbPath = Paths.get(pref.get(pref_userSaveDir, null)).resolve(dbName);
			persistence.DbAccess.getInstance().connect(dbPath.toString());
			SwingUtilities.invokeLater(() -> showMainWindow());
		}
		catch (ParseException e) {
			System.err.println(e);
			return;
		}
	}
	
	private static void showMainWindow() {
		gui.MainWindow mw = new gui.MainWindow();
		mw.setVisible(true);
	}
	
	/**
	 * Install the application:
	 * <ul>
	 * <li>let user choose a directory for user data
	 * <li>save the location of that directory to {@link java.util.prefs.Preferences}
	 * <li>create database
	 * </ul>
	 * @param cmd command line arguments
	 * @return true if succeed
	 */
	private static boolean install() {
		//Ask for application directory
		int choice = JOptionPane.showConfirmDialog(null, getGUIText("chooseDir"), getGUIText("setup"),
				JOptionPane.OK_CANCEL_OPTION);
		if(choice == JOptionPane.CANCEL_OPTION) {
			return false;
		}
		Optional<Path> chosenDir = getValidPathByGUI();
		if(!chosenDir.isPresent()) {
			return false;
		}
		try {
			Files.createDirectories(chosenDir.get());
			// Add location of application directory to Preferences
			Preferences pref = Preferences.userRoot().node(appPrefNode);
			pref.put(pref_userSaveDir, chosenDir.get().toString());
			pref.flush();
		} catch (IOException | BackingStoreException e) {
			e.printStackTrace();
			return false;
		}
		// Fill tables
		persistence.CreateInitialTables.createTables(chosenDir.get().resolve(dbName).toString());
		return true;
	}
	
	/**
	 * Uninstalls the application. First, it deletes the user data directory of the application.
	 * Then, it deletes the registry entries of the application ({@link java.util.prefs.Preferences})
	 * @param cmd command line arguments
	 * @throws BackingStoreException if access to Java preferences failed
	 */
	private static void uninstall(CommandLine cmd) throws BackingStoreException {
		// Application is already uninstalled 
		if(!Preferences.userRoot().nodeExists(appPrefNode)) {
			String message = getGUIText("alreadyUninstalled");
			JOptionPane.showMessageDialog(null, message);
			return;
		}
		// Delete user data directory of the application
		Preferences appPrefs = Preferences.userRoot().node(appPrefNode);
		String appDirStr = appPrefs.get(pref_userSaveDir, null);
		if(appDirStr == null)
			throw new RuntimeException("Preferences corrupted.");
		Path appDir = Paths.get(appDirStr);
		if(!Files.exists(appDir) || !Files.isDirectory(appDir))
			throw new RuntimeException("File system corrupted.");
		if(!cmd.hasOption("force")) {
			// Show confirmation dialog to user
			String firstMsgRow = MessageFormat.format(getGUIText("uninstallMsg1"),
					appDir.toString());
			int choice = JOptionPane.showConfirmDialog(
				    null,
				    firstMsgRow + '\n' + getGUIText("uninstallMsg2"),
				    getGUIText("uninstall"),
				    JOptionPane.YES_NO_OPTION, JOptionPane. WARNING_MESSAGE);
			if(choice == JOptionPane.NO_OPTION)
				return;
		}
		try {
			PathUtils.deleteDirectoryTree(appDir);
		} catch (IOException e) {
			System.err.println("Could not delete " + appDir);
		}
		// Delete preferences
		appPrefs.removeNode();
	}
	
	
	/**
	 * Opens a {@link javax.swing.JFileChooser} and asks a {@link java.nio.file.Path} from the user.
	 * @return the chosen Path or Optional.empty, if the user canceled.
	 */
	private static Optional<Path> getPathByGUI() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fc.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION)
			return Optional.of(fc.getSelectedFile().toPath());
		else
			return Optional.empty();
	}
	

	/**
	 * Opens a {@link javax.swing.JFileChooser} and asks a {@link java.nio.file.Path} from the user.
	 * Checks the validity of the <code>Path</code>. A Path is valid, if
	 * <ul>
	 * <li>it is an existent empty directory or</li>
	 * <li>it is a non-existent but creatable (writable) directory</li>
	 * </ul>
	 * 
	 * <p>Displays a {@link javax.swing.JOptionPane} with an error message, if the chosen Path was
	 * invalid. Repeats displaying <code>JFileChooser</code> until a valid <code>Path</code> was
	 * selected or the user canceled.
	 * @return the chosen Path or Optional.empty, if the user canceled.
	 */
	private static Optional<Path> getValidPathByGUI() {
		while(true) {
			Optional<Path> selectedPath = getPathByGUI();
			if(!selectedPath.isPresent())
				// user canceled
				return Optional.empty();
			else {
				// check validity
				Path p = selectedPath.get();
				Set<PathType> pt = PathUtils.checkPathType(p);
				// valid cases
				if(pt.contains(PathType.ISEXISTENT) && pt.contains(PathType.ISEMPTY) &&
						pt.contains(PathType.ISWRITABLE) ||
						pt.contains(PathType.ISCREATABLE))
					return Optional.of(p);
				// invalid cases
				String errorMsg;
				if(!pt.contains(PathType.ISWRITABLE))
					errorMsg = getGUIText("nonwritable");
				else if(pt.contains(PathType.ISDIRECTORY))
					errorMsg = getGUIText("nonemptyDir");
				else
					errorMsg = getGUIText("invalidDir");
				int result = JOptionPane.showConfirmDialog(null, errorMsg +
						"\n" + getGUIText("tryAgainChooseDir"), getGUIText("setup"),
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
				if(result == JOptionPane.CANCEL_OPTION)
					return Optional.empty();
			}
		}
	}
	
	
	/**
	 * Application main() entry. Just calls {@link #start(String[])}.
	 * @param args command line arguments
	 * @throws BackingStoreException if access to Java preferences failed
	 */
	public static void main(String[] args) throws BackingStoreException {
		start(args);
	}
	
}
