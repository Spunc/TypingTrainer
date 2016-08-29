package gui.chart;

import java.awt.Font;

import javax.swing.JPanel;

public abstract class ChartLabel {
	
	static final Font AXIS_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
	
	JPanel panel;
	String label;
	Font font;
	
	public ChartLabel(JPanel panel, String label) {
		this.panel = panel;
		this.label = label;
	}

}
