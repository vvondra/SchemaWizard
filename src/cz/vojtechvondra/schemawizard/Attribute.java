package cz.vojtechvondra.schemawizard;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Data attribute
 * Can represent a column in a database later on
 */
public class Attribute implements Comparable<Attribute> {

	/**
	 * Display name of attribute
	 */
	String name;

	/**
	 * Create attribute with specified name
	 * @param name Name of atttribute
	 */
	public Attribute(String name) {
		name = name.replaceAll("\\s","");
		if (!name.matches("[A-Za-z0-9]+")) {
			throw new IllegalArgumentException("Invalid attribute name " + name + ", it can contain only letters or numbers");
		}

		this.name = name;
	}

	/**
	 * Helper function which sorts and pretty prints a set of attributes
	 * Attributes are printed separated by a space
	 * @param hs attributes to be printed
	 * @return String representation of attribute set
	 */
	public static String hashSetToString(HashSet<Attribute> hs) {
		StringBuilder keyText = new StringBuilder();
		Attribute[] as = new Attribute[hs.size()];
		int i = 0;
		for (Attribute a : hs) {
			as[i++] = a;
		}

		Arrays.sort(as);
		for (Attribute a : as) {
			keyText.append(a).append(" ");
		}

		return keyText.toString();
	}

	protected Attribute() {}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Attribute attribute = (Attribute) o;

		return !(name != null ? !name.equals(attribute.name) : attribute.name != null);

	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public int compareTo(Attribute o) {
		return name.compareTo(o.name);
	}
}
