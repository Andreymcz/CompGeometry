package br.ufal.cg.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;
import br.ufal.cg.algorithm.geometry.InsidePoligonAlgorithm;
import br.ufal.cg.algorithm.geometry.InsidePoligonQ01;
import br.ufal.cg.algorithm.line.BresenhamsLineMouseListener;
import br.ufal.cg.algorithm.line.RectEquationLineMouseListener;
import br.ufal.cg.renderer.GridRenderer;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private JButton linesColorChooser;
	private JCheckBox fillPol;
	private JCheckBox analitcArea;

	public MainWindow() {
		super("Equação da reta VS Bresham ");
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilites = new GLCapabilities(profile);
		capabilites.setDoubleBuffered(true);

		canvas = new GLCanvas(capabilites);

		final GridRenderer r;
		canvas.addGLEventListener(r = new GridRenderer(canvas));
		canvas.setPreferredSize(new Dimension(0, 0));

		initGUIComponents(canvas, r);

		// MouseClickListener listener = new MouseClickListener(r);
		// canvas.addMouseListener(listener);

		// getContentPane().add(canvas);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setSize(new Dimension(800, 600));
		pack();
		// setVisible(true);

		// setSize(new Dimension(800, 600));
		canvas.requestFocus();

	}

	private void initGUIComponents(GLCanvas canvas2, final GridRenderer renderer) {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setOpaque(true);

		JPanel buttonsPanel = new JPanel(new MigLayout(""));

		ButtonGroup group = new ButtonGroup();

		final MainWindow mainThis = this;

		final RectEquationLineMouseListener rectListener = new RectEquationLineMouseListener(
				renderer, mainThis);
		final JToggleButton rectEquation = new JToggleButton(
				new AbstractAction("Eq. da reta") {

					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						renderer.setActiveAction(rectListener);
					}
				});
		group.add(rectEquation);

		final BresenhamsLineMouseListener breshanListener = new BresenhamsLineMouseListener(
				renderer, mainThis);
		final JToggleButton bresenhamsLine = new JToggleButton(
				new AbstractAction("Bresenham's Line") {

					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						renderer.setActiveAction(breshanListener);

					}
				});
		group.add(bresenhamsLine);

		
		final InsidePoligonAlgorithm insidePoligonListener = new InsidePoligonQ01(
				renderer, mainThis);
		final JToggleButton insidePoligon = new JToggleButton(
				new AbstractAction("Inside Poligon Q01") {

					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						renderer.setActiveAction(insidePoligonListener);
					}
				});
		group.add(insidePoligon);		
		
				
		// Polígono
		addSeparator(buttonsPanel, "Polígonos");

		buttonsPanel.add(rectEquation, "wrap,growx");
		buttonsPanel.add(bresenhamsLine, "wrap 20, growx");
		buttonsPanel.add(insidePoligon, "wrap, growx");
		
		buttonsPanel.add(new JLabel("Cor:"), "split 2");
		buttonsPanel.add(linesColorChooser = new JButton(new ChooseColorAction(
				this)), "h 30!, w 30!,wrap 20");
		linesColorChooser.setBackground(Color.RED);

		// Preencher
		fillPol = new JCheckBox("Preencher Polígono");

		analitcArea = new JCheckBox("Calcular área analítica");

		buttonsPanel.add(fillPol, "wrap,span");
		buttonsPanel.add(analitcArea, "wrap 20,span");

		Integer[] obj = new Integer[] { 1, 2, 5, 10, 15, 20, 25, 30, 40, 45, 50 };
		JComboBox<Integer> comboBox = new JComboBox<>(obj);
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				Integer newDim = Integer.parseInt(e.getItem().toString());
				renderer.setGridDimension(newDim);
				breshanListener.clear();
				rectListener.clear();
			}
		});
		comboBox.setSelectedItem(new Integer(5));

		buttonsPanel.add(new JLabel("Tamanho do grid:"), "split 2");
		buttonsPanel.add(comboBox, "wrap 40");

		// FIM Polígono

		// Clear
		buttonsPanel.add(new JButton(new AbstractAction("Clear") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				renderer.clear();
			}
		}), "growx, wrap");

		// Créditos:
		JPanel creditsPanel = new JPanel(new MigLayout());

		JLabel credits = new JLabel("Créditos:");
		credits.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

		creditsPanel.add(credits, "wrap 15");

		JLabel name1 = new JLabel("Andrey Rodrigues");
		name1.setFont(new Font(Font.SERIF, Font.BOLD, 13));
		JLabel name2 = new JLabel("Arthur Maia");
		name2.setFont(new Font(Font.SERIF, Font.BOLD, 13));

		creditsPanel.add(name1, "wrap");
		creditsPanel.add(name2);

		buttonsPanel.add(creditsPanel, "south");

		JPanel leftPanel = new JPanel(new MigLayout("fill"));
		leftPanel.add(buttonsPanel, "wrap,dock north");
		leftPanel.add(creditsPanel,"dock south");

		JPanel canvasPanel = new JPanel(new BorderLayout());
		canvasPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		canvasPanel.setOpaque(true);

		canvasPanel.add(canvas2);

		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(canvasPanel, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		getContentPane().add(mainPanel);

	}

	public static void addSeparator(JPanel panel, String text) {

		if (!(panel.getLayout() instanceof MigLayout))
			return;

		JLabel l = new JLabel(text);
		l.setForeground(new Color(0, 70, 213));

		panel.add(l, "gapbottom 1, span, split 2");
		panel.add(new JSeparator(), "gapleft rel, growx,wrap");
	}

	public static void main(String[] args) {

		String laf = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";

		try {
			UIManager.setLookAndFeel(laf);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		final MainWindow main = new MainWindow();
		main.setVisible(true);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				main.setSize(800, 600);
			}
		});
	}

	public Color getCurrentColor() {
		return linesColorChooser.getBackground();
	}

	public boolean isFillPoligonActived() {
		return fillPol.isSelected();
	}

	public boolean isCalculateAnalitcArea() {
		return analitcArea.isSelected();
	}

}
