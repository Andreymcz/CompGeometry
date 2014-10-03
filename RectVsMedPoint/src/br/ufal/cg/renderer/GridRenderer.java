package br.ufal.cg.renderer;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;

import br.ufal.cg.algorithm.GLAlgorithm;

public class GridRenderer implements GLEventListener {

	private float BOUNDS = 600f;
	private int CELL_SIZE = 5;

	private List<Point> pointsToDraw = new ArrayList<>();

	private List<Point> cellsToDraw = new ArrayList<>();
	private List<Color> cellsToDrawColors = new ArrayList<>();

	private List<Point[]> poligonsToDraw = new ArrayList<>();



	private GLCanvas canvas;
	private int curHeight;
	private int curWidth;

	public GridRenderer(GLCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void display(GLAutoDrawable gLDrawable) {
		final GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		createBaseGrid(gl);
		// drawLines(gl);
		drawPoligons(gl);
		drawPoints(gl);

		drawCells(gl);
		gl.glFlush();

	}

	private void drawPoints(GL2 gl) {
		gl.glColor3f(0, 0, 0);

		for (Point p : pointsToDraw) {
			gl.glRectf(p.x - 1, p.y - 1, p.x + 1, p.y + 1);
		}

	}

	private void drawPoligons(GL2 gl) {
		gl.glColor3f(0f, 0f, 0f);
		for (Point[] points : poligonsToDraw) {

			gl.glBegin(GL2.GL_LINE_LOOP);
			for (int j = 0; j < points.length; j++)
				gl.glVertex2f(points[j].x, points[j].y);
			gl.glEnd();
		}

	}

	private void drawCells(GL2 gl) {

		int index = 0;
		for (Point cell : cellsToDraw) {
			Color color = cellsToDrawColors.get(index);
			gl.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
			gl.glRectf(CELL_SIZE * cell.x, CELL_SIZE * cell.y, CELL_SIZE
					* cell.x + CELL_SIZE, CELL_SIZE * cell.y + CELL_SIZE);
			index++;
		}

	}

	private void createBaseGrid(GL2 gl) {

		// float colSize = curWidth / NUM_PIXELS;
		float curXPoint = 0;

		// float rowSize = curHeight / NUM_PIXELS;
		float curYPoint = 0;

		gl.glColor3f(0.7f, 0.7f, 0.7f);
		// Pintar colunas
		while (curXPoint < curWidth) {
			gl.glRectf(curXPoint - 0.5f, curHeight, curXPoint + 0.5f, 0);
			curXPoint += CELL_SIZE;
		}

		while (curYPoint < curHeight) {
			gl.glRectf(0, curYPoint - 0.5f, curWidth, curYPoint + 0.5f);
			curYPoint += CELL_SIZE;
		}

	}

	@Override
	public void init(GLAutoDrawable gld) {
		GL2 gl = gld.getGL().getGL2();
		gl.glClearColor(1f, 1f, 1f, 1f);
		gl.glOrthof(0, BOUNDS, 0, BOUNDS, -1f, 1f);
	}

	@Override
	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width,
			int height) {
//		System.out.println(String.format("x=%d, y=%d, w=%d, h=%d ", x,y,width, height));
		final GL2 gl = gLDrawable.getGL().getGL2();
		// float aspectRatio;
		// //
		// if (height == 0)
		// height = 1;
		// //
		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, 0, height, -1f, 1f);
		//
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		curHeight = height;
		curWidth = width;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {

	}

	/**
	 * 
	 * 
	 * @param xScreenCoord
	 *            Coordenada x da tela
	 * @param yScreenCoord
	 *            Coordenada y da tela
	 */
	public Point getPixel(int xScreenCoord, int yScreenCoord) {

		int posY = curHeight - yScreenCoord;

		int xCell = xScreenCoord / CELL_SIZE;
		int yCell = posY / CELL_SIZE;

		return new Point(xCell, yCell);
	}

	/**
	 * 
	 * 
	 * @param xCoordinatePos
	 *            Coordenada x da tela
	 * @param yCoordinatePos
	 *            Coordenada y da tela
	 */
	public Point getNormalizedPixel(int xCoordinatePos, int yCoordinatePos) {

		int xCell = xCoordinatePos / CELL_SIZE;
		int yCell = yCoordinatePos / CELL_SIZE;

		return new Point(xCell, yCell);
	}

	public void drawPoint(Point point) {
		normalizePoints(point);
		pointsToDraw.add(point);
		canvas.display();
	}

	/**
	 * Pinta o pixel x,y com a cor especificada.
	 * 
	 * O grid começa com 0, 0 na posição inferior esquerda
	 * 
	 * @param x
	 * @param y
	 */
	public void drawPixel(int x, int y, Color color) {
		cellsToDrawColors.add(color);
		cellsToDraw.add(new Point(x, y));
		canvas.display();
	}

	public void drawPixel(Point p, Color color) {
		drawPixel(p.x, p.y, color);
	}

	public void drawPoligon(Point[] array) {
		normalizePoints(array);
		poligonsToDraw.add(array);
		canvas.display();
	}

	private void normalizePoints(Point... points) {
		for (Point point : points) {
			point.setLocation(point.x, curHeight - point.y);
		}

	}

	public void setActiveAction(GLAlgorithm algorithm) {

		pointsToDraw.clear();

		for (KeyListener keyListener : canvas.getKeyListeners()) {
			canvas.removeKeyListener(keyListener);
		}

		for (MouseListener listener : canvas.getMouseListeners()) {
			canvas.removeMouseListener(listener);
		}

		canvas.addMouseListener(algorithm);
		canvas.addKeyListener(algorithm);

		canvas.display();
	}

	public void clear() {
		pointsToDraw.clear();
		cellsToDraw.clear();
		cellsToDrawColors.clear();
		poligonsToDraw.clear();

		canvas.display();

	}

	public void setGridDimension(Integer newDim) {
		CELL_SIZE = newDim;
		clear();
	}
	
	public List<Point[]> getPoligonsToDraw() {
		return poligonsToDraw;
	}

}
