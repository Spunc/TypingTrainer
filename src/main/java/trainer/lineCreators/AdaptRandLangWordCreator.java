package trainer.lineCreators;

import trainer.PerformanceStats;

/**
 * Similar to {@link RandLangWordCreator} but with adaptive feature like in
 * {@link AdaptRandWordCreator}
 * 
 * @author Lasse Osterhagen
 *
 */

public class AdaptRandLangWordCreator extends AdaptRandWordCreator {

	public AdaptRandLangWordCreator(String charSet, PerformanceStats ps) {
		super(charSet, ps);
	}
	
	protected GenericWordCreator getStringCreator(String adaptCharSet) {
		return new RandLangWordCreator(adaptCharSet);
	}

}
