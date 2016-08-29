package gui.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import gui.chart.XAxis.Position;

import java.util.ArrayList;

/**
 * A stacked bar chart that draws bars horizontally with labels along the y-axis.
 * 
 * <p>The chart includes a numerical x-Axis.
 * 
 * @author Lasse Osterhagen
 *
 */
public class BarChart extends Chart {
	
	private int betweenBarGap;
	private int afterLabelGap;
	private double valueMax = 0;
	private XAxis xAxis = new XAxis(panel);
	private ArrayList<Map.Entry<String,double[]>> bars = new ArrayList<>();
	private int maxLabelWidth = 0;
	private Font labelAxisFont;
	private int inlayHeight;
	
	public BarChart() {
		xAxis.range.min = 0;
		xAxis.range.max = 0;
		setLabelAxisFont(xAxis.font); // init with x-axis font
		xAxis.position = Position.Top;
	}
	
	/**
	 * Set the font of the bar labels that appear on the left side along the y-axis.
	 * 
	 * <p> The font size also determines the height of the bars.
	 * 
	 * @param font the font of the bar labels
	 */
	public void setLabelAxisFont(Font font) {
		this.labelAxisFont = font;
		FontMetrics labelFontMetrics = panel.getFontMetrics(labelAxisFont);
		betweenBarGap = labelFontMetrics.getHeight()/2;
		afterLabelGap = labelFontMetrics.charWidth('k');
		inlayHeight = xAxis.getAxisOffset() + betweenBarGap +
				bars.size()*(labelFontMetrics.getHeight() + betweenBarGap);
	}
	
	/**
	 * Add a stacked bar with its associated label.
	 * 
	 * <p>The number of doubles passed as <code>sizes</code> determines the number
	 * of sections for this bar. Each section will be painted in another color. The
	 * sequence of colors is the same for each bar. The sequence of colors can be
	 * changed by calling {@link gui.chart.Chart#setColors(Color[]) setColors}.
	 * 
	 * <p> The length of the whole bar is the sum of all segments.
	 * 
	 * @param label the label for this bar. The label will be drawn to the left side
	 * of the bar.
	 * @param sizes the sizes of the bar segments.
	 */
	public void addBar(String label, double... sizes) {
		bars.add(new SimpleEntry<String, double[]>(label, sizes.clone()));
		// Determine bar size
		double barSize = 0;
		for(double val : sizes) {
			barSize += val;
		}
		valueMax = Math.max(valueMax, barSize);
		xAxis.range.max = valueMax+.05*valueMax; // 5 % longer than valueMax
		// Actualize max string width
		FontMetrics labelFontMetrics = panel.getFontMetrics(labelAxisFont);
		int labelWidth = labelFontMetrics.stringWidth(label);
		maxLabelWidth = Math.max(maxLabelWidth, labelWidth);
		// Actualize panel size
		inlayHeight += labelFontMetrics.getHeight() + betweenBarGap;
	}
	
	/**
	 * Set the number of decimal places of x-Axis' ticks
	 * @param decimalPrecision the number of decimal places
	 */
	public void setAxisDecimalPrecision(int decimalPrecision) {
		xAxis.decimalPrecision = decimalPrecision;
	}

	@Override
	protected void paintInlay(Graphics2D g, Dimension freeArea) {
		int xAxisOffset = xAxis.getAxisOffset();
		int labelAxisOffset = maxLabelWidth + afterLabelGap;
		freeArea.width -= labelAxisOffset;
		 // move to origin
		g.translate(labelAxisOffset, xAxisOffset);
		// paint x-axis
		double xScaleFactor = xAxis.paint(g, freeArea);
		
		FontMetrics fm = g.getFontMetrics(labelAxisFont);
		final int barHeight = fm.getHeight();
		g.setFont(labelAxisFont);
		int yPos = betweenBarGap;
		for(int i = 0; i < bars.size(); ++i) {
			Map.Entry<String, double[]> bar = bars.get(i);
			// Draw label
			g.setColor(Color.BLACK);
			String label = bar.getKey();
			g.drawString(label, -fm.stringWidth(label)-afterLabelGap, yPos+fm.getAscent());
			// Draw bars
			double[] values = bar.getValue();
			int barBeginX = 0;
			for(int j = 0; j<values.length; ++j) {
				g.setColor(colors[j%colors.length]);
				int rectLength = (int) Math.round(values[j]*xScaleFactor);
				g.fillRect(barBeginX, yPos, rectLength, barHeight);
				barBeginX += rectLength;
			};
			yPos += (barHeight+betweenBarGap);
		}
	}
	
	@Override
	Dimension getPrefSize() {
		int height = inlayHeight;
		if(title.isPresent())
			height += panel.getFontMetrics(title.get().font).getHeight();
		if(xAxisLabel.isPresent())
			height += Util.getLineSpace(panel.getFontMetrics(xAxisLabel.get().font));
		return new Dimension(0, height);
		
	}

}
