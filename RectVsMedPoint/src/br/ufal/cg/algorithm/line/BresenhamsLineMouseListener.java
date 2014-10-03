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

public class BresenhamsLineMouseListener extends PoligonDrawnerListener {

//	private MainWindow mainWindow;

	public BresenhamsLineMouseListener(GridRenderer renderer,
			MainWindow mainWindow) {
		super(renderer,mainWindow);
//		this.mainWindow = mainWindow;
	}

	@Override
	public synchronized void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		renderer.drawPixel(renderer.getPixel(e.getX(), e.getY()),
				mainWindow.getCurrentColor());

	}

	public void keyTyped(KeyEvent e) {
		super.keyTyped(e);

		if (e.getKeyChar() == '\n' && acumVertices.size() > 0) {
			List<Point> points = getPixels(acumVertices);
			Color toDraw = mainWindow.getCurrentColor();
			List<Point> pointsDrawned = new ArrayList<>();
			for (int i = 0; i < points.size(); i++) {
				int next = i + 1;
				if (i == points.size() - 1)
					next = 0;

				pointsDrawned.addAll(drawLine(points.get(i), points.get(next),
						toDraw));
			}

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

		List<Point> drawned = new ArrayList<>();
		int dx, dy, incE, incNE, d, x, y, yInc, xInc;

		double x1 = point.getX(), x2 = point2.getX(), y1 = point.getY(), y2 = point2
				.getY();

		dx = (int) Math.abs(x2 - x1);
		dy = (int) Math.abs(y2 - y1);
		if (dx > dy) // use x as incrementor
		{
			d = 2 * dy - dx;
			incE = 2 * dy;
			incNE = 2 * (dy - dx);

			if (x1 > x2) // switch end-points
			{
				double tmp = x1;
				x1 = x2;
				x2 = tmp;

				tmp = y1;
				y1 = y2;
				y2 = tmp;
			}

			if (y1 < y2) // either slope up or down
			{
				yInc = 1;
			} else {
				yInc = -1;
			}

			y = (int) y1;

			for (x = (int) x1; x <= x2; x++) {
				renderer.drawPixel(x, y, toDraw);
				drawned.add(new Point(x, y));
				if (d < 0) {
					d += incE;
				} else {
					y += yInc;
					d += incNE;
				}
			}
		} else {
			d = 2 * dx - dy;
			incE = 2 * dx;
			incNE = 2 * (dx - dy);
			if (y1 > y2) // switch end-points 
			{
				double tmp = x1;
				x1 = x2;
				x2 = tmp;

				tmp = y1;
				y1 = y2;
				y2 = tmp;
			}
			if (x1 < x2) // either slope up or down
			{
				xInc = 1;
			} else {
				xInc = -1;
			}

			x = (int) x1;

			for (y = (int) y1; y <= y2; y++) {
				renderer.drawPixel(x, y, toDraw);
				drawned.add(new Point(x, y));
				if (d < 0) {
					d += incE;
				} else {
					x += xInc;
					d += incNE;
				}

			}
		}
		return drawned;
	}
}
