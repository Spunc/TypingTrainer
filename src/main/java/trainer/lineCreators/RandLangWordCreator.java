package trainer.lineCreators;

import java.util.Random;

/**
 * <p>Create pseudo words consisting of random chars, but with language-like
 * punctuation.
 * 
 * <p>Some created words will start with upper cases. Some words will end with
 * punctuation that can occur within sentences. Some word will end with punctuation
 * that stand at the end of a sentence. 
 * 
 * @author Lasse Osterhagen
 *
 */
public class RandLangWordCreator implements GenericWordCreator {
	
	private final static String endWordCharsCmp = ",;:-";
	private final static String endSentenceCharsCmp = ".!?";
	private String lowerCases;
	private String upperCases;
	private String endWordChars;
	private String endSentenceChars;
	// percentage of upper case chars at word begin
	private final double fractionUpper = .4;
	// percentage of end chars at word end
	private final double fractionEndChars = .35;
	// percentage of end chars that are sentence end chars
	private final double fractionSentenceEndChars = .6;
	// true if last char added was an endSentenceChar
	private boolean sentenceStart = true;
	
	private Random random = new Random();
	
	/**
	 * Class constructor.
	 * 
	 * @param charSet The character set from which the language-like words will
	 * 	be created. charSet must contain upper case letters, lower case letters,
	 * 	and at least one of the following punctuation characters: '.', '!', or '?'.
	 */
	public RandLangWordCreator(String charSet) {
		// Validate charSet argument
		StringBuilder uppers = new StringBuilder();
		StringBuilder lowers = new StringBuilder();
		StringBuilder endWChars = new StringBuilder();
		StringBuilder endSChars = new StringBuilder();
		for(int i=0; i<charSet.length(); ++i) {
			char c = charSet.charAt(i);
			if(Character.isLowerCase(c))
				lowers.append(c);
			else if(Character.isUpperCase(c))
				uppers.append(c);
			else if(endWordCharsCmp.indexOf(c) >= 0)
				endWChars.append(c);
			else if(endSentenceCharsCmp.indexOf(c) >= 0)
				endSChars.append(c);
		}
		lowerCases = lowers.toString();
		upperCases = uppers.toString();
		endWordChars = endWChars.toString();
		endSentenceChars = endSChars.toString();
		if( lowerCases.length() == 0 ||
			upperCases.length() == 0 ||
			endSentenceChars.length() == 0)
			throw new IllegalArgumentException("The specified charSet is invalid:\n"
					+ "it must contain lower case letters, upper case letters, and\n"
					+ "one of the following chars: '.', '!', or '?'");
	}

	@Override
	public String create(int length) {
		StringBuffer sb = new StringBuffer(length);
		
		// First char
		if(sentenceStart || random.nextDouble() < fractionUpper) {
			sb.append(upperCases.charAt(random.nextInt(upperCases.length())));
			sentenceStart = false;
		}
		else {
			sb.append(lowerCases.charAt(random.nextInt(lowerCases.length())));
		}
		
		//Chars between
		for(int i=1; i<length-1; ++i) {
			sb.append(lowerCases.charAt(random.nextInt(lowerCases.length())));
		}

		//Last char
		if(random.nextDouble() < fractionEndChars) {
			if(endWordChars.length() == 0 ||
					random.nextDouble() < fractionSentenceEndChars) {
				// Append a sentence ending punctuation
				sb.append(endSentenceChars.charAt(random.nextInt(endSentenceChars.length())));
				sentenceStart = true;
			}
			else {
				// Append a word ending punctuation
				sb.append(endWordChars.charAt(random.nextInt(endWordChars.length())));
			}
		}
		else {
			sb.append(lowerCases.charAt(random.nextInt(lowerCases.length())));
		}

		return sb.toString();
	}

}
