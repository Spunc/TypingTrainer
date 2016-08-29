package gui.chart;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * A classic line chart that draws series of dots onto a two-dimensional plane.
 * Optionally, adjacent dots can be connect by lines.
 * 
 * <p>The chart includes a numerical x-axis and a numerical y-axis. 
 * 
 * @author Lasse Osterhagen
 *
 */

public class LineChart extends Chart {
	
	private int pointSize = 7;
	private boolean lines = true;
	private Range xValueRange = new Range();
	private Range yValueRange = new Range();
	private XAxis xAxis = new XAxis(panel);
	private YAxis yAxis = new YAxis(panel);
	private Deque<ArrayList<Point2D>> points = new ArrayDeque<>();
	
	/**
	 * Add a series of points to the chart.
	 * 
	 * <p>All points that were added at the same time will represent a series and
	 * will therefore be painted in the same color.
	 * 
	 * @param points the series of points to be added
	 */
	public void addPoints(Point2D ... points) {
		ArrayList<Point2D> ps = new ArrayList<>();
		for(Point2D p : points) {
			ps.add(p);
			xValueRange.min = Math.min(xValueRange.min, p.getX());
			xValueRange.max = Math.max(xValueRange.max, p.getX());
			yValueRange.min = Math.min(yValueRange.min, p.getY());
			yValueRange.max = Math.max(yValueRange.max, p.getY());
		}
		this.points.addLast(ps);
		
		// increase axis ranges
		double lengthOffset = computeAxisOffset(xValueRange);
		xAxis.range.min = xValueRange.min - lengthOffset;
		xAxis.range.max = xValueRange.max + lengthOffset;
		lengthOffset = computeAxisOffset(yValueRange);
		yAxis.range.min = yValueRange.min - lengthOffset;
		yAxis.range.max = yValueRange.max + lengthOffset;
	}
	
	/**
	 * The axis range should be a bit longer than the value range. Use this function to
	 * compute an offset that should be added to both sides of the value range.
	 * @param r the Range for which an offset should be computed
	 * @return the offset as absolute value
	 */
	private double computeAxisOffset(Range r) {
		if(r.getLength() > 1.0e-8) {
			return r.getLength() * .05;
		}
		return Math.abs(r.max) < 1.0e-8 ? 0.1 : r.max * .5;
	}
	
	/**
	 * Set the size of the graphical representation of a point.
	 * @param size the point size in pixels
	 */
	public void setPointSize(int size) {
		pointSize = size;
	}
	
	/**
	 * Set whether lines should be drawn between adjacent points within a series.
	 * @param lines true if lines should be drawn, otherwise false
	 */
	public void setLines(boolean lines) {
		this.lines = lines;
	}
	
	/**
	 * Set the number of decimal places of x-Axis' ticks
	 * @param decimalPrecision the number of decimal places
	 */
	public void setXAxisDecimalPrecision(int decimalPrecision) {
		xAxis.decimalPrecision = decimalPrecision;
	}
	
	/**
	 * Set the number of decimal places of y-Axis' ticks
	 * @param decimalPrecision the number of decimal places
	 */
	public void setYAxisDecimalPrecision(int decimalPrecision) {
		yAxis.decimalPrecision = decimalPrecision;
	}

	@Override
	void paintInlay(Graphics2D g, Dimension freeArea) {
		int xAxisOffset = xAxis.getAxisOffset();
		int yAxisOffset = yAxis.getAxisOffset();
		freeArea.width -= yAxisOffset;
		freeArea.height -= xAxisOffset;
		// move to origin of axes
		g.translate(yAxisOffset, freeArea.height);
		// paint axes
		double xScaleFactor = xAxis.paint(g, freeArea);
		double yScaleFactor = yAxis.paint(g, freeArea);
		// paint points
		int colorIndex = 0;
		for(ArrayList<Point2D> series : points) {
			Point2D lastPoint = null;
			g.setColor(colors[colorIndex%colors.length]);
			for(Point2D p : series) {
				// Draw points
				g.fillOval(
						(int) Math.round((p.getX()-xAxis.range.min)*xScaleFactor - 0.5*pointSize),
						(int) Math.round((p.getY()-yAxis.range.min)*yScaleFactor - 0.5*pointSize),
						pointSize, pointSize);
				// Draw lines between points
				if(!lines) continue;
				if(lastPoint != null)
					g.drawLine(
						(int) Math.round((lastPoint.getX()-xAxis.range.min)*xScaleFactor),
						(int) Math.round((lastPoint.getY()-yAxis.range.min)*yScaleFactor),
						(int) Math.round((p.getX()-xAxis.range.min)*xScaleFactor),
						(int) Math.round((p.getY()-yAxis.range.min)*yScaleFactor)
						);
				lastPoint = p;
			}
			++colorIndex;
		}
	}

}
