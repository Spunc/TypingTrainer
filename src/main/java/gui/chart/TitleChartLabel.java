package gui.chart;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

class TitleChartLabel extends ChartLabel {

	public TitleChartLabel(JPanel panel, String label) {
		super(panel, label);
		font = new Font(Font.SANS_SERIF, Font.BOLD, 19);
	}

	public Dimension paint(Graphics2D g, Dimension freeArea) {
		FontMetrics fm = g.getFontMetrics(font);
		g.setFont(font);
		g.drawString(label, (panel.getWidth()-fm.stringWidth(label))/2, fm.getAscent());
		g.translate(0, fm.getHeight());
		freeArea.height -= fm.getHeight();
		return freeArea;
	}

}
