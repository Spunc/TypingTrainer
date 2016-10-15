package trainer.lineCreators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple encoding of a <code>Map</code> into a single <code>String</code> and backwards.
 * 
 * @author Lasse Osterhagen
 *
 */
public class StringCode {
	
	/**
	 * Encoding using default parameters.
	 * @see #encode(Map, String, String)
	 * @param map the map to be encoded
	 * @return the String-encoded map
	 */
	public static String encode(Map<String, String> map) {
		return encode(map, ";", "=");
	}
	
	/**
	 * Encode a <code>Map</code> into a single <code>String</code>.
	 * @param map the map to be encoded
	 * @param pairDelimiter the delimiter between map entries [default: ";"]
	 * @param keyValueDelimiter the delimiter between key/value pairs [default: "="]
	 * @return the String-encoded Map
	 */
	public static String encode(Map<String, String> map, String pairDelimiter,
			String keyValueDelimiter) {
		StringBuilder sb = new StringBuilder();
		map.entrySet().stream().forEach( e -> {
			String key = e.getKey();
			String value = e.getValue();
			if(key.contains(pairDelimiter) ||
					key.contains(keyValueDelimiter))
				throw new RuntimeException("Delimiter in key string.");
			if(value.contains(pairDelimiter) ||
					value.contains(keyValueDelimiter))
				throw new RuntimeException("Delimiter in value string.");
			sb.append(e.getKey());
			sb.append(keyValueDelimiter);
			sb.append(e.getValue());
			sb.append(pairDelimiter);
		});
		return sb.toString();
	}
	
	/**
	 * Decoding using default parameters.
	 * @see #decode(String, String, String)
	 * @param s the String to be decoded
	 * @return the decoded  Map
	 */
	public static HashMap<String, String> decode(String s) {
		return decode(s, ";", "=");
	}
	
	/**
	 * Decode a <code>Map</code> from a <code>String</code>-encoding.
	 * @param s the encoded String
	 * @param pairDelimiter the delimiter between map entries [default: ";"]
	 * @param keyValueDelimiter the delimiter between key/value pairs [default: "="]
	 * @return the decoded Map
	 */
	public static HashMap<String, String> decode(String s, String pairDelimiter,
			String keyValueDelimiter) {
		HashMap<String, String> map = new HashMap<>();
		String[] pairs = s.split(pairDelimiter);
		Arrays.stream(pairs).forEach(p -> {
			String[] kv = p.split(keyValueDelimiter);
			map.put(kv[0], kv[1]);
		});
		return map;
	}

}
