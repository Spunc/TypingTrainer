package install;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Test;

import install.PathUtils.PathType;

public class PathUtils_checkPathTypeTest {
	
	private static String home = System.getProperty("user.home");
	private static final String rareDirName = "typetrainertestdir_q3y9z";
	private static final String rareFileName = "typetrainertestfile_q4y9z.tst";

	@Test
	public void testExistentDir() {
		Path p = Paths.get(home);
		Set<PathType> s = PathUtils.checkPathType(p);
		assertTrue(s.contains(PathType.ISEXISTENT));
		assertTrue(s.contains(PathType.ISDIRECTORY));
		assertFalse(s.contains(PathType.ISFILE));
	}
	
	@Test
	public void testExistentFile() {
		Path tempFile = Paths.get(home, rareFileName);
		try {
			Files.createFile(tempFile);
			Set<PathType> s = PathUtils.checkPathType(tempFile);
			assertTrue(s.contains(PathType.ISFILE));
			assertFalse(s.contains(PathType.ISDIRECTORY));
			Files.delete(tempFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEmptyDirectory() {
		Path tempDir = Paths.get(home, rareDirName);
		try {
			Files.createDirectory(tempDir);
			Set<PathType> s = PathUtils.checkPathType(tempDir);
			assertTrue(s.contains(PathType.ISEMPTY));
			Files.delete(tempDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNonEmptyDirectory() {
		Path p = Paths.get(home);
		Path tempFile = p.resolve(rareFileName);
		try {
			Files.createFile(tempFile);
			Set<PathType> s = PathUtils.checkPathType(tempFile);
			assertFalse(s.contains(PathType.ISEMPTY));
			Files.delete(tempFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreatableDir() {
		Path p = Paths.get(home, rareDirName, rareDirName, rareDirName);
		Set<PathType> s = PathUtils.checkPathType(p);
		assertTrue(s.contains(PathType.ISCREATABLE));
	}
	
	@Test
	public void testNonCreatableDir() {
		// May not work on Windows
		Path p = Paths.get(home).getRoot().resolve(rareDirName);
		Set<PathType> s = PathUtils.checkPathType(p);
		assertFalse(s.contains(PathType.ISCREATABLE));
	}

}
