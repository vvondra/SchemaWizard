package cz.vojtechvondra.schemawizard;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;


public class AttributeModelExporterTest {

	@Test
	public void testExportModel() throws Exception {
		Attribute a = new Attribute("A");
		Attribute b = new Attribute("B");
		Attribute c = new Attribute("C");
		FunctionalDependency fa = new FunctionalDependency();
		FunctionalDependency fb = new FunctionalDependency();

		fa.addToLeftSide(a);
		fa.addToLeftSide(b);
		fa.addToRightSide(c);

		fb.addToLeftSide(b);
		fb.addToRightSide(c);

		HashSet<Attribute> attributes = new HashSet<Attribute>();
		attributes.add(a);
		attributes.add(b);
		attributes.add(c);

		HashSet<FunctionalDependency> deps = new HashSet<FunctionalDependency>();
		deps.add(fa);
		deps.add(fb);

		AttributeModel schema = new AttributeModel(attributes, deps);

		AttributeModelExporter exporter =  new AttributeModelExporter(schema);
		String dump = exporter.exportModel();
		assertTrue(dump.contains("A\n"));
		assertTrue(dump.contains("B\n"));
		assertTrue(dump.contains("C\n"));
		assertTrue(dump.contains("=====\n"));
		assertTrue(dump.contains("A B -> C\n"));
	}

	@Test
	public void testCreateModel() throws Exception {
		String dump = "A\nB\nC\n=====\nA B -> C";
		AttributeModel am = AttributeModelExporter.loadModelFromString(dump);
		assertEquals(3, am.attributes.size());
		assertEquals(1, am.deps.size());
		assertEquals(2, am.deps.iterator().next().left.size());
		assertEquals(1, am.deps.iterator().next().right.size());
	}
}
