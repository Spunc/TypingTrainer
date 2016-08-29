package install;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Set;

public class PathUtils {
	
	/**
	 * <p>Delete an entire directory tree.
	 * 
	 * <p>This function is a copy of Oracle's example of a
	 * <a href="http://docs.oracle.com/javase/8/docs/api/java/nio/file/FileVisitor.html">FileVisitor</a>
	 * @param directory the target directory to be deleted
	 * @throws IOException if access to file failed
	 */
	public static void deleteDirectoryTree(Path directory) throws IOException {
		
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
				if(e == null) {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
				else {
					throw e;
				}
			}
			
		});
	}
	
	/**
	 * Classification of paths for directories
	 */
	public enum PathType {
		/**
		 * path (file or directory) exists
		 */
		ISEXISTENT,
		/**
		 * path is a file
		 */
		ISFILE,
		/**
		 * path is a directory
		 */
		ISDIRECTORY,
		/**
		 * path (file or directory) is writable
		 */
		ISWRITABLE,
		/**
		 * directory is empty
		 */
		ISEMPTY,
		/**
		 * the file or directory can be created, i.e. all needed parent directories can be created
		 */
		ISCREATABLE
	}
	
	public static Set<PathType> checkPathType(Path p) {
		p = p.normalize();
		Set<PathType> resultSet = EnumSet.noneOf(PathType.class);
		
		// Existent path
		if(Files.exists(p)) {
			resultSet.add(PathType.ISEXISTENT);
			// Writable?
			if(Files.isWritable(p))
				resultSet.add(PathType.ISWRITABLE);
			// Directory
			if(Files.isDirectory(p)) {
				resultSet.add(PathType.ISDIRECTORY);
				try {
					if(Files.list(p).count() == 0)
						resultSet.add(PathType.ISEMPTY);
				} catch (AccessDeniedException e) {
					resultSet.remove(PathType.ISWRITABLE);
				} catch (IOException e) {
					/*
					 * TODO: This perhaps needs to be changed: catch all exceptions and
					 * create a new PathType "ERROR"
					 */
					throw new RuntimeException(e);
				}
			}
			// File
			else
				resultSet.add(PathType.ISFILE);
		}
		// Nonexistent path
		else {
			// Check if path can be created
			do {
				p = p.getParent();
			} while (!Files.exists(p));
			if(Files.isWritable(p))
				resultSet.add(PathType.ISCREATABLE);
		}
		
		return resultSet;
	}
	
}
