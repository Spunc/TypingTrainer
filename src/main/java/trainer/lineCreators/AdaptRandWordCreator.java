package trainer.lineCreators;

import java.util.Map.Entry;

import trainer.PerformanceRate;
import trainer.PerformanceStats;

import java.util.Set;

/**
 * Similar as {@link RandWordCreator}, but the probability of occurrence of a char depends
 * on the current {@link trainer.PerformanceStats}, with a high error rate for a given char
 * increasing the chance of its occurrence.
 * 
 * @author Lasse Osterhagen
 *
 */

public class AdaptRandWordCreator implements GenericWordCreator {
	
	public static final double ADAPT_VAL = 60.0;
	private String charSet;
	private PerformanceStats ps;
	private double adaptFactor;
	
	/**
	 * Defines how the <i>adapted</i> charSet will be used to create generic words.
	 * Override this method in subclasses to provide different word creation
	 * strategies.
	 * @param adaptCharSet the <i>adapted</i> char set.
	 * The <i>adapted</i> char set consists of chars from which new generic words
	 * should be constructed. The frequency of recurrence of an individual char in the
	 * set increases with the typists error rate for that char.
	 * @return the implementation of <tt>GenericWordCreator</tt> that will be used
	 * to create generic words (this class: {@link RandWordCreator}.
	 */
	protected GenericWordCreator getStringCreator(String adaptCharSet) {
		return new RandWordCreator(adaptCharSet);
	}
	
	/**
	 * Class constructor.
	 * @param charSet the initial char set from which generic words should be created
	 * @param ps the current performance statistics of the typist
	 */
	public AdaptRandWordCreator(String charSet, PerformanceStats ps) {
		this.charSet = charSet;
		this.ps = ps;
		this.adaptFactor = ADAPT_VAL/charSet.length();
	}
	
	/**
	 * Create a generic word.
	 * @param length the length of the word
	 * @return the created generic word
	 */
	@Override
	public String create(int length) {
		StringBuilder chars = new StringBuilder(3*charSet.length());
		
		// Create the adapted charSet
		// The adapted charSet consists at least of the initial charSet
		chars.append(charSet);
		Set<Entry<Character, PerformanceRate>> hits_errors = ps.getHits_errors();
		hits_errors.stream()
			.forEach(e -> {
				for(int i=0; i<e.getValue().getErrorRate()*adaptFactor; ++i) {
					chars.append(e.getKey());
				}
			});
		return getStringCreator(chars.toString()).create(length);
	}

}
