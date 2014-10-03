package br.ufal.cg.algorithm.geometry;

import java.awt.Point;

import br.ufal.cg.main.MainWindow;
import br.ufal.cg.renderer.GridRenderer;
/**
 * 
 * Questao 1 da lista de exercicios_1 do prof. Hélio Lopes {@link http://www-di.inf.puc-rio.br/~lopes/index.html}
 * 
 * 
 * @author andrey
 *
 */
public class InsidePoligonQ01 extends InsidePoligonAlgorithm{

	public InsidePoligonQ01(GridRenderer renderer, MainWindow window) {
		super(renderer, window);
	}

	@Override
	protected PointType isInsidePoligon(Point p, Point[] points) {
		int n = 0;

		for (int i = 0; i < points.length; i++) {
			PoligonEdge edge = new PoligonEdge(points, i);
			Point inter = null;

			if (!edge.isHorizontal() && // Verify if edge is a horizontal line
					(inter = edge.getIntersection(p)) != null) {
				if (inter.x == p.x) {
					return PointType.BORDER;
					
				} else {
					if (inter.x > p.x
							&& inter.y > Math.min(edge.p0.y, edge.p1.y))
						n++;
				}
			} else {
				if(p.equals(edge.p1) || p.equals(edge.p0)) return PointType.BORDER;
			}
		}

		return n % 2 == 0 ? PointType.OUTSIDE : PointType.INSIDE;
	}
	
}
