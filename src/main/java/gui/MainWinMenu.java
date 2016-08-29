package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import trainer.Exercise;

import static gui.Util.getGUIText;
import static gui.Util.getKeyCodeFromString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class MainWinMenu {
	
	private MainWindow mw;
	private JMenuBar menuBar = new JMenuBar();
	private MenuActionListener actionListener = new MenuActionListener();
	
	public MainWinMenu(MainWindow mw) {
		this.mw = mw;
		init();
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
	private class MenuActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(mw.conditionalStopPractice())
				switch (e.getActionCommand()) {
				case "select":
					Optional<Exercise> opEx = new SelectExerciseDlg(mw).showDialog();
					opEx.ifPresent(ex-> mw.setExercise(ex));
				}
		}
	}
	
	private void init() {
		JMenu menu;
		
		// First column
		menu = new JMenu(getGUIText("exercise"));
		menu.setMnemonic(getKeyCodeFromString(getGUIText("exerciseMnemonic")));
		menuBar.add(menu);
		
		addItem(menu, "select", "selectMnemonic");
	}
	
	private void addItem(JMenu menu, String commandKey, String mnemonicKey) {
	// commands should be keys in guiText.properties
		JMenuItem item = new JMenuItem(getGUIText(commandKey));
		item.setMnemonic(getKeyCodeFromString(getGUIText(mnemonicKey)));
		item.setActionCommand(commandKey);
		item.addActionListener(actionListener);
		menu.add(item);
	}

}
