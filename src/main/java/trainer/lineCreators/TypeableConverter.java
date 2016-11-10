package trainer.lineCreators;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 * Convert a unicode text so that it can be typed with a given country-dependent keyboard layout.
 * 
 * <p>Country dependency of a keyboard layout assumes, that different layouts within the same
 * country (e. g. Qwerty vs. Dvorak) all have the same set of typeable characters.
 * 
 * <p>Characters that cannot be typed with a specific keyboard layout will be either replaced
 * by similar characters that can by typed (e. g. letters with accents by the same letters
 * without the accents), or replaced by a question mark (foreign letters and symbols).
 * 
 * <p>The current implementation assumes that ASCII characters generally can be typed. Additional
 * characters that can be typed are defined in a .json file named <i>typableChars_XX.json</i> with
 * the country ID in the place of <i>XX</i>. The following specification file for Germany
 * (<i>DE</i>) exemplifies the structure of the .json file:
 * <pre>
 * {@code
 * {
 *	"specials": "\\u00df$",
 *	"allowedAccents": [
 *		{"\\u0308": "aouAOU"}
 *	]
 * }
 * }
 * </pre>
 * 
 * This specification defines two additional characters that can be typed with a German keyboard
 * under the field "specials". Furthermore, it defines one accent ("\\u0308") that, if composed
 * with the one of the letters from the string "aouAOU", forms typeable characters. Additional
 * accents that form typeable characters with other characters can be appended to the array under
 * the field "allowedAccents".
 * 
 * @author Lasse Osterhagen
 *
 */

public class TypeableConverter {
	
	private String specialChars;
	private Map<Character, String> allowedAccents = new HashMap<>();
	
	/**
	 * Create a TypeableConverter for a specific country-dependent keyboard layout
	 * @param countryID the country ID of the keyboard
	 */
	public TypeableConverter(String countryID) {
		String fileName = "typableChars_" + countryID + ".json";
		try(InputStream is = TypeableConverter.class.getResourceAsStream(fileName)) {
			if(is == null)
				throw new RuntimeException("Missing resource: " + fileName);
			parseJson(Json.createParser(is));
			
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Convert a text with potentially non-typeable characters into a text that consists of
	 * only typeable characters.
	 * @param text the original text
	 * @return the converted text
	 */
	public String convert(String text) {
		StringBuilder sb = new StringBuilder(text.length());
		text = Normalizer.normalize(text, Normalizer.Form.NFKD);
	    for(int i = 0, n = text.length(); i < n; ++i) {
	        char c = text.charAt(i);
	        if(c <= '\u007F')
	        	// typical ASCII char
	        	sb.append(c);
	        else if(allowedAccents.containsKey(c)
	        		&& i>0
	        		&& allowedAccents.get(c).contains(sb.substring(sb.length()-1)))
	        	// allowed accent that follows a letter for which this accent is allowed
	        	sb.replace(sb.length()-1, sb.length(),
	        			Normalizer.normalize(text.substring(i-1,i+1), Normalizer.Form.NFC));
	        else if(specialChars.contains(Character.toString(c)))
	        	// predefined allowed special (non-ASCII) character
	        	sb.append(c);
	        else if(!(c >= '\u0300' && c <= '\u036F'))
	        	// not a combining accent
	        	sb.append('?');
	        // else, i. e. it is a combining accent: do not include into resulting text
	    }
	    return sb.toString();
	}
	
	private void parseJson(JsonParser jp) {
		while(jp.hasNext()) {
			Event event = jp.next();
			if(event == Event.KEY_NAME)
				switch(jp.getString()) {
				case "specials":
					jp.next(); // VALUE_STRING
					specialChars = jp.getString();
					break;
				case "allowedAccents":
					readAllowedAccents(jp);
				}
		}
	}

	private void readAllowedAccents(JsonParser jp) {
		if(!(jp.next() == Event.START_ARRAY))
			throw new RuntimeException("Malformed .json");
		// read keys
		while(!(jp.next() == Event.END_ARRAY)) {
			jp.next(); // KEY_NAME
			Character key = jp.getString().charAt(0);
			jp.next(); // VALUE_STRING
			allowedAccents.put(key, jp.getString());
			jp.next(); // END_OBJECT
		}
	}

}
