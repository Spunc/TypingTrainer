package gui.keyboard;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class KeyboardImageTest {

	@Test
	public void testExistingKeyboardImage() {
		assertTrue(KeyboardImage.exists(Locale.GERMANY));
	}
	
	@Test
	public void testNonExistingKeyboardImage() {
		assertFalse(KeyboardImage.exists(Locale.CHINESE));
	}

}
