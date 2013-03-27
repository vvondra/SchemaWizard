package cz.vojtechvondra.schemawizard;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;


public class AttributeModelTest {
	@Test
	 public void testFindAttributeClosure() throws Exception {
		//F = {a -> b, bc -> d, bd -> a}
		Attribute a = new Attribute("A");
		Attribute b = new Attribute("B");
		Attribute c = new Attribute("C");
		Attribute d = new Attribute("D");
		FunctionalDependency fa = new FunctionalDependency();
		FunctionalDependency fb = new FunctionalDependency();
		FunctionalDependency fc = new FunctionalDependency();

		fa.addToLeftSide(a);
		fa.addToRightSide(b);

		fb.addToLeftSide(b);
		fb.addToLeftSide(c);
		fb.addToRightSide(d);

		fc.addToLeftSide(b);
		fc.addToLeftSide(d);
		fc.addToRightSide(a);

		HashSet<Attribute> attributes = new HashSet<Attribute>();
		attributes.add(b);
		attributes.add(c);

		HashSet<FunctionalDependency> deps = new HashSet<FunctionalDependency>();
		deps.add(fa);
		deps.add(fb);
		deps.add(fc);

		AttributeModel schema = new AttributeModel(attributes, deps);
		HashSet<Attribute> closure = schema.findAttributeClosure();

		assertEquals(4, closure.size());
		assertTrue(closure.containsAll(Arrays.asList(new Attribute[]{a, b, c, d})));
	}

	@Test
	public void testDependencyClosureMembership() throws Exception {
		Attribute a = new Attribute("A");
		Attribute b = new Attribute("B");
		Attribute c = new Attribute("C");
		Attribute d = new Attribute("D");
		FunctionalDependency fa = new FunctionalDependency();
		FunctionalDependency fb = new FunctionalDependency();
		FunctionalDependency fc = new FunctionalDependency();
		FunctionalDependency fd = new FunctionalDependency();
		fa.addToLeftSide(a);
		fa.addToRightSide(b);

		fb.addToLeftSide(b);
		fb.addToRightSide(c);

		fc.addToLeftSide(a);
		fc.addToRightSide(c);

		fd.addToLeftSide(a);
		fd.addToRightSide(d);

		HashSet<Attribute> attributes = new HashSet<Attribute>();
		attributes.add(a);
		attributes.add(b);
		attributes.add(c);

		HashSet<FunctionalDependency> deps = new HashSet<FunctionalDependency>();
		deps.add(fa);
		deps.add(fb);

		AttributeModel schema = new AttributeModel(attributes, deps);
		assertTrue(schema.isDependencyInClosure(fa));
		assertTrue(schema.isDependencyInClosure(fb));
		assertTrue(schema.isDependencyInClosure(fc));
		assertFalse(schema.isDependencyInClosure(fd));

	}

	@Test
	public void testDependencyRedundant() throws Exception {
		Attribute a = new Attribute("A");
		Attribute b = new Attribute("B");
		Attribute c = new Attribute("C");
		Attribute d = new Attribute("D");
		FunctionalDependency fa = new FunctionalDependency();
		FunctionalDependency fb = new FunctionalDependency();
		FunctionalDependency fc = new FunctionalDependency();
		FunctionalDependency fd = new FunctionalDependency();
		fa.addToLeftSide(a);
		fa.addToRightSide(b);

		fb.addToLeftSide(b);
		fb.addToRightSide(c);

		fc.addToLeftSide(a);
		fc.addToRightSide(c);

		fd.addToLeftSide(a);
		fd.addToRightSide(d);

		HashSet<Attribute> attributes = new HashSet<Attribute>();
		attributes.add(a);
		attributes.add(b);
		attributes.add(c);

		HashSet<FunctionalDependency> deps = new HashSet<FunctionalDependency>();
		deps.add(fa);
		deps.add(fb);
		deps.add(fc);
		deps.add(fd);

		AttributeModel schema = new AttributeModel(attributes, deps);
		assertFalse(schema.isDependencyRedundant(fa));
		assertFalse(schema.isDependencyRedundant(fb));
		assertTrue(schema.isDependencyRedundant(fc));
		assertFalse(schema.isDependencyRedundant(fd));

	}

	@Test
	public void testRedundantAttribute() throws Exception {
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

		assertTrue(schema.isAttributeRedundantInDependency(a, fa));
		assertTrue(fa.left.contains(a));
	}

	@Test
	public void testGetReducedDependency() throws Exception {
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

		FunctionalDependency reduced = schema.getReducedDependency(fa);
		assertEquals(reduced.left.size(), 1);
	}

	@Test
	public void testFindAllKeys() throws Exception {
		Attribute c = new Attribute("C");
		Attribute s = new Attribute("S");
		Attribute j = new Attribute("J");
		Attribute q = new Attribute("Q");
		Attribute d = new Attribute("D");
		Attribute p = new Attribute("P");
		Attribute v = new Attribute("V");
		FunctionalDependency fa = FunctionalDependency.fromString("C -> S J Q D P V");
		FunctionalDependency fb = FunctionalDependency.fromString("S D -> P");
		FunctionalDependency fc = FunctionalDependency.fromString("P -> D");
		FunctionalDependency fd = FunctionalDependency.fromString("J P -> C");
		FunctionalDependency fe = FunctionalDependency.fromString("J -> S");

		HashSet<Attribute> attributes = new HashSet<Attribute>();
		attributes.addAll(Arrays.asList(c,s, j, q, d,p,v));
		HashSet<FunctionalDependency> deps = new HashSet<FunctionalDependency>();
		deps.addAll(Arrays.asList(fa, fb, fc, fd, fe));

		AttributeModel schema = new AttributeModel(attributes, deps);

		Vector<HashSet<Attribute>> keys = schema.findAllModelKeys();
		assertEquals(3, keys.size());
	}

	@Test
	public void testFindUniversalRuleKey() throws Exception {
		Attribute c = new Attribute("C");
		Attribute s = new Attribute("S");
		Attribute j = new Attribute("J");
		Attribute q = new Attribute("Q");
		Attribute d = new Attribute("D");
		Attribute p = new Attribute("P");
		Attribute v = new Attribute("V");
		FunctionalDependency fa = FunctionalDependency.fromString("C -> S J Q D P V");
		FunctionalDependency fb = FunctionalDependency.fromString("S D -> P");
		FunctionalDependency fc = FunctionalDependency.fromString("P -> D");
		FunctionalDependency fd = FunctionalDependency.fromString("J P -> C");
		FunctionalDependency fe = FunctionalDependency.fromString("J -> S");

		HashSet<Attribute> attributes = new HashSet<Attribute>();
		attributes.addAll(Arrays.asList(c,s, j, q, d,p,v));
		HashSet<FunctionalDependency> deps = new HashSet<FunctionalDependency>();
		deps.addAll(Arrays.asList(fa, fb, fc, fd, fe));


		AttributeModel schema = new AttributeModel(attributes, deps);

		HashSet<Attribute> key = schema.findModelKey();
		assertEquals(1, key.size());
	}

}
