package br.ufal.cg.algorithm.geometry;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JOptionPane;

import br.ufal.cg.algorithm.GLAlgorithm;
import br.ufal.cg.main.MainWindow;
import br.ufal.cg.renderer.GridRenderer;

public abstract class InsidePoligonAlgorithm extends GLAlgorithm {

	public enum PointType {
		OUTSIDE, INSIDE, BORDER;
	}

	public InsidePoligonAlgorithm(GridRenderer renderer, MainWindow window) {
		super(renderer, window);

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		Point clicked = e.getPoint();
		renderer.drawPoint(clicked);

		List<Point[]> poligons = renderer.getPoligonsToDraw();
		int countInside = 0;

		for (Point[] points : poligons) {
			if (isInsidePoligon(clicked, points).equals(PointType.INSIDE))
				countInside++;
		}

		JOptionPane.showMessageDialog(window,
				String.format("O ponto está dentro de %d Poligonos", countInside));
	}

	protected abstract PointType isInsidePoligon(Point p, Point[] points);
	
	protected class PoligonEdge {

		Point2D.Double p0;
		Point2D.Double p1;

		// Parametros da eq da reta: y = ax + b
		double a;
		double b;

		public PoligonEdge(Point[] points, int index) {
			p0 = new Point2D.Double(points[index].x, points[index].y);
			p1 = (index == points.length - 1) ? new Point2D.Double(points[0].x, points[0].y) : new Point2D.Double(points[index+1].x, points[index+1].y);

			a = (p1.y - p0.y) / (p1.x - p0.x);
			b = p0.y - a * p0.x;

		}

		public Point getIntersection(Point point) {
			double intersectionX = (point.y - b) / a;

			if (intersectionX >= point.x) {
				return new Point((int) intersectionX, point.y);
			} else {
				return null;
			}
		}

		public boolean isHorizontal() {
			return p0.y == p1.y;
		}
	}
}
