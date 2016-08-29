package trainer.lineCreators;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * This class creates words of a predefined length by randomly
 * choosing chars from the provided charSet.
 * 
 * @author Lasse Osterhagen
 *
 */

public class RandWordCreator implements GenericWordCreator {
	
	private String charSet;
	
	/**
	 * Construct a RandWordCreator.
	 * @param charSet the character set from which words should be constructed. To give
	 * some characters more weight during the random selection process, include them
	 * more often in the char set.
	 */
	public RandWordCreator(String charSet) {
		this.charSet = charSet;
	}
	
	/**
	 * Create a word of a predefined length.
	 * @param length the length of the word to be created
	 * @return the word
	 */
	@Override
	public String create(int length) {
		Random r = new Random();
		IntStream s = r.ints(length, 0, charSet.length());
		StringBuilder sb = new StringBuilder(length);
		s.forEach(index -> sb.append(charSet.charAt(index)));
		return sb.toString();
	}

}
