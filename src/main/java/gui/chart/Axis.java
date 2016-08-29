package gui.chart;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * <p>Abstract base class for axes that are part of a {@link Chart}
 * 
 * <p>The graphics context must point to the origin of the axis in order to paint it at the correct
 * position.
 * 
 * @author Lasse Osterhagen
 *
 */

abstract class Axis {
	
	JPanel panel;
	Range range = new Range(); // range of the axis [min, max]
	int decimalPrecision = 2; // number of decimal places
	Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12); // tick label font
	int tickLength = 5; // length of tick line
	
	Axis(JPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * Get the offset between the outer boundary of the painting area and the axis line.
	 * For a x-axis, this is a vertical offset; for a y-axis, it is a horizontal offset. 
	 * @return the offset between outer boundary and axis line
	 */
	abstract int getAxisOffset();
	
	/**
	 * Paint the axis inside the free paining area. The cursor of the graphics context
	 * must point to the origin.
	 * @param g the graphics context
	 * @param freeArea the free painting area available
	 * @return scaling factor for this axis
	 */
	abstract double paint(Graphics2D g, Dimension freeArea);
	
	/**
	 * Computes the maximum width of a label string.
	 * @param fm FontMetrics of the label font
	 * @return the maximum width of a label string
	 */
	int getLabelMaxWidth(FontMetrics fm) {
		return Math.max(
				fm.stringWidth(formatDouble(range.min)),
				fm.stringWidth(formatDouble(range.max))
				);
	}
	
	/**
	 * Formats a double value according to decimalPrecision
	 * @param value the double value to be formatted
	 * @return formatted string representation of the double value
	 */
	String formatDouble(double value) {
		return String.format("%." + decimalPrecision + "f", value);
	}
	
	/**
	 * Round a double value to the next lower attractor.
	 * @param val the value to be rounded
	 * @return the next lower attractor
	 */
	static double roundNicely(double val) {
		double exp = Math.floor(Math.log10(val));
		double transVal = val*Math.pow(10, -exp);
		double newVal = 10;
		if(transVal <= 5)
			newVal = 5;
		if(transVal <= 2)
			newVal = 2;
		return newVal*Math.pow(10, exp);
	}
}
