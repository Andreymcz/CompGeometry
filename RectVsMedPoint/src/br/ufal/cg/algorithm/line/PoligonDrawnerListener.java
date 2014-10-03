package br.ufal.cg.algorithm.line;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.ufal.cg.algorithm.GLAlgorithm;
import br.ufal.cg.main.MainWindow;
import br.ufal.cg.renderer.GridRenderer;

public class PoligonDrawnerListener extends GLAlgorithm {

	protected List<Point> acumVertices = new ArrayList<>();
	protected MainWindow mainWindow;

	public PoligonDrawnerListener(GridRenderer renderer, MainWindow mainWindow) {
		super(renderer, mainWindow);
		this.mainWindow = mainWindow;
	}

	protected void fillPoligon(List<Point> pointsDrawned,
			List<Point> acumVertices) {

		int count = 0;
		int minY = getminY(pointsDrawned);
		int maxY = getMaxY(pointsDrawned);

		for (int i = minY; i <= maxY; i++) {
			List<Point> linePoints = new ArrayList<>();
			linePoints.addAll(getCoordYPoints(acumVertices, i));
			linePoints.addAll(getCoordYPoints(pointsDrawned, i));

			int minX = getminX(linePoints);
			int maxX = getMaxX(linePoints);

			boolean parity = true;
			for (int j = minX; j <= maxX; j++) {
				if (!parity) {
					renderer.drawPixel(j, i, mainWindow.getCurrentColor());
					count++;
				}

				if (linePoints.contains(new Point(j, i))
						&& !linePoints.contains(new Point(j - 1, i)))
					parity = !parity;

			}
		}
		JOptionPane.showMessageDialog(mainWindow, "Tamanho do Polígono: "
				+ count + " px²");
	}

	private List<Point> getCoordYPoints(List<Point> points, int i) {
		List<Point> coordYpoints = new ArrayList<>();
		for (Point point : points) {
			if (point.y == i)
				coordYpoints.add(point);
		}
		return coordYpoints;
	}

	private int getminY(List<Point> pointsDrawned) {
		int min = Integer.MAX_VALUE;

		for (Point point : pointsDrawned) {
			min = point.y < min ? point.y : min;
		}

		return min;
	}

	private int getMaxY(List<Point> pointsDrawned) {
		int max = 0;

		for (Point point : pointsDrawned) {
			max = point.y > max ? point.y : max;
		}

		return max;
	}

	private int getminX(List<Point> pointsDrawned) {
		int min = Integer.MAX_VALUE;

		for (Point point : pointsDrawned) {
			min = point.x < min ? point.x : min;
		}

		return min;
	}

	private int getMaxX(List<Point> pointsDrawned) {
		int max = 0;

		for (Point point : pointsDrawned) {
			max = point.x > max ? point.x : max;
		}

		return max;
	}

	protected List<Point> getPixels(List<Point> acumVertices) {
		List<Point> pixels = new ArrayList<>();

		for (Point point : acumVertices) {
			pixels.add(renderer.getNormalizedPixel(point.x, point.y));
		}
		return pixels;
	}

	protected double getArea(List<Point> acumVer) {
		double area = 0.0;
		Point v1, v2;
		int size = acumVertices.size();

		for (int i = 0; i < size; i++) {
			v1 = acumVer.get(i);
			v2 = acumVer.get((i + 1) % size);

			area += v1.x * v2.y;
			area -= v2.x * v1.y;

		}

		area = area / 2.0;
		area = Math.abs(area);
		return area;
	}

	public void clear() {
		acumVertices.clear();
	}

	@Override
	public synchronized void mouseClicked(MouseEvent e) {
		// renderer.drawPixel(renderer.getPixel(e.getX(), e.getY()), Color.red);
		acumVertices.add(e.getPoint());
		renderer.drawPoint(e.getPoint());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\n' && acumVertices.size() > 0) {
			renderer.drawPoligon(acumVertices.toArray(new Point[0]));
			// acumVertices.clear();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
