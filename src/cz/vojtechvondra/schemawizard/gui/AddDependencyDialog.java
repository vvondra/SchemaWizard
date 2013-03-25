package cz.vojtechvondra.schemawizard.gui;

import cz.vojtechvondra.schemawizard.Attribute;
import cz.vojtechvondra.schemawizard.EmptyAttribute;
import cz.vojtechvondra.schemawizard.FunctionalDependency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Window dialog for adding a new dependency
 */
public class AddDependencyDialog extends JDialog {

	/**
	 * Parent GUI
	 */
	WorkspaceFrame gui;

	JButton buttonOK;

	JButton buttonCancel;

	/**
	 * Individual combo boxes for left and right side attributes
	 */
	ArrayList<JComboBox<Attribute>> boxes = new ArrayList<JComboBox<Attribute>>(10);

	/**
	 * Creates and displays dialog window for adding dependencies
	 * @param gui Parent window, main application GUI
	 */
	public AddDependencyDialog(final WorkspaceFrame gui) {
		this.gui = gui;
		setTitle("Add dependency");
		setSize(300, 300);
		setVisible(true);
		setLayout(new BorderLayout());

		buttonOK = new JButton("Add");
		buttonCancel = new JButton("Cancel");

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(buttonOK);
		buttonPanel.add(buttonCancel);
		add(createComboBoxes(), BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
	}

	/**
	 * Prepares combo boxes with attributes as values and adds them to the layout and box map
	 * @return JPanel with ComboBoxes
	 */
	private JPanel createComboBoxes() {
		JPanel boxPanel = new JPanel(new GridLayout(5, 2));
		Attribute[] ats = new Attribute[gui.currentModel.attributes.size() + 1];
		ats[0] = new EmptyAttribute();
		int i = 1;
		for (Attribute a : gui.currentModel.attributes) {
			ats[i++] = a;
		}
		Arrays.sort(ats);
		for (i = 0; i < 5; i++) {
			for (int j = 0; j < 2; j++) {
				JComboBox<Attribute> box = new JComboBox<Attribute>(ats);
				boxes.add(box);
				boxPanel.add(box);
			}
		}
		return boxPanel;
	}

	/**
	 * Check filled out boxes and add a dependency if a valid one is specified
	 */
	private void onOK() {
		HashSet<Attribute> left = new HashSet<Attribute>();
		HashSet<Attribute> right = new HashSet<Attribute>();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 2; j++) {
				Attribute e = (Attribute) boxes.get(i * 2 + j).getSelectedItem();
				if (!(e instanceof EmptyAttribute)) {
					if (j == 0) {
						left.add(e);
					} else {
						right.add(e);
					}
				}
			}
		}

		// remove all attributes which would define themselves
		//right.removeAll(left);

		if (left.size() > 0 && right.size() > 0) {
			gui.currentModel.deps.add(new FunctionalDependency(left, right));
			gui.refreshWorkspace();
		}
// add your code here
		dispose();
	}

	/**
	 * Cancel the window, throw everything away
	 */
	private void onCancel() {
// add your code here if necessary
		dispose();
	}
}
