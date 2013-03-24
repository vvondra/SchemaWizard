package cz.vojtechvondra.schemawizard.gui;

import cz.vojtechvondra.schemawizard.AttributeModel;

import javax.swing.*;
import java.awt.*;

/**
 * Hlavní okno pro práci se schématy
 */
public class WorkspaceFrame {

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

	public WorkspaceFrame() {
	}

	public void create() {

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
		return new WorkspaceToolbar(this);
	}

	protected void updateWindowTitle(String title) {
		window.setTitle("SchemaWizard - " + title);
	}

	protected void clearWorkspace() {
		currentModelName = "Untitled";
		currentModel = new AttributeModel();
		updateWindowTitle(currentModelName);
	}

	protected boolean confirmDiscardModifications() {
		if (currentModel != null) {
			int dialogResult = JOptionPane.showConfirmDialog(window, "Do you want to discard current changes?", "Warning", JOptionPane.YES_NO_OPTION);
			return dialogResult == JOptionPane.YES_OPTION;
		}

		return true;
	}


}
