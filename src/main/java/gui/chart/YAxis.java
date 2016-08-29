package gui.chart;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

class YAxis extends Axis {
	
	private int beforeTickGap = tickLength*2;

	public YAxis(JPanel panel) {
		super(panel);
	}

	@Override
	int getAxisOffset() {
		return getLabelMaxWidth(panel.getFontMetrics(font)) + beforeTickGap;
	}

	@Override
	double paint(Graphics2D g, Dimension freeArea) {
		FontMetrics fm = g.getFontMetrics(font);
		g.setFont(font);
		double scaleFactor = -freeArea.getHeight()/range.getLength();
		// draw axis line
		g.drawLine(0, 0, 0, -freeArea.height);
		
		int maxTickLabelHeight = fm.getHeight()+fm.getDescent();
		int maxNumTicks = freeArea.height / maxTickLabelHeight;
		if(maxNumTicks == 0) return scaleFactor;
		double tick = roundNicely(range.getLength()/maxNumTicks);
		tick = Math.max(tick, Math.pow(10, -decimalPrecision));
		double nextTick = Math.ceil(range.min/tick)*tick;
		int tickpos = (int) ((nextTick-range.min)*scaleFactor);
		int labelOffset = fm.getAscent()/2;
		while(tickpos-labelOffset > -freeArea.height) {
			String label = formatDouble(nextTick);
			int labelWidth = fm.stringWidth(label);
			// Paint this tick
			g.drawLine(-tickLength, tickpos, 0, tickpos);
			g.drawString(label, -labelWidth-beforeTickGap, tickpos+labelOffset);
			// Compute next tick
			nextTick += tick;
			tickpos = (int) ((nextTick-range.min)*scaleFactor);
			label = formatDouble(nextTick);
			labelWidth = fm.stringWidth(label);
		}
		return scaleFactor;
	}

}
