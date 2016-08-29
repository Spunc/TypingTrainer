package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;

import javax.swing.Timer;

/**
 * A <tt>LineDisplay</tt> class that allows to highlight chars and to display an error signal.
 * 
 * Highlighting always starts from the first char (left side) and extends to the specified
 * numbers of chars. The error signal shortly displays a colored rectangle, appearing as
 * a blinking, instead of the text line.
 * 
 * @author Lasse Osterhagen
 *
 */

@SuppressWarnings("serial")
public class ColorLineDisplay extends LineDisplay {
	
	private Color highlightColor = Color.red;
	private Color errorColor = Color.red;
	private int signalTime = 150;
	private boolean errorSignal = false;
	
	/**
	 * Create a ColorLineDisplay component.
	 * @param maxTextSize the maximum string length to be displayed
	 * @param textFont the font in which the string will be displayed
	 */	
	public ColorLineDisplay(int maxTextSize, Font textFont) {
		super(maxTextSize, textFont);
		setFocusable(true);
	}
	
	/**
	 * Create a ColorLineDisplay component.
	 * @param maxTextSize the maximum string length to be displayed
	 */	
	public ColorLineDisplay(int maxTextSize) {
		this(maxTextSize, defaultFont);
	}
	
	/**
	 * Set the font color of the highlighted chars.
	 * @param highlightColor the font color
	 */
	public void setHighlightColor(Color highlightColor) {
		this.highlightColor = highlightColor;
	}
	
	/**
	 * Set the number of highlighted chars, starting from the left side.
	 * @param count the number of highlighted chars
	 */
	public void setHighlighted(int count) {
		if(count <= 0) return; //don'n color a substring shorter than 1
		textLine.addAttribute(TextAttribute.FOREGROUND, highlightColor, 0, count);
		repaint();
	}
	
	/**
	 * Set the color of the rectangle that signals an error.
	 * @param errorColor the color of the rectangle
	 */
	public void setErrorColor(Color errorColor) {
		this.errorColor = errorColor;
	}
	
	/**
	 * Set the time that the rectangle of the error signal will be displayed.
	 * @param signalTime the signal time in ms
	 */
	public void setSignaleTime(int signalTime) {
		this.signalTime = signalTime;
	}
	
	/**
	 * Let the error signal be displayed.
	 */
	public void signalError() {
		// Let the paint method only paint a colored rectangle
		errorSignal = true;
		repaint();
		// Schedule the end of the error signal painting
		Timer timer = new Timer(signalTime,
				(evt)->{errorSignal=false;repaint();});
		timer.setRepeats(false);
		timer.start();
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Dimension size = getSize();
		if(errorSignal) {
			g2.setColor(errorColor);
			g2.fillRect(0, 0, size.width-1, size.height-1);
		}
		else {
			super.paint(g);
		}
	}

}
