package gui;

import static gui.Util.getGUIText;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import gui.chart.*;
import persistence.Session;
import persistence.SessionPersist;
import trainer.Exercise;
import trainer.PerformanceRate;
import trainer.PerformanceStats;

@SuppressWarnings("serial")
public class PracticeStatsDlg extends JDialog {
	
	private PerformanceStats performanceStats;
	private ArrayList<Session> sessions;
	
	public PracticeStatsDlg(Dialog parent, Exercise exercise,
			PerformanceStats performanceStats) {
		super(parent, getGUIText("exerciseStats"), true);
		this.performanceStats = performanceStats;
		sessions = new SessionPersist().getSessions(exercise.getId());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(400, 400));
		
		// --- Create chart panels ---
		JTabbedPane tabbedPane = new JTabbedPane();
		// Character chart
		JPanel charDiagramPanel = getBarChartPanel();
		JScrollPane charScrollpane = new JScrollPane(charDiagramPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tabbedPane.add(getGUIText("chars"), charScrollpane);
		// Sessions charts
		if(sessions.size() > 0) {
			// Only add the session charts if there are any entries in sessions
			CardLayoutManager manager = new CardLayoutManager();
			manager.addPanel(getErrorRateChart().getPanel(), getGUIText("errorRate"));
			manager.addPanel(getCharPerMinChart().getPanel(), getGUIText("strokesPerMin"));
			tabbedPane.add(getGUIText("sessions"), manager.getViewPanel());
		}
		add(tabbedPane, BorderLayout.CENTER);
		
		// --- Add button ---
		JButton okButton = new JButton(getGUIText("ok"));
		okButton.addActionListener(e -> dispose());
		add(okButton, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	/**
	 * Get chart panel that shows red/green bars for errors/hits for every typed character.
	 * @return bar chart panel
	 */
	private JPanel getBarChartPanel() {
		// first sort by errors (max->min), then by char (a->z)
		Comparator<Entry<Character,PerformanceRate>> comp = (e1, e2) -> {
			int errorComp = e2.getValue().getErrors() - e1.getValue().getErrors();
			return errorComp != 0 ? errorComp : e1.getKey() - e2.getKey();
		};
		TreeSet<Entry<Character,PerformanceRate>> sortedSet = new TreeSet<>(comp);
		sortedSet.addAll(performanceStats.getHits_errors());
		BarChart chart = new BarChart();
		chart.setTitle(getGUIText("errors_hits"));
		chart.setAxisDecimalPrecision(0);
		Character c;
		String label;
		double[] miss_hit = new double[2];
		for(Entry<Character,PerformanceRate> e : sortedSet) {
			c = e.getKey();
			switch(c) {
			case ' ':
				label = "Space";
				break;
			case '\n':
				label = "Enter";
				break;
			default:
				label = c.toString();
			}
			miss_hit[0] = e.getValue().getErrors();
			miss_hit[1] = e.getValue().getHits();
			chart.addBar(label, miss_hit);
		}
		return chart.getPanel();
	}
	
	private Chart getErrorRateChart() {
		LineChart lc = new LineChart();
		lc.setTitle(getGUIText("progress"));
		lc.setYAxisLabel("%");
		lc.setXAxisLabel(getGUIText("sessionNo"));
		int n = sessions.size();
		Point2D[] points = new Point2D[n];
		for(int i=0; i<n; ++i) {
			points[i] = new Point2D.Double(i+1,
					sessions.get(n-i-1).getPerformanceRate().getErrorRate()*100);
		}
		lc.addPoints(points);
		lc.setXAxisDecimalPrecision(0);
		lc.setYAxisDecimalPrecision(2);
		return lc;
	}
	
	private Chart getCharPerMinChart() {
		LineChart lc = new LineChart();
		lc.setTitle(getGUIText("progress"));
		lc.setYAxisLabel(getGUIText("strokesPerMin"));
		lc.setXAxisLabel(getGUIText("sessionNo"));
		int n = sessions.size();
		Point2D[] points = new Point2D[n];
		for(int i=0; i<n; ++i) {
			points[i] = new Point2D.Double(i+1,
					sessions.get(n-i-1).getPerformanceRate().getHits()/
					(sessions.get(n-i-1).getRequiredTime()/60_000.0));
		}
		lc.addPoints(points);
		lc.setXAxisDecimalPrecision(0);
		lc.setYAxisDecimalPrecision(0);
		return lc;
	}
	
	

}
