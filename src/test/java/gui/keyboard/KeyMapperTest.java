package gui.keyboard;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gui.keyboard.KeyMapper.NotInKeySetException;

public class KeyMapperTest {
	
	private static final Coordinates de_b = new Coordinates(355, 170, 409, 224);
	private static final Coordinates de_space = new Coordinates(223, 227, 553, 281);
	private KeyMapper km;
	
	@Before
	public void createKeyMapperDE() {
		km = new KeyMapper("de");
	}

	@Test
	public void testDESingleLetter() throws NotInKeySetException {
		Coordinates[] co = km.getCoordinatesFor('b');
		assertEquals(de_b, co[0]);
	}
	
	@Test
	public void testDEShiftLetter() throws NotInKeySetException {
		Coordinates[] co = km.getCoordinatesFor('B');
		assertEquals(2, co.length);
	}
	
	@Test
	public void testSpace() throws NotInKeySetException {
		Coordinates[] co = km.getCoordinatesFor(' ');
		assertEquals(de_space, co[0]);
	}
	
	@Test(expected=KeyMapper.NotInKeySetException.class)
	public void testDENotInKeySet() throws NotInKeySetException {
		km.getCoordinatesFor('æ');
	}
	

}
