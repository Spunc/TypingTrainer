package trainer.lineCreators;

import static org.junit.Assert.*;
import org.hamcrest.number.IsCloseTo;
import org.junit.Before;
import org.junit.Test;

import trainer.PerformanceStats;

public class AdaptRandWordCreatorTest {
	
	private final String charSet = "ab";
	private PerformanceStats ps;
	private AdaptRandWordCreator arwc;
	
	@Before
	public void setUp() {
		ps = new PerformanceStats();
		arwc = new AdaptRandWordCreator(charSet, ps);
	}
	
	/**
	 * Evaluate the correctness of the expected percentage for the char 'b'
	 * @param bExpectedPercentage the expected percentage of occurrence for 'b'
	 */
	private void eval_b_Percentage(double bExpectedPercentage) {
		String result = arwc.create(1000);
		int countB = 0;
		for(int i=0; i<result.length(); ++i) {
			if(result.charAt(i) == 'b')
				++countB;
		}
		double bObservedPercentage = countB/1000.0;
		assertThat(bObservedPercentage, IsCloseTo.closeTo(bExpectedPercentage, 0.08));
			// 0.08 as liberal evaluation
	}

	@Test
	public void testCreateWithMaxErrorRate() {
		ps.addError('a'); //error rate for a is 1
		final double bExpectedPercentage = 1/(1+AdaptRandWordCreator.ADAPT_VAL/charSet.length());
		/* With an error rate of 1, AdaptRandWordCreator.ADAPT_VAL/charSet.length() additional
		 * 'a's will be appended to original char set to create the adapt char set. This changes
		 * the ratio of 'a' to 'b' from 1:1 to 1:(1+AdaptRandWordCreator.ADAPT_VAL/charSet.length()).
		 */
		eval_b_Percentage(bExpectedPercentage);
	}
	
	@Test
	public void testWithNoErrors() {
		eval_b_Percentage(0.5); // 'a' and 'b' equally probable
	}

}
