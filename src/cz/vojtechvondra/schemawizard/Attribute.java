package cz.vojtechvondra.schemawizard;

/**
 * Data attribute
 * Can represent a column in a database later on
 */
public class Attribute implements Comparable<Attribute> {

	/**
	 * Display name of attribute
	 */
	String name;

	public Attribute(String name) {
		this.name = name.replaceAll("\\s","");
		if (!name.matches("[A-Za-z0-9]+")) {
			throw new IllegalArgumentException("Invalid attribute name, it can contain only letters or numbers");
		}
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
