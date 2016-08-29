package gui.chart;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

class XAxisLabel extends ChartLabel {

	public XAxisLabel(JPanel panel, String label) {
		super(panel, label);
		font = AXIS_FONT;
	}

	public Dimension paint(Graphics2D g, Dimension freeArea) {
		FontMetrics fm = g.getFontMetrics(font);
		g.setFont(font);
		g.drawString(label, (panel.getWidth()-fm.stringWidth(label))/2,
				freeArea.height-fm.getDescent());
		int xAxisLabelHeight = Util.getLineSpace(fm);
		freeArea.height -= xAxisLabelHeight;
		return freeArea;
	}

}
