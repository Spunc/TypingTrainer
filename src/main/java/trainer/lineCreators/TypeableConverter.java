package trainer.lineCreators;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;


public class TypeableConverter {
	
	private String specialChars;
	private Map<Character, String> allowedAccents = new HashMap<>();
	
	public TypeableConverter(String layoutID) {
		String fileName = "typableChars_" + layoutID + ".json";
		try(InputStream is = TypeableConverter.class.getResourceAsStream(fileName)) {
			if(is == null)
				throw new RuntimeException("Missing resource: " + fileName);
			parseJson(Json.createParser(is));
			
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String convert(String text) {
		StringBuilder sb = new StringBuilder(text.length());
		text = Normalizer.normalize(text, Normalizer.Form.NFD);
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
	        else if(Character.isLetter(c))
	        	// non typeable letters
	        	sb.append('?');
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
