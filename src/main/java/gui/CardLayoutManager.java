package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * <p>Use radio buttons to click through different <code>JPanel</code>s that reside in a
 * {@link java.awt.CardLayout}.
 * 
 * <p>This class allows to change the currently displayed view by means of radio buttons. Views
 * in the form of {@link javax.swing.JPanel}s can be added to manager. For each added panel, an
 * additional button will be added to the <code>ButtonGroup</code> which is displayed under the
 * panel.
 * 
 * @author Lasse Osterhagen
 *
 */

public class CardLayoutManager implements ItemListener {
	private JPanel cards = new JPanel(new CardLayout());
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel buttonPanel = new JPanel();
	private JPanel viewPanel = new JPanel(new BorderLayout());
	
	/**
	 * Add an additional view (<code>JPanel</code>) to the Layout.
	 * @param panel the view to be added
	 * @param name the button name for the view
	 */
	public void addPanel(JPanel panel, String name) {
		JRadioButton button = new JRadioButton(name);
		button.addItemListener(this);
		if(buttonGroup.getButtonCount() == 0)	//select the first button
			button.setSelected(true);
		buttonGroup.add(button);
		buttonPanel.add(button);
		cards.add(panel, name);
	}
	
	public JPanel getViewPanel() {
		viewPanel.add(cards, BorderLayout.CENTER);
		viewPanel.add(buttonPanel, BorderLayout.PAGE_END);
		return viewPanel;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, ((JRadioButton) e.getItem()).getText());
	}
}
