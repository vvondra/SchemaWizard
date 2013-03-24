package cz.vojtechvondra.schemawizard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Application GUI toolbar
 */
public class SchemaWizardGUIToolbar extends JToolBar {

	/**
	 * Creates the default buttons and adds action listeners
	 * @param gui parent GUI
	 */
	public SchemaWizardGUIToolbar(final SchemaWizardGUI gui) {
		setFloatable(false);
		JButton newButton = new JButton("New");
		newButton.setToolTipText("Create new schema");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gui.confirmDiscardModifications()) {
					gui.clearWorkspace();
				}
			}
		});
		add(newButton);

		newButton = new JButton("Open");
		newButton.setToolTipText("Open schema from file");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(gui.window);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();

					try {
						gui.currentModel = AttributeModelExporter.loadModelFromFile(file.toPath());
						gui.currentModelName = file.getName();
						gui.updateWindowTitle(gui.currentModelName);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(new JFrame(), "Error during opening of attribute model file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		add(newButton);
	}
}
