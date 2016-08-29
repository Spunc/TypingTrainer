package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

/**
 * A gui component that draws a line of text surrounded by a boarder.
 * The length of the string that can be displayed, as well as the font,
 * is fixed at creation time. The size of the graphical display will
 * be computed from those constants.
 * 
 * @author Lasse Osterhagen
 *
 */

@SuppressWarnings("serial")
public class LineDisplay extends Component {
	
	private static final int MARGIN = 10;
	private int maxTextSize;
	private int textYpos;
	private Font textFont;
	protected AttributedString textLine;
	protected static final Font defaultFont = new Font(Font.MONOSPACED, Font.BOLD, 19);
	
	/**
	 * Create a LineDisplay component.
	 * @param maxTextSize the maximum number of printable chars that can be displayed
	 * @param textFont the font in which the string will be displayed
	 */
	public LineDisplay(int maxTextSize, Font textFont) {
		this.maxTextSize = maxTextSize;
		this.textFont = textFont;
		calcSize();
	}
	
	/**
	 * Create a LineDisplay component.
	 * @param maxTextSize the maximum string length to be displayed
	 */
	public LineDisplay(int maxTextSize) {
		this(maxTextSize, defaultFont);
	}
	
	private void calcSize() {
		FontMetrics fm = getFontMetrics(textFont);
		//use character 'm' to get an approximation of the maximum width of a char
		int w, h;
		w = fm.charWidth('m')*maxTextSize + MARGIN*2;
		h = fm.getMaxAscent() + fm.getMaxDescent() + MARGIN*2;
		setPreferredSize(new Dimension(w, h));
		textYpos = fm.getMaxAscent() + MARGIN;
	}
	
	/**
	 * Set a new text line to be displayed.
	 * @param textLine the new text line
	 */
	public void setTextLine(String textLine) {
		if(textLine.equals("") || textLine == null) {
			this.textLine = null;
			return;
		}
		if(textLine.length() > maxTextSize+1) //allow a last newline char
			throw new IllegalArgumentException("Text row too long: "
					+ "was: " +textLine.length());
		this.textLine = new AttributedString(textLine);
		this.textLine.addAttribute(TextAttribute.FONT, textFont);
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Dimension size = getSize();
		
		// Draw a boarder
		g2.drawRect(0, 0, size.width-1, size.height-1);
		
		//Draw text line
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		if(textLine != null) {
			g2.drawString(textLine.getIterator(), MARGIN, textYpos);
		}
	}

}
