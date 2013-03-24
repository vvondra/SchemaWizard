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

	public void setDependencies(HashSet<FunctionalDependency> FunctionalDependencys) {
		FunctionalDependency[] as = new FunctionalDependency[FunctionalDependencys.size()];
		int i = 0;
		for (FunctionalDependency a : FunctionalDependencys) {
			as[i++] = a;
		}

		Arrays.sort(as);
		setListData(as);
	}
}
