package gui.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Optional;

import javax.swing.JPanel;

public abstract class Chart {
	
	JPanel panel = new ChartPanel();
	Optional<TitleChartLabel> title = Optional.empty();
	Optional<XAxisLabel> xAxisLabel = Optional.empty();
	Optional<YAxisLabel> yAxisLabel = Optional.empty();
	Color[] colors = {Color.RED, Color.GREEN, Color.BLUE,
			Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE};
	
	/**
	 * Paint the inner chart. That is everything except title, and axis labels.
	 * The cursor of the graphics context should point to the origin.
	 * @param g the graphics context
	 * @param freeArea the free painting area available
	 */
	abstract void paintInlay(Graphics2D g, Dimension freeArea);
	
	/**
	 * Get the panel with the drawn chart.
	 * @return panel with chart
	 */
	public JPanel getPanel() {
		return panel;
	}
	
	/**
	 * Set title of the chart.
	 * @param title the title string
	 */
	public void setTitle(String title) {
		this.title = Optional.ofNullable(new TitleChartLabel(panel, title));
	}
	
	/**
	 * Set the x-axis label.
	 * @param label the label string
	 */
	public void setXAxisLabel(String label) {
		this.xAxisLabel = Optional.ofNullable(new XAxisLabel(panel, label));
	}
	
	/**
	 * Set the y-axis label.
	 * @param label the label string
	 */
	public void setYAxisLabel(String label) {
		this.yAxisLabel = Optional.ofNullable(new YAxisLabel(panel, label));
	}
	
	/**
	 * Set the color to be used for the series. The order at which the series are
	 * added to the chart determines their color: series and color position match.
	 * If more series are added to the chart then the size of colors, colors will
	 * repeat.
	 * @param colors an ordered array of colors.
	 */
	public void setColors(Color[] colors) {
		this.colors = colors;
	}
	
	Dimension getPrefSize() {
		return null;
	}
	
	@SuppressWarnings("serial")
	private class ChartPanel extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			final Dimension size = panel.getSize();
			Dimension freeArea = new Dimension(size);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			if(title.isPresent())
				freeArea = title.get().paint(g2, freeArea);
			if(xAxisLabel.isPresent())
				freeArea = xAxisLabel.get().paint(g2, freeArea);
			if(yAxisLabel.isPresent())
				freeArea = yAxisLabel.get().paint(g2, freeArea);
			paintInlay(g2, freeArea);
		}
		
		@Override
		public Dimension getPreferredSize() {
			return getPrefSize();
		}
		
	}

}
