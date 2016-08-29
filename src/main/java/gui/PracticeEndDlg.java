package gui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import trainer.PerformanceRate;
import trainer.PracticeController;

import static gui.Util.getGUIText;


public class PracticeEndDlg extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private PracticeController pc;
	
	private JPanel getButtonPanel() {
		JButton buttonOk = new JButton("Ok");
		buttonOk.addActionListener(e -> dispose());
		//prevents accidentally closing by user
		buttonOk.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		JButton buttonStatistics = new JButton("Statistik");
		buttonStatistics.addActionListener(e ->
			new PracticeStatsDlg(PracticeEndDlg.this, pc.getExercise(), pc.getPerformanceStats()));
		JPanel panel = new JPanel();
		panel.add(buttonOk);
		panel.add(buttonStatistics);
		return panel;
	}
	
	PracticeEndDlg(Frame parent, PracticeController pc) {
		super(parent, "Ende der Übung", true);
		this.pc = pc;
	 	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	 	((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	 	
		//Build text display
	 	PerformanceRate totalPerformance = pc.getPerformanceStats().getTotalPerformanceRate();
		long requiredTime = pc.getRequiredTime();
		StringBuilder sb = new StringBuilder();
		sb.append(getGUIText("typedChars") + ": ");
		sb.append(totalPerformance.getHits());
		sb.append('\n');
		sb.append(getGUIText("errors") + ": ");
		sb.append(totalPerformance.getErrors());
		sb.append('\n');
		sb.append(getGUIText("errorRate") + ": ");
		sb.append(Util.rateLabel(totalPerformance.getErrorRate()));
		sb.append('\n');
		sb.append(getGUIText("requiredTime") + ": ");
		sb.append(Util.milli2TimeLabel(requiredTime));
		sb.append('\n');
		sb.append(getGUIText("strokesPerMin") + ": ");
		sb.append(Util.hitsPerMinLabel(totalPerformance.getHits(), requiredTime));
		JTextArea textArea = Util.makeLabelStyle(sb.toString());
		JPanel textPanel = new JPanel();
		textPanel.add(textArea);
		textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		add(textPanel, BorderLayout.PAGE_START);
		add(getButtonPanel(), BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
