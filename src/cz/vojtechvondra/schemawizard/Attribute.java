package cz.vojtechvondra.schemawizard;

/**
 * Data attribute
 * Can represent a column in a database later on
 */
public class Attribute {

	/**
	 * Display name of attribute
	 */
	String name;

	public Attribute(String name) {
		this.name = name.replaceAll("\\s","");
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Attribute attribute = (Attribute) o;

		if (name != null ? !name.equals(attribute.name) : attribute.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
