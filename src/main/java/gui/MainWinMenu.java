package gui;

import javax.swing.JCheckBoxMenuItem;
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
		public void actionPerformed(ActionEvent event) {
			switch (event.getActionCommand()) {
			case "select":
				if(mw.conditionalStopPractice()) {
					Optional<Exercise> opEx = new SelectExerciseDlg(mw).showDialog();
					opEx.ifPresent(ex-> mw.setExercise(ex));
				}
				break;
			case "showKeyboard":
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) event.getSource();
				if(item.isSelected()) {
					String[] layouts = gui.keyboard.Keyboard.getAvailableLayouts();
					Optional<String> selection =
							new SelectListDlg<String>(
									mw,
									getGUIText("selectKeyboardLayout"),
									layouts,
									e -> Util.getKeyboardLayoutText(e)).show();
					if(selection.isPresent())
						mw.addKeyboard(selection.get());
					else
						item.setSelected(false);
				}
				else
					mw.removeKeyboard();
			}
		}
	}
	
	private void init() {
		JMenu menu;
		
		// First column
		menu = new JMenu(getGUIText("exercise"));
		menu.setMnemonic(getKeyCodeFromString(getGUIText("exerciseMnemonic")));
		menuBar.add(menu);
		addItem(menu, new JMenuItem(), "select", "selectMnemonic");
		
		// Second column
		menu = new JMenu(getGUIText("keyboard"));
		menu.setMnemonic(getKeyCodeFromString(getGUIText("keyboardMnemonic")));
		menuBar.add(menu);
		addItem(menu, new JCheckBoxMenuItem(), "showKeyboard", "showKeyboardMnemonic");
	}
	
	
	/**
	 * Adds a <code>JMenuItem</code> to a <code>JMenu</code> (which is a column of a
	 * <code>JMenuBar</code>).
	 * @param menu the menu column to which the item should be added
	 * @param item the JMenuItem that should be added
	 * @param commandKey fulfills two tasks: First, it is the key for I18n of the item text
	 * (<i>guiText.properties</i>); second, it serves as key in the switch statement of the
	 * <code>MenuActionListener</code>.
	 * @param mnemonicKey key for I18n of the mnemonic
	 */
	private void addItem(JMenu menu, JMenuItem item, String commandKey, String mnemonicKey) {
		item.setText(getGUIText(commandKey));
		item.setMnemonic(getKeyCodeFromString(getGUIText(mnemonicKey)));
		item.setActionCommand(commandKey);
		item.addActionListener(actionListener);
		menu.add(item);
	}

}
