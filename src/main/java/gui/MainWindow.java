package gui;

import static gui.Util.getGUIText;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import gui.keyboard.Keyboard;
import persistence.DbAccess;
import trainer.Exercise;
import trainer.KeyTypedEvent;
import trainer.LineMonitor;
import trainer.PracticeController;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;
import trainer.lineCreators.InitException;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Observer {
	
	private static final int MAX_LINE_LENGTH = 60;
	
	private JLabel practiceUnitLabel = new JLabel("keine gewählt", JLabel.RIGHT);
	private JLabel typedCharLabel = new JLabel("0", JLabel.RIGHT);
	private JLabel faultsLabel = new JLabel("0", JLabel.RIGHT);
	private JLabel timeLabel = new JLabel("00:00", JLabel.RIGHT);
	private JLabel faultRateLabel = new JLabel("0,00 %", JLabel.RIGHT);
	private JLabel typedByMinLabel = new JLabel("0", JLabel.RIGHT);
	private JPanel middlePanel;
	private ColorLineDisplay line1 = new ColorLineDisplay(MAX_LINE_LENGTH);
	private LineDisplay line2 = new LineDisplay(MAX_LINE_LENGTH);
	private Optional<Keyboard> keyboard = Optional.empty();
	private JPanel keyboardPanel;
	private JButton startButton;
	private JButton stopButton;
	private LineMonitor lineMonitor;
	private Timer timer = new Timer(1000, this::updateClock);
	
	private PracticeController pc;
	private Exercise exercise;
	
	void setExercise(Exercise exercise) {
		this.exercise = exercise;
//		try {
//			resetExercise();
//		} catch (ImplementationNotFound e) {
//			e.printStackTrace();
//		}
		startButton.setEnabled(true);
		practiceUnitLabel.setText(Util.getExerciseNameText(exercise.getName(),
				exercise.getExerciseGroup().getId()));
	}
	
	void addKeyboard(String layout) {
		keyboard = Optional.of(new Keyboard(layout));
		keyboardPanel = new JPanel();
		keyboardPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		keyboardPanel.add(keyboard.get().getKeyboardComponent());
		middlePanel.add(keyboardPanel);
		pack();
		if( pc != null && (pc.getState() == PracticeController.State.READY ||
				pc.getState() == PracticeController.State.RUNNING) )
			registerKeyboard();
	}
	
	private void registerKeyboard() {
		keyboard.ifPresent(k -> {
			k.setLineMonitor(lineMonitor);
			lineMonitor.addObserver(k);
		});
	}
	
	private void deregisterKeyboard() {
		keyboard.ifPresent(k -> {
			if(pc != null) {
				// pc is null, if no exercise has been loaded
				lineMonitor.deleteObserver(k);
				k.removeLineMonitor();
			}
		});
	}
	
	void removeKeyboard() {
		deregisterKeyboard();
		middlePanel.remove(keyboardPanel);
		keyboard = Optional.empty();
		pack();
	}
	
	/**
	 * Some operations (like changing the exercise) do not allow a running exercise.
	 * This function checks whether a practice is currently running. If so, it tries to stop
	 * the practice, which might involve asking the user for stopping it.
	 * @return true if there is no practice currently running.
	 */
	boolean conditionalStopPractice() {
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
	private void resetExercise() throws ImplementationNotFound, InitException {
		pc = new PracticeController(exercise, MAX_LINE_LENGTH);
		pc.addObserver(this);
		lineMonitor = pc.getLineMonitor();
		lineMonitor.addObserver(this);
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
	
	private void stopPractice() {
		stopButton.setEnabled(false);
		pc.userStop();
	}
	
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		startButton = new JButton("Start");
		startButton.addActionListener(evt -> {
			startButton.setEnabled(false);
			try {
				resetExercise();
				resetLabels();
				pc.ready();
			}
			catch (ImplementationNotFound | InitException e) {
				// Insert error message for user here
				startButton.setEnabled(true);
				e.printStackTrace();
			}
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
		setJMenuBar(new MainWinMenu(this).getMenuBar());
		add(getLabelPanel(), BorderLayout.PAGE_START);
		middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
		middlePanel.add(Util.wrapInEmtpyBorder(line1, 5, 5, 1, 5));
		middlePanel.add(Util.wrapInEmtpyBorder(line2, 1, 5, 5, 5));
		add(middlePanel, BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void actualizeTypedByMinLabel() {
		typedByMinLabel.setText(Util.hitsPerMinLabel(
				pc.getPerformanceStats().getTotalPerformanceRate().getHits(),
				pc.getCurrentTime()));
	}

	@Override
	public void update(Observable o, Object arg) {

		// React on events of KeyTypedMonitor
		if(o instanceof LineMonitor) { // change to true reference
			KeyTypedEvent kte = (KeyTypedEvent) arg;
			if(kte.correct) {
				line1.setHighlighted(lineMonitor.getPosition());
				typedCharLabel.setText(Integer.toString(
						pc.getPerformanceStats().getTotalPerformanceRate().getHits()));
				actualizeTypedByMinLabel();
			}
			else {
				line1.signalError();
				faultsLabel.setText(Integer.toString(
						pc.getPerformanceStats().getTotalPerformanceRate().getErrors()));
			}
			faultRateLabel.setText(Util.rateLabel(
					pc.getPerformanceStats().getTotalPerformanceRate().getErrorRate()));
		}
		
		// React on events of PracticeController
		if(o instanceof PracticeController) {
			if(arg == PracticeController.Event.STATE_CHANGED) {
				PracticeController.State state = pc.getState();
				switch(state) {
				case INIT:
					break;
				case READY:
					stopButton.setEnabled(true);
					line1.addKeyListener(lineMonitor);
					line1.requestFocusInWindow();
					registerKeyboard();
					break;
				case RUNNING:
					timer.start();
					break;
				case REG_STOPPED: case USER_STOPPED:
					line1.removeKeyListener(lineMonitor);
					stopButton.setEnabled(false);
					startButton.setEnabled(true);
					timer.stop();
					deregisterKeyboard();
					break;
				}
				if(state == PracticeController.State.REG_STOPPED)
					// Call invokeLater to let the GUI be updated before the modal
					// PracticeEndDlg window shows up.
					SwingUtilities.invokeLater(() -> new PracticeEndDlg(this, pc));
			}
			else if(arg == PracticeController.Event.NEW_LINE) {
				line1.setTextLine(pc.getLine1());
				line2.setTextLine(pc.getLine2());
			}
		}
	}
}
