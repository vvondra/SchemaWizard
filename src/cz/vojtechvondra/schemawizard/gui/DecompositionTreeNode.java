package cz.vojtechvondra.schemawizard.gui;

import cz.vojtechvondra.schemawizard.Attribute;
import cz.vojtechvondra.schemawizard.AttributeModel;
import cz.vojtechvondra.schemawizard.FunctionalDependency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Vector;

/**
 * Part of the BCNF decomposition tree
 * During the algorithm, we separate the relation into smaller nad smaller parts
 */
public class DecompositionTreeNode extends JPanel implements ActionListener {

	/**
	 * Nodes created by splitting this node
	 */
	Vector<DecompositionTreeNode> children = new Vector<DecompositionTreeNode>();

	/**
	 * Subpanels with parts of decomposed relation
	 */
	JPanel childrenPane;

	/**
	 * List of dependencies which can be used to decompose the relation
	 */
	JComboBox<FunctionalDependency> dependencySelect;

	JButton decomposeButton;

	/**
	 * Relation displayed in this node
	 */
	AttributeModel model;


	/**
	 * Creates a visual representation of the relation
	 * @param model relation to represent
	 */
	DecompositionTreeNode(AttributeModel model) {
		this.model = model;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel row = new JPanel(new FlowLayout());

		JTextField field = new JTextField();
		field.setEditable(false);
		field.setText(Attribute.hashSetToString(model.attributes));
		row.add(field);

		// Be able to decompose only if relation is not in BCNF
		if (!model.isInBCNF()) {
			HashSet<Attribute> key = model.findModelKey();

			Vector<FunctionalDependency> selectableDeps = new Vector<FunctionalDependency>();
			for (FunctionalDependency fd : model.deps) {
				if (!fd.left.containsAll(model.attributes)) {
					// Functional dependency breaks BCNF rules, left side is not a super key
					selectableDeps.add(fd);
				}
			}
			dependencySelect = new JComboBox<FunctionalDependency>(selectableDeps);
			JScrollPane pane = new JScrollPane(dependencySelect);
			row.add(pane);

			decomposeButton = new JButton("Decompose");
			decomposeButton.setActionCommand("DECOMPOSE");
			decomposeButton.addActionListener(this);
			row.add(decomposeButton);
		} else {
			row.add(new JLabel("Relation is in BCNF"));
		}

		add(row);
		add(new JSeparator());

		childrenPane = new JPanel(new FlowLayout());
		add(childrenPane);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == decomposeButton) {
			// Splits the relation into two using the selected dependency
			children.clear();
			FunctionalDependency dep = (FunctionalDependency) dependencySelect.getSelectedItem();
			HashSet<Attribute> leftRelationAttributes = new HashSet<Attribute>();
			for (Attribute a : dep.left) {
				leftRelationAttributes.add(a);
			}

			for (Attribute a : dep.right) {
				leftRelationAttributes.add(a);
			}

			HashSet<FunctionalDependency> leftRelationDeps = new HashSet<FunctionalDependency>();
			for (FunctionalDependency d : model.deps) {
				if (d.isValidOnAttributes(leftRelationAttributes)) {
					leftRelationDeps.add(d);
				}
			}

			AttributeModel leftModel = new AttributeModel(leftRelationAttributes, leftRelationDeps);
			children.add(new DecompositionTreeNode(leftModel));

			HashSet<Attribute> rightRelationAttributes = new HashSet<Attribute>(model.attributes);
			rightRelationAttributes.removeAll(dep.right);
			HashSet<FunctionalDependency> rightRelationDeps = new HashSet<FunctionalDependency>();
			for (FunctionalDependency d : model.deps) {
				if (d.isValidOnAttributes(rightRelationAttributes)) {
					rightRelationDeps.add(d);
				}
			}

			AttributeModel rightModel = new AttributeModel(rightRelationAttributes, rightRelationDeps);
			children.add(new DecompositionTreeNode(rightModel));
			refreshChildrenPane();
		}
	}

	/**
	 * Reconstructs the visual subtree containing separated relations
	 */
	protected void refreshChildrenPane() {
		remove(childrenPane);
		childrenPane = new JPanel(new FlowLayout());
		for (DecompositionTreeNode n : children) {
			childrenPane.add(n);
			childrenPane.add(new JSeparator());
			n.refreshChildrenPane();
		}
		add(childrenPane);
		revalidate();
		getParent().revalidate();
	}
}
