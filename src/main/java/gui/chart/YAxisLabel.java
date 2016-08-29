package gui.chart;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

class YAxisLabel extends ChartLabel {

	public YAxisLabel(JPanel panel, String label) {
		super(panel, label);
		font = AXIS_FONT;
	}

	public Dimension paint(Graphics2D g, Dimension freeArea) {
		FontMetrics fm = g.getFontMetrics(font);
		g.setFont(font);
		AffineTransform oldTransform = g.getTransform();
	    AffineTransform newTransform = new AffineTransform();
	    newTransform.setToRotation(-Math.PI *.5);
	    g.setTransform(newTransform);
	    g.drawString(label, (-fm.stringWidth(label)-panel.getHeight())/2, fm.getAscent());
	    g.setTransform(oldTransform);
	    g.translate(Util.getLineSpace(fm), 0);
	    freeArea.width -= Util.getLineSpace(fm);
		return freeArea;
	}

}
