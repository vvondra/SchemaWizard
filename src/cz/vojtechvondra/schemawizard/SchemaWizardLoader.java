package cz.vojtechvondra.schemawizard;

import cz.vojtechvondra.schemawizard.gui.WorkspaceFrame;

import javax.swing.*;

/**
 * Application entry point
 */
public class SchemaWizardLoader {

	static public void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				launchGui();
			}
		});
	}

	static protected void launchGui() {
		WorkspaceFrame gui = new WorkspaceFrame();
		gui.create();
	}
}
