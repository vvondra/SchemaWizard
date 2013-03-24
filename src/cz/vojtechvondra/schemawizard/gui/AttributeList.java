package cz.vojtechvondra.schemawizard.gui;

import cz.vojtechvondra.schemawizard.Attribute;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * JList extension working with attribute lists from the AttributeModel
 */
public class AttributeList extends JList<Attribute> {

	public AttributeList(HashSet<Attribute> attributes) {
		setAttributes(attributes);
	}

	public void setAttributes(HashSet<Attribute> attributes) {
		Attribute[] as = new Attribute[attributes.size()];
		int i = 0;
		for (Attribute a : attributes) {
			as[i++] = a;
		}
		Arrays.sort(as);
		setListData(as);
	}
}
