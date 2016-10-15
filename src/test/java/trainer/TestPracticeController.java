package trainer;

import trainer.lineCreators.LineCreator;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;
import trainer.lineCreators.LineCreatorProvider.InitException;

/**
 * A <tt>PracticeController</tt> that allows to set the <tt>LineCreator</tt> for
 * testing purposes. Usage: call setLineCreator() after object creation.
 * 
 * @author Lasse Osterhagen
 *
 */
public class TestPracticeController extends PracticeController {

	public TestPracticeController(Exercise exercise, int maxLineLength)
			throws ImplementationNotFound, InitException {
		super(exercise, maxLineLength);
	}
	
	public void setLineCreator(LineCreator lineCreator) {
		this.lineCreator = lineCreator;
	}

}
