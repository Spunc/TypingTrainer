package trainer.lineCreators;

/**
 * A LineCreator for testing purposes. It always returns the same line for the
 * specified number of times. After all lines have been consumed, a call to
 * hasNext() will result in false. Additional calls to create() will throw
 * IllegalState exceptions.
 * 
 * @author Lasse Osterhagen
 *
 */
public class TestLineCreator implements LineCreator {
	
	private String staticLine;
	private int repetitions;
	
	/**
	 * Create a TestLineCreator object.
	 * @param staticLine the line that will be returned after calls to create(). The line
	 * must be of the same size as later calls to create(<strong>length</strong>. Do not
	 * append a new-line char. New-line chars will be appended by TestLineCreator.
	 * @param repetitions how often a call to create will supply the line
	 */
	public TestLineCreator(String staticLine, int repetitions) {
		this.staticLine = staticLine;
		this.repetitions = repetitions;
	}

	@Override
	public String create(int length) {
		if(length > staticLine.length())
			throw new IllegalArgumentException("Demanded length greater than length of staticLine.");
		if(repetitions > 1) {
			--repetitions;
			return staticLine + '\n';
		}
		else if(repetitions == 1) {
			--repetitions;
			return staticLine;
		}
		else
			throw new IllegalStateException("LineCreator empty.");
	}
	
	@Override
	public boolean hasNext() {
		return repetitions > 0;
	}

}
