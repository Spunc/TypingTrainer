package gui;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gui.chart.LineChart;


public class Man_LineChartTest {
	
	public void initAndShowGUI() {
		LineChart l = new LineChart();
		l.addPoints(new Point.Double(1, 0));
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(400, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(l.getPanel());
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater( () -> new Man_LineChartTest().initAndShowGUI());
	}

}
