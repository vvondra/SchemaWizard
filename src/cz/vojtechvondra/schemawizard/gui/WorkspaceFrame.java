package cz.vojtechvondra.schemawizard.gui;

import cz.vojtechvondra.schemawizard.Attribute;
import cz.vojtechvondra.schemawizard.AttributeModel;
import cz.vojtechvondra.schemawizard.FunctionalDependency;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

/**
 * Main window work working with schemas
 */
public class WorkspaceFrame implements ActionListener {

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

	/**
	 * Visual list of attributes
	 */
	AttributeList attrList;

	/**
	 * Visual list of dependencies
	 */
	DependencyList depList;

	public WorkspaceFrame() {
	}


	/**
	 * Creates the main application window and displays it
	 */
	public void create() {

		window = new JFrame("SchemaWizard");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container frameContent = window.getContentPane();

		frameContent.setLayout(new BorderLayout());
		frameContent.add(createToolbar(), BorderLayout.NORTH);

		JPanel attributePanel = new JPanel();
		attributePanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		attributePanel.setLayout(new BorderLayout());
		attrList = new AttributeList(new HashSet<Attribute>());
		JScrollPane attrListPane = new JScrollPane(attrList);
		attributePanel.add(new JLabel("Attributes:", SwingConstants.LEFT), BorderLayout.NORTH);
		attributePanel.add(attrListPane, BorderLayout.CENTER);
		attributePanel.add(createDataEntryPanel(), BorderLayout.SOUTH);
		frameContent.add(attributePanel, BorderLayout.WEST);

		JPanel depPanel = new JPanel();
		depPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		depPanel.setLayout(new BorderLayout());
		depList = new DependencyList(new HashSet<FunctionalDependency>());
		JScrollPane depListPane = new JScrollPane(depList);
		depPanel.add(new JLabel("Dependencies:", SwingConstants.LEFT), BorderLayout.NORTH);
		depPanel.add(depListPane, BorderLayout.CENTER);
		depPanel.add(createDependencyEditPanel(), BorderLayout.SOUTH);

		frameContent.add(depPanel, BorderLayout.EAST);

		frameContent.add(createToolPanel(), BorderLayout.SOUTH);

		window.setSize(600, 400);
		window.setLocation(200, 200);
		window.setVisible(true);

		clearWorkspace();
	}


	/**
	 * Creates a panel for a List which can add and remove items
	 */
	public JPanel createDataEntryPanel() {
		// Create a panel to hold all other components
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());

		// Create some function buttons
		JButton addButton = new JButton("Add");
		dataPanel.add(addButton, BorderLayout.WEST);
		addButton.addActionListener(this);

		JButton removeButton = new JButton("Delete");
		dataPanel.add(removeButton, BorderLayout.EAST);
		removeButton.addActionListener(this);

		// Create a text field for data entry and display
		JTextField dataField = new JTextField();
		dataPanel.add(dataField, BorderLayout.CENTER);
		addButton.putClientProperty("field", dataField);
		return dataPanel;
	}

	protected JPanel createDependencyEditPanel() {
		// Create a panel to hold all other components
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());

		// Create some function buttons
		JButton addButton = new JButton("Add dependency");
		dataPanel.add(addButton, BorderLayout.WEST);
		addButton.addActionListener(this);

		JButton removeButton = new JButton("Delete");
		removeButton.setActionCommand("Delete dependency");
		dataPanel.add(removeButton, BorderLayout.EAST);
		removeButton.addActionListener(this);
		return dataPanel;
	}

	protected JPanel createToolPanel() {
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(new FlowLayout());

		toolPanel.add(new JButton("Redundant attributes"));
		toolPanel.add(new JButton("Redundant dependencies"));
		toolPanel.add(new JButton("Detect keys"));

		return toolPanel;
	}

	/**
	 * Prepares the main application toolbar
	 *
	 * @return Initialized toolbar
	 */
	protected JToolBar createToolbar() {
		return new WorkspaceToolbar(this);
	}

	/**
	 * Sets the window title accordingly
	 *
	 * @param title Additional title text
	 */
	protected void updateWindowTitle(String title) {
		window.setTitle("SchemaWizard - " + title);
	}

	/**
	 * Updates GUI with a new model
	 *
	 * @param model model to be used
	 * @param name  Name of them model
	 */
	protected void setActiveModel(AttributeModel model, String name) {
		currentModel = model;
		currentModelName = name;
		refreshWorkspace();
		updateWindowTitle(currentModelName);

	}

	protected void refreshWorkspace() {
		attrList.setAttributes(currentModel.attributes);
		depList.setDependencies(currentModel.deps);
	}

	/**
	 * Creates an empty workspace with an empty model
	 */
	protected void clearWorkspace() {
		setActiveModel(new AttributeModel(), "Untitled");
	}

	/**
	 * Display a yes/no dialog if user wants to discard his workspace
	 *
	 * @return true if user confirmed discarding
	 */
	protected boolean confirmDiscardModifications() {
		if (currentModel != null) {
			int dialogResult = JOptionPane.showConfirmDialog(window, "Do you want to discard current changes?", "Warning", JOptionPane.YES_NO_OPTION);
			return dialogResult == JOptionPane.YES_OPTION;
		}

		return true;
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent source = (JComponent) event.getSource();
		if (event.getActionCommand().equals("Add")) {
			// Get the text field value
			JTextField dataField = (JTextField) source.getClientProperty("field");
			String stringValue = dataField.getText();
			dataField.setText("");

			// Add this item to the list and refresh
			if (stringValue != null && stringValue.length() > 0) {
				currentModel.attributes.add(new Attribute(stringValue));
				refreshWorkspace();
			}
		}

		if (event.getActionCommand().equals("Delete")) {
			// Get the current selection
			int selection = attrList.getSelectedIndex();
			if (selection >= 0) {
				currentModel.attributes.remove(attrList.getSelectedValue());
				refreshWorkspace();
			}
		}

		if (event.getActionCommand().equals("Delete dependency")) {
			// Get the current selection
			int selection = depList.getSelectedIndex();
			if (selection >= 0) {
				currentModel.deps.remove(depList.getSelectedValue());
				refreshWorkspace();
			}
		}

		if (event.getActionCommand().equals("Add dependency")) {
			JDialog d = new AddDependencyDialog(this);
			d.setVisible(true);
		}
	}

}
