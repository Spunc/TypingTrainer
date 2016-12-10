package gui.keyboard;

import static org.junit.Assert.*;

import org.junit.Test;

public class KeyboardTest {

	@Test
	public void testAvailableKeyboard() {
		// Must find at least one available keyboard
		assertNotNull(Keyboard.getAvailableLayouts());
	}
	
	@Test
	public void testDefaultKeyboardLayout() {
		// Use German as language: must return German Qwerty as default keyboard layout
		assertEquals(Keyboard.getDefaultKeyboardLayout("DE").get(), "DE_qw");
	}

}
