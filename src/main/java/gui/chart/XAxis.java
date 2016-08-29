package gui.chart;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

class XAxis extends Axis {
	
	/**
	 * The position of the axis. Can be on top (<code>Top</code>) or below (<code>Down</code>).
	 * Default is <code>Down</code>.
	 */
	public enum Position {
		Top,
		Down
		}
	
	Position position = Position.Down;

	public XAxis(JPanel panel) {
		super(panel);
	}

	@Override
	int getAxisOffset() {
		return tickLength + panel.getFontMetrics(font).getHeight();
	}

	@Override
	double paint(Graphics2D g, Dimension freeArea) {
		FontMetrics fm = g.getFontMetrics(font);
		g.setFont(font);
		double scaleFactor = freeArea.getWidth()/range.getLength();
		// draw axis line
		g.drawLine(0, 0, freeArea.width, 0);
		
		int posFactor, labelPosFactor;
		switch(position) {
		case Top:
			posFactor = -1;
			labelPosFactor = fm.getDescent();
			break;
		default:
			posFactor = 1;
			labelPosFactor = fm.getHeight();
		}
		int maxTickLabelWidth = getLabelMaxWidth(fm) + fm.charWidth(' ')*2;
		int maxNumTicks = freeArea.width / maxTickLabelWidth;
		if(maxNumTicks == 0) return scaleFactor;
		double tick = roundNicely(range.getLength()/maxNumTicks);
		tick = Math.max(tick, Math.pow(10, -decimalPrecision));
		double nextTick = Math.ceil(range.min/tick)*tick;
		int tickpos = (int) ((nextTick-range.min)*scaleFactor);
		String label = formatDouble(nextTick);
		while(tickpos+fm.stringWidth(label)/2 < freeArea.width) {
			// Paint this tick
			g.drawLine(tickpos, 0, tickpos, posFactor*tickLength);
			g.drawString(label, tickpos-fm.stringWidth(label)/2,
					posFactor*(tickLength+labelPosFactor));
			// Compute next tick
			nextTick += tick;
			tickpos = (int) ((nextTick-range.min)*scaleFactor);
			label = formatDouble(nextTick);
		}
		return scaleFactor;
	}

}
