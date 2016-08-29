package trainer;

/**
 * Class for shortcut object creation.
 * 
 * @author Lasse Osterhagen
 *
 */

public class DefaultObjectFactory {
	
	/**
	 * Create a default <tt>ExerciseGroup</tt> object named <i>TestGroup</i> with
	 * <tt>editable</tt> set to <tt>false</tt>.
	 * @return a default <tt>Exercise</tt> instance
	 */
	public static ExerciseGroup getExerciseGroup() {
		return new ExerciseGroup("TestGroup", false);
	}
	
	/**
	 * Create a default <tt>Exercise</tt> object named <i>TestExercise</i> with
	 * an id of <i>-1</i>, a default <tt>ExerciseGroup</tt>, <i>GENERIC_RAND</i>
	 * as <tt>LineCreator</tt>, <tt>param</tt> set to <i>abcde</i>,
	 * <tt>Exercise.LimitType.CHARS</tt> with value <i>20</i>.
	 * 
	 * @return a default <tt>Exercise</tt> instance
	 */
	public static Exercise getExercise() {
		return new Exercise(-1, "TestExercise", getExerciseGroup(),
				"GENERIC_RAND", "abcde", Exercise.LimitType.CHARS, 20);
	}

}
