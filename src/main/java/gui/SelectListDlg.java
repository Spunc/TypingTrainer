package gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import static gui.Util.getGUIText;

/**
 * A {@link javax.swing.JDialog} that allows the user to select a single element from a list
 * of elements.
 * 
 * @author Lasse Osterhagen
 *
 * @param <T> the type of the elements
 */
public class SelectListDlg<T> {
	
	private T[] list;
	private JDialog dialog;
	private int selectedIndex = -1;
	
	/**
	 * Show the dialog.
	 * @return the selected item or empty, if nothing was selected
	 */
	public Optional<T> show() {
		dialog.setVisible(true);
		if(selectedIndex < 0)
			return Optional.empty();
		else
			return Optional.of(list[selectedIndex]);
	}
	
	/**
	 * Create a modal dialog.
	 * @param parent parent of the dialog
	 * @param title title of the dialog window
	 * @param list elements to be displayed
	 */
	public SelectListDlg(Frame parent, String title, T[] list) {
		this(parent, title, list, Object::toString);
	}
	
	/**
	 * Create a modal dialog.
	 * @param parent parent of the dialog
	 * @param title title of the dialog window
	 * @param list the elements to be displayed
	 * @param displayFun a function that transforms the elements into a String for displaying
	 */
	public SelectListDlg(Frame parent, String title, T[] list, Function<T, String> displayFun) {
		this.list = list;
		String[] strList = Arrays.stream(list).map(displayFun).toArray(size -> new String[size]);
		JList<String> jlist = new JList<>(strList);
		dialog = new JDialog(parent, title, true);
		dialog.add(new JScrollPane(jlist, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		
		// Create buttons
		JPanel buttonPanel = new JPanel();
		JButton button = new JButton(getGUIText("ok"));
		button.addActionListener(e-> {
			selectedIndex = jlist.getSelectedIndex();
			dialog.dispose();});
		buttonPanel.add(button);
		button = new JButton(getGUIText("cancel"));
		button.addActionListener(e-> dialog.dispose());
		buttonPanel.add(button);
		
		dialog.add(buttonPanel, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
	}

}
