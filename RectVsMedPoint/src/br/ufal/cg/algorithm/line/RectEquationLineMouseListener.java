package br.ufal.cg.algorithm.line;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.ufal.cg.main.MainWindow;
import br.ufal.cg.renderer.GridRenderer;

public class RectEquationLineMouseListener extends PoligonDrawnerListener {

	public RectEquationLineMouseListener(GridRenderer renderer,
			MainWindow mainWindow) {
		super(renderer,mainWindow);
	}

	@Override
	public synchronized void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		renderer.drawPixel(renderer.getPixel(e.getX(), e.getY()),
				mainWindow.getCurrentColor());

	}

	@Override
	public void keyTyped(KeyEvent e) {
		super.keyTyped(e);

		if (e.getKeyChar() == '\n' && acumVertices.size() > 0) {
			List<Point> pointsDrawned = new ArrayList<>();
			List<Point> points = getPixels(acumVertices);
			Color toDraw = mainWindow.getCurrentColor();
			for (int i = 0; i < points.size(); i++) {
				int next = i + 1;
				if (i == points.size() - 1)
					next = 0;

				pointsDrawned.addAll(drawLine(points.get(i), points.get(next),
						toDraw));
			}

			// Preencher o polígono
			if (mainWindow.isFillPoligonActived())
				fillPoligon(pointsDrawned, getPixels(acumVertices));
			if (mainWindow.isCalculateAnalitcArea())
				JOptionPane.showMessageDialog(mainWindow,
						"A área do polígono desenhado é :"
								+ getArea(points)+"px²");

		}
		acumVertices.clear();
	}

	

	

	private List<Point> drawLine(Point point, Point point2, Color toDraw) {
		int x, y;

		double x1 = point.getX(), x2 = point2.getX(), y1 = point.getY(), y2 = point2
				.getY();

		List<Point> pointsDrawned = new ArrayList<>();

		boolean invertY = false;
		if (x2 < x1) {
			double aux = x1;
			x1 = x2;
			x2 = aux;

			aux = y1;
			y1 = y2;
			y2 = aux;

		}

		if (y2 < y1) {
			y1 = -y1;
			y2 = -y2;
			invertY = true;
		}

		if (Math.abs(y2 - y1) < Math.abs(x2 - x1)) {
			float a;
			a = (float) ((y2 - y1) / (x2 - x1));
			for (x = (int) x1 + 1; x < x2; x++) {
				/* arredonda y */
				y = (int) (y1 + a * (x - x1));
				if (invertY) {
					renderer.drawPixel(x, -y, toDraw);
					pointsDrawned.add(new Point(x, -y));
				} else {
					renderer.drawPixel(x, y, toDraw);
					pointsDrawned.add(new Point(x, y));
				}
			}/* end for */
		} else {
			float a;
			a = (float) ((x2 - x1) / (y2 - y1));
			for (y = (int) y1 + 1; y < y2; y++) {
				/* arredonda y */
				x = (int) (x1 + a * (y - y1));
				if (invertY) {
					renderer.drawPixel(x, -y, toDraw);
					pointsDrawned.add(new Point(x, -y));
				} else {
					renderer.drawPixel(x, y, toDraw);
					pointsDrawned.add(new Point(x, y));
				}
			}/* end for */
		}
		return pointsDrawned;
	}

//	private List<Point> getPixels(List<Point> acumVertices) {
//		List<Point> pixels = new ArrayList<>();
//
//		for (Point point : acumVertices) {
//			pixels.add(renderer.getNormalizedPixel(point.x, point.y));
//		}
//		return pixels;
//	}
}
