package gui.keyboard;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Maps unicode characters or {@link java.awt.event.KeyEvent} to pixel
 * {@link gui.keyboard.Coordinates} of keys on locale-specific keyboard layout images.
 * 
 * <p>This class needs a set of text files that are located in this package
 * (<i>gui.keyboard</i>) for every layout. The file names always end with the code that belongs to
 * the layout (e. g. <i>DE_qw</i> for a German Qwerty layout), before the file extension (e. g.
 * <i>.csv</i>) is appended. All files must be in UTF-8 format.
 * 
 * <p>At first, there are files that map unicode characters to pixel coordinates of the
 * corresponding keys. A line of such a file starts with the character followed by the coordinates
 * left (x), top (y), right (x), and bottom, all separated by white spaces. The file extension is
 * <i>.csv</i>:
 * <table summary="Key coordinates files">
 * 	<tr>
 * 		<th>File name</th>
 * 		<th>Description</th>
 * 	</tr>
 * 	<tr>
 * 		<td>co_letters_</td>
 * 		<td>letter characters</td>
 * 	</tr>
 * 	<tr>
 * 		<td>co_noshift_</td>
 * 		<td>non-letter characters that do not need a modifier key</td>
 * 	</tr>
 * 	<tr>
 * 		<td>co_lshift_</td>
 * 		<td>non-letter characters that need the left shift to be pressed</td>
 * 	</tr>
 * 	<tr>
 * 		<td>co_rshift_</td>
 * 		<td>non-letter characters that need the right shift to be pressed</td>
 * 	</tr>
 *  <tr>
 * 		<td>co_altgr_</td>
 * 		<td>non-letter characters that need the AltGr to be pressed</td>
 * 	</tr>
 * </table>
 * 
 * <p>Second, there is the file <i>co_specials_XX_xx.csv</i> (replace <i>xx</i> with the layout id)
 * with the coordinates of the keys:
 * <ol>
 * 	<li>enter1</li>
 * 	<li>(optional) enter2</li>
 * 	<li>space</li>
 * 	<li>lshift</li>
 * 	<li>rshift</li>
 *  <li>lctrl</li>
 *  <li>rctrl</li>
 * 	<li>alt</li>
 * 	<li>altgr</li>
 * </ol>
 * Notice that there is an optional entry <i>enter2</i> for the case that the enter key does
 * not have a rectangular form and it must be represented with two rectangles.
 * 
 * <p>Last, the file <i>set_letters_lshift_XX_xx.txt</i> contains all letters (not separated by
 * white spaces) that need the left shift modifier key if they are capitals, as opposed to letters
 * that need the right shift modifier key.
 * 
 * @author Lasse Osterhagen
 *
 */
public class KeyMapper {
	
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private Set<Character> lShiftLetters = new HashSet<>();
	private Map<Character, Coordinates> letters = new HashMap<>();
	private Map<Character, Coordinates> noShift = new HashMap<>();
	private Map<Character, Coordinates> lShift = new HashMap<>();
	private Map<Character, Coordinates> rShift = new HashMap<>();
	private Map<Character, Coordinates> altgr = new HashMap<>();
	private Coordinates[] enterCo;
	private Coordinates lShiftCo;
	private Coordinates rShiftCo;
	private Coordinates lCtrlCo;
	private Coordinates rCtrlCo;
	private Coordinates altCo;	
	private Coordinates altgrCo;
	
	/**
	 * Create a KeyMapper for the specified locale
	 * @param layoutID a complete language_country Locale like <i>en_GB</i> or <i>de_DE</i>
	 * @throws IOException 
	 */
	public KeyMapper(String layoutID) {
		try {
			fillSetLShiftLetter("set_letters_lshift_" + layoutID + ".txt");
			fillMap("co_letters_" + layoutID + ".csv", letters);
			fillMap("co_noshift_" + layoutID + ".csv", noShift);
			fillMap("co_lshift_" + layoutID + ".csv", lShift);
			fillMap("co_rshift_" + layoutID + ".csv", rShift);
			fillMap("co_altgr_" + layoutID + ".csv", altgr);
			fillSpecials("co_specials_" + layoutID + ".csv");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get the <code>Coordinates</code> of all keys that correspond to the <code>KeyEvent</code>.
	 * @param k the KeyEvent
	 * @return Coordinates for every key involved in creating the KeyEvent
	 * @throws NotInKeySetException if the keys do not exist on the keyboard
	 */
	public Coordinates[] getCoordinatesFor(KeyEvent k) throws NotInKeySetException {
		char c = k.getKeyChar();
		if(!(c == KeyEvent.CHAR_UNDEFINED))
			return getCoordinatesFor(c);
		int keyCode = k.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_SHIFT:
			if(k.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT)
				return new Coordinates[] {lShiftCo};
			return new Coordinates[] {rShiftCo};
		case KeyEvent.VK_CONTROL:
			if(k.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT)
				return new Coordinates[] {lCtrlCo};
			return new Coordinates[] {rCtrlCo};
		case KeyEvent.VK_ALT:
			return new Coordinates[] {altCo};
		case KeyEvent.VK_ALT_GRAPH:
			return new Coordinates[] {altgrCo};
		default:
			throw new NotInKeySetException(c);
		}
	}
	
	/**
	 * Get the <code>Coordinates</code> of all keys that correspond to the character.
	 * @param c the character
	 * @return Coordinates for every key involved in creating the character
	 * @throws NotInKeySetException if the keys do not exist on the keyboard
	 */
	public Coordinates[] getCoordinatesFor(char c) throws NotInKeySetException {
		Coordinates[] co = null;
		// letter
		if(Character.isLetter(c)) {
			char cLower = Character.toLowerCase(c);
			// with shift key
			if(Character.isUpperCase(c)) {
				co = new Coordinates[2];
				if(lShiftLetters.contains(cLower))
					co[1] = lShiftCo;
				else
					co[1] = rShiftCo;
			}
			else {
				co = new Coordinates[1];
			}
			if(!letters.containsKey(cLower))
				throw new NotInKeySetException(c);
			co[0] = letters.get(cLower);
		}
		// no letter
		else if(c == '\n')
			return enterCo;
		else if(noShift.containsKey(c)) {
			// no additional modifier key
			co = new Coordinates[1];
			co[0] = noShift.get(c);
		}
		else {
			// with modifier key
			co = new Coordinates[2];
			if(lShift.containsKey(c)) {
				co[1] = lShiftCo;
				co[0] = lShift.get(c);
			}
			else if(rShift.containsKey(c)) {
				co[1] = rShiftCo;
				co[0] = rShift.get(c);
			}
			else if(altgr.containsKey(c)) {
				co[1] = altgrCo;
				co[0] = altgr.get(c);
			}
			else
				throw new NotInKeySetException(c);
		}
		return co;
	}
	
	private void fillSetLShiftLetter(String sourceFileName) throws IOException {
		try(InputStream is = KeyMapper.class.getResourceAsStream(sourceFileName)) {
			if(is == null)
				throw new RuntimeException("Missing resource: " + sourceFileName);
			try(InputStreamReader r = new InputStreamReader(is, CHARSET)) {
				int lShiftLetter;
				while((lShiftLetter = r.read()) != -1) {
					lShiftLetters.add( (char) lShiftLetter);
				}
			}
		}
	}
	
	private void fillSpecials(String sourceFileName) throws IOException {
		try(InputStream is = KeyMapper.class.getResourceAsStream(sourceFileName)) {
			if(is == null)
				throw new RuntimeException("Missing resource: " + sourceFileName);
			try(Scanner s = new Scanner(is, CHARSET.name())) {
				// enter key
				if(!(s.next().equals("enter1")))
					throw new RuntimeException("enter1" + " expected.");
				Coordinates enterCo1 = readCoordinates(s);
				String nextKey = s.next();
				if(nextKey.equals("enter2")) {
					enterCo = new Coordinates[] {enterCo1, readCoordinates(s)};
					nextKey = s.next();
				}
				else {
					enterCo = new Coordinates[] {enterCo1};
				}
				
				// next key must be "space"
				if(!(nextKey.equals("space")))
					throw new RuntimeException("space" + " expected.");
				noShift.put(' ', readCoordinates(s));
				
				// left shift
				if(!(s.next().equals("lshift")))
					throw new RuntimeException("lshift" + " expected.");
				lShiftCo = readCoordinates(s);
				
				// right shift
				if(!(s.next().equals("rshift")))
					throw new RuntimeException("rshift" + " expected.");
				rShiftCo = readCoordinates(s);
				
				// left ctrl
				if(!(s.next().equals("lctrl")))
					throw new RuntimeException("lctrl" + " expected.");
				lCtrlCo = readCoordinates(s);
				
				// right ctrl
				if(!(s.next().equals("rctrl")))
					throw new RuntimeException("rctrl" + " expected.");
				rCtrlCo = readCoordinates(s);
				
				// alt
				if(!(s.next().equals("alt")))
					throw new RuntimeException("alt" + " expected.");
				altCo = readCoordinates(s);
				
				// altgr
				if(!(s.next().equals("altgr")))
					throw new RuntimeException("altgr" + " expected.");
				altgrCo = readCoordinates(s);
			}
					
		}

	}
	
	private static void fillMap(String sourceFileName, Map<Character, Coordinates> dest)
			throws IOException {
		try(InputStream is = KeyMapper.class.getResourceAsStream(sourceFileName)) {
			if(is == null)
				throw new RuntimeException("Missing resource: " + sourceFileName);
			try(Scanner s = new Scanner(KeyMapper.class.getResourceAsStream(sourceFileName),
					CHARSET.name())) {
				String c;
				while(s.hasNextLine()) {
					c = s.next();
					dest.put(c.charAt(0), readCoordinates(s));
				}
			}
		}
	}
	
	private static Coordinates readCoordinates(Scanner s) {
		Coordinates c = new Coordinates();
		c.x1 = s.nextInt();
		c.y1 = s.nextInt();
		c.x2 = s.nextInt();
		c.y2 = s.nextInt();
		return c;
	}
	
	/**
	 * There is no corresponding key on the keyboard layout.
	 */
	@SuppressWarnings("serial")
	public static class NotInKeySetException extends Exception {
		public final char c;
		public NotInKeySetException(char c) {
			super("Char '" + c +"' does not exist in the key set.");
			this.c = c;
		}
	}

}
