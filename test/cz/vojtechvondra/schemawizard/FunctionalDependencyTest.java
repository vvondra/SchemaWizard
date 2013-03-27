package cz.vojtechvondra.schemawizard;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

public class FunctionalDependencyTest {
	@Test
	public void testAddToLeftSide() throws Exception {
		FunctionalDependency dep = new FunctionalDependency();
		dep.addToLeftSide(new Attribute("A"));
		assertEquals(dep.left.size(), 1);
	}

	@Test
	public void testAddToRightSide() throws Exception {
		FunctionalDependency dep = new FunctionalDependency();
		dep.addToRightSide(new Attribute("A"));
		assertEquals(dep.right.size(), 1);
	}

	@Test
	public void testToString() throws Exception {
		FunctionalDependency dep = new FunctionalDependency(
			new HashSet<Attribute>(Arrays.asList(new Attribute[] { new Attribute("A"), new Attribute("B")})),
			new HashSet<Attribute>(Arrays.asList(new Attribute[] { new Attribute("C"), new Attribute("D")}))
		);

		assertEquals("A B -> D C", dep.toString());
	}

	@Test
	public void testFromString() throws Exception {
		FunctionalDependency dep = FunctionalDependency.fromString("A B C -> D E F G");
		assertEquals(3, dep.left.size());
		assertEquals(dep.right.size(), 4);
	}
}
