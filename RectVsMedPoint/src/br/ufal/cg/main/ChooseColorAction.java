package br.ufal.cg.main;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

public class ChooseColorAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame parent;

	public ChooseColorAction(JFrame parent) {
		this.parent = parent;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (parent.isEnabled()) {
			Color color = JColorChooser.showDialog(parent, "Cor",
					((AbstractButton) e.getSource()).getBackground());
			((AbstractButton) e.getSource()).setBackground(color);
		}
	}

}
