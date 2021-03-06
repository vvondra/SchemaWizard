package cz.vojtechvondra.schemawizard;

import javax.naming.directory.AttributeInUseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

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
		HashSet<Attribute> leftSide = new HashSet<Attribute>(dep2.left);
		for (Attribute a : dep2.left) {
			if (isAttributeRedundantInDependency(a, new FunctionalDependency(leftSide, dep2.right)) && !toRemove.contains(a)) {
				toRemove.add(a);
				leftSide.remove(a);
			}
		}

		dep2.left.removeAll(toRemove);

		return dep2;
	}

	/**
	 * Find a key (random) for the model
	 * A key is a set of attributes which define all other attributes
	 *
	 * Works by creating a dependency from the full attribute set to the full attribute set and removing redundant attributes from the left side
	 * @return a set of attributes making a key
	 */
	public HashSet<Attribute> findModelKey() {
		FunctionalDependency dep = getReducedDependency(new FunctionalDependency(attributes, attributes));
		return dep.left;
	}

	/**
	 * Find all the keys for the model
	 * A key is a set of attributes which define all other attributes
	 *
	 * Works by creating a dependency from the full attribute set to the full attribute set and removing redundant attributes from the left side
	 * @return a set of attributes making a key
	 */
	public Vector<HashSet<Attribute>> findAllModelKeys() {
		Vector<HashSet<Attribute>> keys = new Vector<HashSet<Attribute>>();

		HashSet<Attribute> key = findModelKey();
		keys.add(key);
		boolean done = false;
		while (!done) {
			done = true;
			for (FunctionalDependency dep : deps) {
				HashSet<Attribute> rightSideCopy = new HashSet<Attribute>(dep.right);
				rightSideCopy.retainAll(key); // Remove the last key from the right side of the rule and see if something stays
				HashSet<Attribute> rest = new HashSet<Attribute>(key);
				rest.addAll(dep.left);
				rest.removeAll(dep.right);
				if (!rightSideCopy.isEmpty()) {
					boolean availableKey = true;
					for (HashSet<Attribute> k : keys) {
						if (rest.containsAll(k)) {
							availableKey = false;
						}
					}
					if (availableKey) {
						AttributeModel tmp = new AttributeModel(new HashSet<Attribute>(rest), new HashSet<FunctionalDependency>(deps));
						FunctionalDependency dp = new FunctionalDependency(rest, attributes);
						FunctionalDependency r = tmp.getReducedDependency(dp);
						keys.add(r.left);
						key = r.left;
						done = false;
					}

				}
			}
		}

		return keys;
	}

	/**
	 * Checks whether relation is in Boyce-Codd Normal Form
	 * @return true if in BCNF
	 */
	public boolean isInBCNF() {
		Vector<HashSet<Attribute>> keys = findAllModelKeys();
		// To be in BCNF, every left side of a dependency rule must be a key!
		for (FunctionalDependency d : deps) {
			boolean isKey = false;
			for (HashSet<Attribute> key : keys) {
				if (d.left.containsAll(key)) {
					isKey = true;
					break;
				}
			}
			if (!isKey) {
				return false;
			}
		}
		return true;
	}

}
