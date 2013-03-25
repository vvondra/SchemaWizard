package cz.vojtechvondra.schemawizard;

import java.util.HashSet;
import java.util.Iterator;

/**
 * A collection of attributes and functional dependencies
 * Dependency rules may contain additional implied attributes
 *
 * Contains several algorithms for determining properties of a database schema
 */
public class AttributeModel {
	public HashSet<Attribute> attributes;
	public HashSet<FunctionalDependency> deps;

	public AttributeModel() {
		this.attributes = new HashSet<Attribute>();
		this.deps = new HashSet<FunctionalDependency>();
	}


	public AttributeModel(HashSet<Attribute> attributes, HashSet<FunctionalDependency> deps) {
		this.attributes = attributes;
		this.deps = deps;
	}

	/**
	 * Calculates transitive closure of selected attributes with regard to functional dependencies
	 * Returns attributes implied by rules but not included in attributes as well
	 * @return Transitive closure of base attribute set
	 */
	public HashSet<Attribute> findAttributeClosure() {
		HashSet<Attribute> closure = new HashSet<Attribute>(attributes);
		boolean done = false;

		while (!done) {
			done = true;
			for (FunctionalDependency dep : deps) {
				if (closure.containsAll(dep.left) && !closure.containsAll(dep.right)) {
					closure.addAll(dep.right);
					done = false;
				}
			}
		}

		return closure;
	}

	/**
	 * Checks if the specified dependency can be deduced from the closure of existing dependencies
	 * @param dep Dependecy to check membership in closure for
	 * @return true if dependency is part of closure
	 */
	public boolean isDependencyInClosure(FunctionalDependency dep) {
		AttributeModel temp = new AttributeModel(dep.left, deps);
		return temp.findAttributeClosure().containsAll(dep.right);
	}

	/**
	 * Checks if functional dependency is redundant
	 * @param dep Dependency to test
	 * @return true if dependency is redundant
	 */
	public boolean isDependencyRedundant(FunctionalDependency dep) {
		HashSet<FunctionalDependency> tempdeps = new HashSet<FunctionalDependency>(deps);
		tempdeps.remove(dep);
		AttributeModel temp = new AttributeModel(attributes, tempdeps);
		return temp.isDependencyInClosure(dep);

	}

	/**
	 * Tests if attribute is redundant in the left side of a dependency rule
	 * @param attrib Attribute to test
	 * @param dep Dependency to test
	 * @return true if attribute is redundant
	 */
	public boolean isAttributeRedundantInDependency(Attribute attrib, FunctionalDependency dep) {
		FunctionalDependency dep2 = new FunctionalDependency(new HashSet<Attribute>(dep.left), new HashSet<Attribute>(dep.right));
		dep2.left.remove(attrib);
		return isDependencyInClosure(dep2);
	}

	/**
	 * Removes all redundant attributes from the left side of a dependency rule
	 * @param dep Rule to reduce
	 * @return Copy of the rule with the reduced left side
	 */
	public FunctionalDependency getReducedDependency(FunctionalDependency dep) {
		FunctionalDependency dep2 = new FunctionalDependency(new HashSet<Attribute>(dep.left), new HashSet<Attribute>(dep.right));
		HashSet<Attribute> toRemove = new HashSet<Attribute>();
		for (Attribute a : dep2.left) {
			if (isAttributeRedundantInDependency(a, dep2)) {
				toRemove.add(a);
			}
		}

		dep2.left.removeAll(toRemove);

		return dep2;
	}

	/**
	 * Find a key for the model
	 * A key is a set of attributes which define all other attributes
	 *
	 * Works by creating a dependency from the full attribute set to the full attribute set and removing redundant attributes from the left side
	 * @return a set of attributes making a key
	 */
	public HashSet<Attribute> findModelKey() {
		FunctionalDependency dep = getReducedDependency(new FunctionalDependency(attributes, attributes));
		return dep.left;
	}

}
