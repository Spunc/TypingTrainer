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
import java.util.function.Supplier;

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
			switch (e.getActionCommand()) {
			case "select":
				if(mw.conditionalStopPractice()) {
					Optional<Exercise> opEx = new SelectExerciseDlg(mw).showDialog();
					opEx.ifPresent(ex-> mw.setExercise(ex));
				}
				break;
			case "showKeyboard":
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				if(item.isSelected())
					System.out.println("Selected");
					// insert mw.showKeyboard here
			}
		}
	}
	
	private void init() {
		JMenu menu;
		
		// First column
		menu = new JMenu(getGUIText("exercise"));
		menu.setMnemonic(getKeyCodeFromString(getGUIText("exerciseMnemonic")));
		menuBar.add(menu);
		addItem(menu, JMenuItem::new, "select", "selectMnemonic");
		
		// Second column
		menu = new JMenu(getGUIText("keyboard"));
		menu.setMnemonic(getKeyCodeFromString(getGUIText("keyboardMnemonic")));
		menuBar.add(menu);
		addItem(menu, JCheckBoxMenuItem::new, "showKeyboard", "showKeyboardMnemonic");
	}
	
	
	/**
	 * Adds a <code>JMenuItem</code> to a <code>JMenu</code> (which is a column of a
	 * <code>JMenuBar</code>
	 * @param menu the menu column to which the item should be added
	 * @param sp Supplier for the kind of JMenuItem to be created
	 * @param commandKey fulfills two tasks: First, it is the key for I18n of the item text
	 * (<i>guiText.properties</i>); second, it serves as key in the switch statement of the
	 * <code>MenuActionListener</code>.
	 * @param mnemonicKey key for I18n of the mnemonic
	 */
	private <T extends JMenuItem>
		void addItem(JMenu menu, Supplier<T> sp, String commandKey, String mnemonicKey) {
		T item = sp.get();
		item.setText(getGUIText(commandKey));
		item.setMnemonic(getKeyCodeFromString(getGUIText(mnemonicKey)));
		item.setActionCommand(commandKey);
		item.addActionListener(actionListener);
		menu.add(item);
	}

}
