package gui.chart;

import java.awt.FontMetrics;

public class Util {
	
	/**
	 * Get the vertical spacing between text lines.
	 * @param fm metrics of the font used
	 * @return the vertical spacing between text lines
	 */
	public static int getLineSpace(FontMetrics fm) {
		return (int) (fm.getHeight()*1.5); // using fixed spacing factor
	}

}
