package gui.keyboard;

import static org.junit.Assert.*;

import java.awt.TextField;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

import gui.keyboard.KeyMapper.NotInKeySetException;

/**
 * Test <code>KeyMapper</code> with resource files for <code>Locale.GERMANY</code>
 * 
 * @author Lasse Osterhagen
 *
 */

public class KeyMapper_deDE_Test {
	
	private static final Coordinates co_b = new Coordinates(355, 170, 409, 224);
	private static final Coordinates co_space = new Coordinates(223, 227, 553, 281);
	private static final Coordinates co_lShift = new Coordinates(0, 170, 70, 224);
	private KeyMapper km;
	
	@Before
	public void createKeyMapperDE() throws LayoutNotFoundException {
		km = new KeyMapper("DE_qw");
	}
	
	/*
	 * 	Tests correct coordinates for a single letter
	 */
	@Test
	public void testSingleLetter() throws NotInKeySetException {
		Coordinates[] co = km.getCoordinatesFor('b');
		assertEquals(co_b, co[0]);
	}
	
	/*
	 *  Tests that an uppercase letter results in two Coordinates to be returned (one
	 *  for the letter and one for the shift key).
	 */
	@Test
	public void testShiftLetter() throws NotInKeySetException {
		Coordinates[] co = km.getCoordinatesFor('B');
		assertEquals(2, co.length);
	}
	
	@Test
	public void testEnterChar() throws NotInKeySetException {
		Coordinates[] co = km.getCoordinatesFor('\n');
		assertEquals(2, co.length);
	}
	
	/*
	 * Tests the use of KeyEvent as parameter. This is especially useful for the coloring of a
	 * modifier key like the shift key.
	 */
	@Test
	public void testLeftShift() throws NotInKeySetException {
		KeyEvent k = new KeyEvent(new TextField(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
				KeyEvent.VK_SHIFT, KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED,
				KeyEvent.KEY_LOCATION_LEFT);
		Coordinates[] co = km.getCoordinatesFor(k);
		assertEquals(co_lShift, co[0]);
	}
	
	/*
	 * Tests a non-letter key
	 */
	@Test
	public void testSpace() throws NotInKeySetException {
		Coordinates[] co = km.getCoordinatesFor(' ');
		assertEquals(co_space, co[0]);
	}
	
	/*
	 * Tests a unicode character that is not represented on the keyboard
	 */
	@Test(expected=KeyMapper.NotInKeySetException.class)
	public void testDENotInKeySet() throws NotInKeySetException {
		km.getCoordinatesFor('æ');
	}
	

}
