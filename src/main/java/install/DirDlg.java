package install;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFileChooser;

/**
 * This class wraps a {@link javax.swing.JFileChooser} for directories to provide additional
 * information about the directory chosen.
 * 
 * @author Lasse Osterhagen
 *
 */

public class DirDlg {
	
	public enum Results {
		/*
		 * directory does not exists, but parent directory does
		 */
		NONEXISTENT_PARENT_EXISTENT,
		/*
		 * directory does not exists and parent directory does not exist, too
		 */
		NONEXISTENT_PARENT_NONEXISTENT,
		/*
		 * directory already exists but is empty
		 */
		EXISTENT_EMPTY,
		/*
		 * directory already exists bus is not empty
		 */
		EXISTENT_NONEMPTY,
		/*
		 * user abort
		 */
		CANCEL,
		/*
		 * the specified path points to an existent file and not to a directory
		 */
		ISFILE,
		/*
		 * an error occured
		 */
		ERROR
	}
	
	public static Results showDirChooser(Component parent, File currentDirectory) {
		JFileChooser fc = new JFileChooser(currentDirectory);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int val = fc.showOpenDialog(parent);
		
		switch(val) {
		
		case JFileChooser.CANCEL_OPTION:
			return Results.CANCEL;
			
		case JFileChooser.ERROR_OPTION:
			return Results.ERROR;
			
		case JFileChooser.APPROVE_OPTION:
			Path selection = fc.getSelectedFile().toPath();
			
			if(Files.exists(selection)) {
				if(Files.isRegularFile(selection))
					return Results.ISFILE;
				try {
					if(Files.list(selection).count() == 0)
						return Results.EXISTENT_EMPTY;
					else
						return Results.EXISTENT_NONEMPTY;
				} catch (IOException e) {
					e.printStackTrace();
					return Results.ERROR;
				}
			}
			else {
				Path parentDir = selection.getParent();
				if(Files.exists(parentDir)) {
					return Results.NONEXISTENT_PARENT_EXISTENT;
				}
				else
					return Results.NONEXISTENT_PARENT_NONEXISTENT;
			}
		default:
			throw new RuntimeException("Undefined state.");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(DirDlg.showDirChooser(null, null));
	}

}
