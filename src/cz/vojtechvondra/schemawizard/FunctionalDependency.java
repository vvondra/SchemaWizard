package cz.vojtechvondra.schemawizard;

import java.util.HashSet;

/**
 * Dependency rule
 * Attributes on the left side of the rule fully specify all the attributes on the right side
 */
public class FunctionalDependency implements Comparable<FunctionalDependency> {

	/**
	 * Left side of dependency rule
	 */
	public HashSet<Attribute> left;

	/**
	 * Right side of dependency rule
	 */
	public HashSet<Attribute> right;

	/**
	 * Creates empty functional dependency
	 */
	public FunctionalDependency() {
		this.left = new HashSet<Attribute>();
		this.right = new HashSet<Attribute>();
	}

	public FunctionalDependency(HashSet<Attribute> left, HashSet<Attribute> right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Adds attribute to left side of the dependency rule
	 * @param a Added attribute
	 */
	public void addToLeftSide(Attribute a) {
		left.add(a);
	}

	/**
	 * Adds attribute to right side of the dependency rule
	 * @param a Added attribute
	 */
	public void addToRightSide(Attribute a) {
		right.add(a);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Attribute a : left) {
			sb.append(a.toString()).append(" ");
		}

		sb.append("-> ");

		for (Attribute a : right) {
			sb.append(a.toString()).append(" ");
		}

		// Delete last space
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	@Override
	public int compareTo(FunctionalDependency o) {
		return toString().compareTo(o.toString());
	}

	public static FunctionalDependency fromString(String s) {
		String[] parts = s.split("\\s->\\s");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid functional dependency notation.");
		}
		HashSet<Attribute> left = new HashSet<Attribute>();
		HashSet<Attribute> right = new HashSet<Attribute>();
		String[] as = parts[0].split("\\s");
		for (String a : as) {
			left.add(new Attribute(a));
		}

		as = parts[1].split("\\s");
		for (String a : as) {
			right.add(new Attribute(a));
		}

		return new FunctionalDependency(left, right);
	}
}
