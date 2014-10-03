package br.ufal.cg.algorithm;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;

import br.ufal.cg.main.MainWindow;
import br.ufal.cg.renderer.GridRenderer;

public abstract class GLAlgorithm extends MouseAdapter implements KeyListener {
	
	protected GridRenderer renderer;
	protected MainWindow window;
	
	public GLAlgorithm(GridRenderer renderer, MainWindow window) {
		this.renderer = renderer;
		this.window = window;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}

}
