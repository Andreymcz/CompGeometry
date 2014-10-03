package br.ufal.cg.algorithm.geometry;

import java.awt.Point;

import br.puc.cg.math.AVector;
import br.ufal.cg.main.MainWindow;
import br.ufal.cg.renderer.GridRenderer;

public class InsidePoligonQ2 extends InsidePoligonAlgorithm {

	private static final double TWO_PI_INV = 1.0 / (2.0 * Math.PI);

	public InsidePoligonQ2(GridRenderer renderer, MainWindow window) {
		super(renderer, window);
	}

	@Override
	protected PointType isInsidePoligon(Point p, Point[] points) {

		double rotationIndex = 0;
		for (int i = 0; i < points.length; i++) {
			PoligonEdge edge = new PoligonEdge(points, i);

			AVector pp0 = new AVector(p, edge.p0);
			AVector pp1 = new AVector(p, edge.p1);

			rotationIndex += pp0.oAngle(pp1);
		}

		rotationIndex = TWO_PI_INV * rotationIndex;

		if (rotationIndex == 1 || rotationIndex == -1) {
			return PointType.INSIDE;
		} else
			return PointType.OUTSIDE;
	}
}
