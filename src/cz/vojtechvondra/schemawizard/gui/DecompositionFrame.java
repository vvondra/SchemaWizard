package cz.vojtechvondra.schemawizard.gui;

import cz.vojtechvondra.schemawizard.AttributeModel;

import javax.swing.*;

/**
 * Interface for BCNF decomposition
 */
public class DecompositionFrame {

	/**
	 * Swing window
	 */
	JFrame window;

	/**
	 * Creates a window through which BCNF schema decomposition is possible
	 * @param model The schema to work on
	 */
	public DecompositionFrame(AttributeModel model, boolean openWindow) {
		window = new JFrame();

		// Do not close application when closing this window
		window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		window.setSize(600, 400);
		window.setLocation(250, 250);
		window.setVisible(openWindow);
	}

	/**
	 * Creates a window through which BCNF schema decomposition is possible
	 * Opens window by default
	 * @param model
	 */
	public DecompositionFrame(AttributeModel model) {
		this(model, true);
	}

}
