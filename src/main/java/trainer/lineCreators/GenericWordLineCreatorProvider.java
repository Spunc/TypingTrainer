package trainer.lineCreators;

import java.util.Random;
import java.util.ResourceBundle;

import trainer.PerformanceStats;

/**
 * This class creates lines of generic words. It uses a <tt>GenericWordCreator</tt> to
 * get words and inserts spaces between words.
 * 
 * @author Lasse Osterhagen
 *
 */

public class GenericWordLineCreatorProvider implements LineCreatorProvider {
	
	private GenericWordCreatorSupplier wordCreatorSupplier;
	private int[] wordLenDistr;
	private String description;
	
	public interface GenericWordCreatorSupplier {
		GenericWordCreator create(String param, PerformanceStats ps);
	}
	 
	public GenericWordLineCreatorProvider(String descriptionKey,
			GenericWordCreatorSupplier wordCreatorSupplier, int[] wordLengthDistribution) {
		this.wordCreatorSupplier = wordCreatorSupplier;
		this.wordLenDistr = wordLengthDistribution;
		description = ResourceBundle.getBundle("txtBundles.lineCreatorText")
				.getString(descriptionKey);
	}
	
	
	private class GenericWordLineCreator implements LineCreator {
		
		private GenericWordCreator wordCreator;
		private Random random = new Random();
		
		public GenericWordLineCreator(GenericWordCreator wordCreator) {
			this.wordCreator = wordCreator;
		}
		/**
		 * Creates a line of words.
		 * @param maxLength the maximum length of <strong>printable</strong> chars
		 * @return a string of words, ending with a newline char
		 */
		@Override
		public String create(int maxLength) {
			StringBuilder sb = new StringBuilder(maxLength+1);
			int wordLength;
			while(maxLength > 0) {
				wordLength = wordLenDistr[random.nextInt(wordLenDistr.length)];
				if(maxLength - wordLength < 0)
					break;
				maxLength = maxLength-wordLength-1; // minus space
				sb.append(wordCreator.create(wordLength));
				sb.append(' ');
			}
			//delete last space before line break
			sb.setLength(sb.length()-1);
			//append a newline character
			sb.append('\n');
			return sb.toString();
		}
	}

	@Override
	public LineCreator getLineCreator(String param, PerformanceStats ps) {
		return new GenericWordLineCreator(wordCreatorSupplier.create(param, ps));
	}

	@Override
	public String description() {
		return description;
	}

}
