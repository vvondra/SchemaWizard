package cz.vojtechvondra.schemawizard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Handles importing and exporting attribute models from plaintext dumps
 */
public class AttributeModelExporter {

	/**
	 * Attribute and rule separator in dump
	 */
	final static protected String dumpSeparator = "=====";

	AttributeModel model;

	public AttributeModelExporter(AttributeModel model) {
		this.model = model;
	}

	/**
	 * Creates a plaintext dump of the attribute model
	 * @return Plaintext dump
	 */
	public String exportModel() {
		StringBuilder sb = new StringBuilder();
		for (Attribute a : model.attributes) {
			sb.append(a);
			sb.append("\n");
		}
		sb.append(dumpSeparator).append("\n");
		for (FunctionalDependency b : model.deps) {
			sb.append(b);
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * Saves attribute model in a text file
	 * @param path path to file
	 * @throws FileNotFoundException
	 */
	public void exportToFile(Path path) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(path.toString());
		pw.print(exportModel());
		pw.close();
	}

	/**
	 * Creates model from plaintext dump
	 * @param dump Dumped model in text
	 * @return Reconstructed model with attributes and dependencies
	 */
	public static AttributeModel loadModelFromString(String dump) {
		String[] lines = dump.split("\n");

		HashSet<Attribute> attribs = new HashSet<Attribute>();
		HashSet<FunctionalDependency> deps = new HashSet<FunctionalDependency>();
		boolean inAttribs = true;
		for (String line : lines) {
			line = line.replace("\r", "");
			if (line.equals(dumpSeparator)) {
				inAttribs = false;
				continue;
			}

			if (inAttribs) {
				attribs.add(new Attribute(line));
			} else {
				String[] parts = line.split("\\s->\\s");
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
				deps.add(new FunctionalDependency(left, right));
			}
		}

		return new AttributeModel(attribs, deps);
	}

	/**
	 * Creates model from plaintext dump stored in a file
	 * @param path Path to dumped model
	 * @return Reconstructed model with attributes and dependencies
	 */
	public static AttributeModel loadModelFromFile(Path path) throws IOException {
		Scanner sc = new Scanner(path);
		String content = sc.useDelimiter("\\Z").next();
		if (sc.ioException() != null) {
			throw new IllegalArgumentException("Invalid file specified for model import");
		}
		sc.close();
		return loadModelFromString(content);
	}
}
