package cz.vojtechvondra.schemawizard.gui;

import cz.vojtechvondra.schemawizard.FunctionalDependency;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * JList extension working with dependency lists from the AttributeModel
 */
public class DependencyList extends JList<FunctionalDependency> {

	public DependencyList(HashSet<FunctionalDependency> FunctionalDependencys) {
		setDependencies(FunctionalDependencys);
	}

	/**
	 * Sets the data model for this list from a set of dependencies
	 * @param FunctionalDependencies Dependencies to fill list with
	 */
	public void setDependencies(HashSet<FunctionalDependency> FunctionalDependencies) {
		FunctionalDependency[] as = new FunctionalDependency[FunctionalDependencies.size()];
		int i = 0;
		for (FunctionalDependency a : FunctionalDependencies) {
			as[i++] = a;
		}

		Arrays.sort(as);
		setListData(as);
	}
}
