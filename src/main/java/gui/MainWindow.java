package gui;

import static gui.Util.getGUIText;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import persistence.DbAccess;
import trainer.Exercise;
import trainer.LineMonitor;
import trainer.PracticeController;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Observer {

	private static final int MAX_LINE_LENGTH = 60;
	
	private JLabel practiceUnitLabel = new JLabel("keine gewählt", JLabel.RIGHT);
	private JLabel typedCharLabel = new JLabel("0", JLabel.RIGHT);
	private JLabel faultsLabel = new JLabel("0", JLabel.RIGHT);
	private JLabel timeLabel = new JLabel("00:00", JLabel.RIGHT);
	private JLabel faultRateLabel = new JLabel("0,00 %", JLabel.RIGHT);
	private JLabel typedByMinLabel = new JLabel("0", JLabel.RIGHT);
	private ColorLineDisplay line1 = new ColorLineDisplay(MAX_LINE_LENGTH);
	private LineDisplay line2 = new LineDisplay(MAX_LINE_LENGTH);
	private JButton startButton;
	private JButton stopButton;
	private KeyMonitor keyMonitor;
	private Timer timer = new Timer(1000, this::updateClock);
	
	private PracticeController pc;
	private Exercise exercise;
	
	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
		try {
			resetExercise();
		} catch (ImplementationNotFound e) {
			e.printStackTrace();
		}
		startButton.setEnabled(true);
		practiceUnitLabel.setText(Util.getExerciseNameText(exercise.getName(),
				exercise.getExerciseGroup().getId()));
	}
	
	/**
	 * Some operations (like changing the exercise) do not allow a running exercise.
	 * This function checks whether a practice is currently running. If so, it tries to stop
	 * the practice, which might involve asking the user for stopping it.
	 * @return true if there is no practice currently running.
	 */
	public boolean conditionalStopPractice() {
		if(pc == null) // PracticeController not initialized yet
			return true;
		PracticeController.State state = pc.getState();
		switch(state) {
		case INIT: case REG_STOPPED: case USER_STOPPED:
			return true;
		case READY:
			// In ready state, the user has not started the practice yet
			stopPractice();
			return true;
		default:
			return openUserStopPracticeDialog();
		}
	}
	
	/**
	 * Opens a dialog to let the user choose if he want so stop the ongoing practice.
	 * @return true if the user stopped the practice.
	 */
	private boolean openUserStopPracticeDialog() {
		switch(JOptionPane.showConfirmDialog(this, getGUIText("stopPracticeMsgDialogText"),
				getGUIText("stopMsgDialogTitle"), JOptionPane.YES_NO_OPTION)) {
		case JOptionPane.YES_OPTION:
			stopPractice();
			return true;
		default:
			line1.requestFocusInWindow(); //immediately focus on first line to allow continuing typing
			return false;
		}
	}
	
	// Reset PracticeController and KeyMonitor to new Exercise
	private void resetExercise() throws ImplementationNotFound {
		pc = new PracticeController(exercise, MAX_LINE_LENGTH);
		pc.addObserver(this);
		keyMonitor = new KeyMonitor();
	}
	
	private void resetLabels() {
		typedCharLabel.setText("0");
		faultsLabel.setText("0");
		timeLabel.setText("00:00");
		faultRateLabel.setText("0,00 %");
		typedByMinLabel.setText("0");
	}
	
	private void updateClock(ActionEvent e) {
		timeLabel.setText(Util.milli2TimeLabel(pc.getCurrentTime()));
		actualizeTypedByMinLabel();
	}
	
	private JPanel getLabelPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Übung:"), c);
		c.gridx = 1;
		panel.add(practiceUnitLabel, c);
		c.gridx = 2;
		c.weightx = 0.5;
		panel.add(Box.createHorizontalGlue(), c);
		c.gridx = 3;
		c.weightx = 0.2;
		panel.add(new JLabel("Fehler:"), c);
		c.gridx = 4;
		panel.add(faultsLabel, c);
		c.gridx = 5;
		c.weightx = 0.5;
		panel.add(Box.createHorizontalGlue(), c);
		c.gridx = 6;
		c.weightx = 0.2;
		panel.add(new JLabel("Zeit:"), c);
		c.gridx = 7;
		panel.add(timeLabel, c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("Getippte Zeichen:"), c);
		c.gridx = 1;
		panel.add(typedCharLabel, c);
		c.gridx = 2;
		c.weightx = 0.5;
		panel.add(Box.createHorizontalGlue(), c);
		c.gridx = 3;
		c.weightx = 0.2;
		panel.add(new JLabel("Fehlerquote:"), c);
		c.gridx = 4;
		panel.add(faultRateLabel, c);
		c.gridx = 5;
		c.weightx = 0.5;
		panel.add(Box.createHorizontalGlue(), c);
		c.gridx = 6;
		c.weightx = 0.2;
		panel.add(new JLabel("Anschläge/Min:"), c);
		c.gridx = 7;
		panel.add(typedByMinLabel, c);
		return panel;
	}
	
	private JPanel getLineDisplayPanel() {
		JPanel panel = new JPanel();
		panel.add(line1);
		panel.add(line2);
		return panel;
	}
	
	private void stopPractice() {
		stopButton.setEnabled(false);
		pc.userStop();
	}
	
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		startButton = new JButton("Start");
		startButton.addActionListener(evt -> {
			startButton.setEnabled(false);
			if(pc.getState() == PracticeController.State.REG_STOPPED ||
					pc.getState() == PracticeController.State.USER_STOPPED) {
				// Reset to current exercise
				try {
					resetExercise();
				} catch (ImplementationNotFound e) {
					// This should not be possible, because when State.STOPPED has been reached,
					// the exercise already has been initialized successfully.
					e.printStackTrace();
				}
			}
			resetLabels();
			pc.ready();
		});
		startButton.setEnabled(false);
		stopButton = new JButton("Stop");
		stopButton.addActionListener(e -> conditionalStopPractice());
		stopButton.setEnabled(false);
		panel.add(startButton);
		panel.add(stopButton);
		return panel;
	}
	
	public MainWindow() {
		super("Schreibtrainer");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(conditionalStopPractice()) {
					DbAccess.getInstance().disconnect();
					System.exit(0);
				}
			}	
		});
		setPreferredSize(new Dimension(750, 350));
		setJMenuBar(new MainWinMenu(this).getMenuBar());
		add(getLabelPanel(), BorderLayout.PAGE_START);
		add(getLineDisplayPanel(), BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private class KeyMonitor extends KeyAdapter {
		private LineMonitor lm = pc.getLineMonitor();
		@Override
		public void keyTyped(KeyEvent e) {
			//start timer at first key press
			if(pc.getState() == PracticeController.State.READY) {
				pc.run();
			}
			char c = e.getKeyChar();
			//correct typed char
			if(lm.advanceIfCorrect(c)) {
				line1.setHighlighted(lm.getPosition());
				typedCharLabel.setText(Integer.toString(
						pc.getPerformanceStats().getTotalPerformanceRate().getHits()));
				actualizeTypedByMinLabel();
			}
			//wrong typed char
			else {
				line1.signalError();
				faultsLabel.setText(Integer.toString(
						pc.getPerformanceStats().getTotalPerformanceRate().getErrors()));
			}
			faultRateLabel.setText(Util.rateLabel(
					pc.getPerformanceStats().getTotalPerformanceRate().getErrorRate()));
		}
	}
	
	private void actualizeTypedByMinLabel() {
		typedByMinLabel.setText(Util.hitsPerMinLabel(
				pc.getPerformanceStats().getTotalPerformanceRate().getHits(),
				pc.getCurrentTime()));
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg == PracticeController.Event.NEW_STATE) {
			PracticeController.State state = pc.getState();
			switch(state) {
			case READY:
				stopButton.setEnabled(true);
				line1.addKeyListener(keyMonitor);
				line1.requestFocusInWindow();
				break;
			case RUNNING:
				timer.start();
				break;
			case REG_STOPPED: case USER_STOPPED:
				line1.removeKeyListener(keyMonitor);
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
				timer.stop();
				break;
			default:
				break;
			}
			if(state == PracticeController.State.REG_STOPPED)
				// Call invokeLater to let the GUI be updated before the modal
				// PracticeEndDlg window shows up.
				SwingUtilities.invokeLater(() -> new PracticeEndDlg(this, pc));
		}
		else if(arg == PracticeController.Event.LINE_CHANGE) {
			line1.setTextLine(pc.getLine1());
			line2.setTextLine(pc.getLine2());
		}
	}
}
