package gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import trainer.Exercise;
import trainer.lineCreators.LineCreatorFactory;
import trainer.lineCreators.LineCreatorProvider;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;

import static gui.Util.getGUIText;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SelectExerciseDlg {
	
	private JDialog dialog;
	private MatrixComboBoxModel mcb;
	private List<Object[]> tableContent;
	private MatrixTableModel mtbl;
	private JTable table;
	private Optional<Exercise> exercise = Optional.empty();
	
	public Optional<Exercise> showDialog() {
		dialog.setVisible(true);
		return exercise;
	}
	
	public SelectExerciseDlg(Frame parent) {
		tableContent = persistence.Util.fillTableFromSelect(
				"SELECT id, groupId, name, param, lineCreatorType FROM exercises");
		tableContent.stream()
			.forEach( k -> {
				// I18N exercise name
				k[2] = gui.Util.getExerciseNameText((String) k[2], (int) k[1]);
				LineCreatorProvider lcp = null;
				try {
					lcp = LineCreatorFactory.getLineCreatorProvider((String) k[4]);
					// display short version of param
					k[3] = lcp.shortParam((String) k[3]);
					// display description for LineCreator
					k[4] = lcp.description();
				} catch (ImplementationNotFound e) {
					// do replace column values
				}
				});
		
		// Create MatrixComboBox
		persistence.Util.ObjectTransformer[] obTr = new persistence.Util.ObjectTransformer[2];
		obTr[1] = v -> gui.Util.getExerciseGroupText((String) v);
		List<Object[]> rows = persistence.Util.fillTableFromSelect(
				"SELECT id, name FROM exerciseGroups", obTr);
		mcb = new MatrixComboBoxModel(rows, 1);
		
		// Create MatrixTable
		mtbl = new MatrixTableModel(new ArrayList<Object[]>(0), 2, 5, new String[]
				{getGUIText("name"), getGUIText("param"), getGUIText("lineCreatorType")});
		
		// Create dialog
		dialog = new JDialog(parent, getGUIText("selectExerciseTitle"), true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		((JComponent) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(getComboBox());
		mainPanel.add(getTableScrollPane());
		dialog.add(mainPanel, BorderLayout.CENTER);
		dialog.add(getButtonPanel(), BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
	}
	
	private JComboBox<Object> getComboBox() {
		JComboBox<Object> comboBox = new JComboBox<Object>(new String[] {
				getGUIText("selectExerciseGroup")
		});
		Font old = comboBox.getFont();
		comboBox.setFont(new Font(old.getName(), Font.ITALIC, old.getSize()));
		comboBox.setMaximumRowCount(16);
		comboBox.addPopupMenuListener(new PopupMenuListener() {
			private boolean firstUse = true;
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				if(firstUse) {
					comboBox.setModel(mcb.getComboModel());
					comboBox.setFont(old);
					firstUse = false;
				}
			}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}
		});
		comboBox.addActionListener(e ->
			displayFilteredExercises((int) mcb.getValue(comboBox.getSelectedIndex(), 0)));
		return comboBox;
	}
	
	private JScrollPane getTableScrollPane() {
		table = new JTable(mtbl.getTableModel());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(700, 200));
		return scrollPane;
	}
	
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		JButton buttonSelect = new JButton(getGUIText("select"));
		buttonSelect.addActionListener(e-> {
			int index = table.getSelectedRow();
			if(index >= 0) {
				exercise = Optional.of(
						persistence.ExerciseCRUD.loadExercise((int) mtbl.getValue(index, 0)));
				dialog.dispose();
			}
		});
		panel.add(buttonSelect);
		JButton buttonCancel = new JButton(getGUIText("cancel"));
		buttonCancel.addActionListener(e-> dialog.dispose());
		panel.add(buttonCancel);
		return panel;
	}
	
	private void displayFilteredExercises(int exerciseGroupId) {
		mtbl.setRows(
				tableContent.stream()
				.filter(k -> (int) k[1] == exerciseGroupId)
				.collect(Collectors.toList())
				);
	}
}
