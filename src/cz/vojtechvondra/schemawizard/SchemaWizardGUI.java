package cz.vojtechvondra.schemawizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Hlavní okno pro práci se schématy
 */
public class SchemaWizardGUI {

	/**
	 * Active window
	 */
	JFrame window;

	/**
	 * Name of the current active model
	 */
	String currentModelName;

	/**
	 * Active model in the workspace
	 */
	AttributeModel currentModel;

	public SchemaWizardGUI() {
	}

	protected void create() {

		window = new JFrame("SchemaWizard");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container frameContent = window.getContentPane();

		frameContent.setLayout(new BorderLayout());
				frameContent.add(createToolbar(), BorderLayout.NORTH);
		window.setSize(500,400);
		window.setLocation(200, 200);
		window.setVisible(true);

		clearWorkspace();
	}

	protected JToolBar createToolbar() {
		JToolBar bar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
		bar.setFloatable(false);
		JButton newButton = new JButton("New");
		newButton.setToolTipText("Create new schema");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearWorkspace();
			}
		});
		bar.add(newButton);

		newButton = new JButton("Open");
		newButton.setToolTipText("Open schema from file");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(window);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();

					try {
						currentModel = AttributeModelExporter.loadModelFromFile(file.toPath());
						currentModelName = file.getName();
						updateWindowTitle(currentModelName);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(new JFrame(), "Error during opening of attribute model file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		bar.add(newButton);
		return bar;
	}

	protected void updateWindowTitle(String title) {
		window.setTitle("SchemaWizard - " + title);
	}

	protected void clearWorkspace() {
		currentModelName = "Untitled";
		currentModel = new AttributeModel();
		updateWindowTitle(currentModelName);
	}


}
