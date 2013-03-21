package cz.vojtechvondra.schemawizard;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
	@Test
	public void testToString() throws Exception {
		Attribute a = new Attribute("Name With\t Spaces");
		assertEquals("NameWithSpaces", a.toString());
	}
}
