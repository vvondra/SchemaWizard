package cz.vojtechvondra.schemawizard.gui;

import cz.vojtechvondra.schemawizard.AttributeModel;

import javax.swing.*;
import java.awt.*;

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
		window.setTitle("BCNF Decomposition");
		window.setSize(new Dimension(700, 300));
		window.setLocation(250, 250);
		window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.PAGE_AXIS));
		ImageIcon icon = new ImageIcon(getClass().getResource("icon.png"));
		window.setIconImage(icon.getImage());

		window.add(new JLabel("Select the dependency to be used to split each field not in BCNF.", SwingConstants.LEFT));
		JPanel rootPanel = new DecompositionTreeNode(model);
		JScrollPane scrollPane = new JScrollPane(rootPanel);
		window.add(scrollPane);

		window.setVisible(openWindow);
	}

	/**
	 * Creates a window through which BCNF relation decomposition is possible
	 * Opens window by default
	 * @param model
	 */
	public DecompositionFrame(AttributeModel model) {
		this(model, true);
	}


}
