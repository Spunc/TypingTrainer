package gui.keyboard;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class KeyMapper {
	
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private Set<Character> lShiftLetters = new HashSet<>();
	private Map<Character, Coordinates> letters = new HashMap<>();
	private Map<Character, Coordinates> noShift = new HashMap<>();
	private Map<Character, Coordinates> lShift = new HashMap<>();
	private Map<Character, Coordinates> rShift = new HashMap<>();
	private Map<Character, Coordinates> altgr = new HashMap<>();
	private Coordinates lShiftCo;
	private Coordinates rShiftCo;
	private Coordinates altgrCo;
	
	public KeyMapper(String localeID) {
		try {
			fillSetLShiftLetter(localeID);
			fillMap("co_letters_" + localeID + ".csv", letters);
			fillMap("co_noshift_" + localeID + ".csv", noShift);
			fillMap("co_lshift_" + localeID + ".csv", lShift);
			fillMap("co_rshift_" + localeID + ".csv", rShift);
			fillMap("co_altgr_" + localeID + ".csv", altgr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fillSpecials(localeID);
	}
	
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
				throw new NotInKeySetException();
			co[0] = letters.get(cLower);
		}
		// no letter
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
				throw new NotInKeySetException();
		}
		return co;
	}
	
	private void fillSetLShiftLetter(String localeID) throws IOException {
		try(InputStreamReader r = new InputStreamReader(
				KeyMapper.class.getResourceAsStream(
						"set_letters_lshift_" + localeID +".txt"), CHARSET)) {
			int lShiftLetter;
			while((lShiftLetter = r.read()) != -1) {
				lShiftLetters.add( (char) lShiftLetter);
			}
		}
	}
	
	private void fillSpecials(String localeID) {
		try(Scanner s = new Scanner(KeyMapper.class.getResourceAsStream(
					"co_specials_" + localeID + ".txt"))) {
			// space
			if(!(s.next().equals("space")))
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
			if(!(s.next().equals("altgr")))
				throw new RuntimeException("altgr" + " expected.");
			altgrCo = readCoordinates(s);
		}
	}
	
	private static void fillMap(String sourceFileName, Map<Character, Coordinates> dest) {
		try(Scanner s = new Scanner(KeyMapper.class.getResourceAsStream(sourceFileName),
				CHARSET.name())) {
			String c;
			while(s.hasNextLine()) {
				c = s.next();
				dest.put(c.charAt(0), readCoordinates(s));
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
	
	@SuppressWarnings("serial")
	public static class NotInKeySetException extends Exception {}

}
