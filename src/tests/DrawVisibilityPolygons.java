package tests;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;

import visibilityPolygon.CCWPolygon;
import visibilityPolygon.VisibilityPolygon;

/**
 *	Used for visualizing visibility polygons, some examples are included.
 */
public class DrawVisibilityPolygons {

	public static void main(String[] args) {
//       DC1();
//	     DC2();
//	     DC3Original();
//		 DC3Corrected();
//		 interiorNotConvex();
//		 interiorConvex();
//		 onEdgeConvex();
//		 onEdgeNotConvex();
//		 onVertexIsConvex();
		onVertexNotConvex();
	}

	// First data challenge (AKN)
	private static void DC1() {
		CCWPolygon inputPol;
		List<Point2D> viewPoints;

		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(13 / 4, 2));
		vertices.add(new Point2D.Double(3, 3));
		vertices.add(new Point2D.Double(5, 3));
		vertices.add(new Point2D.Double(5, 5));
		vertices.add(new Point2D.Double(15 / 4, 21 / 4));
		vertices.add(new Point2D.Double(4, 4));
		vertices.add(new Point2D.Double(9 / 4, 15 / 4));

		inputPol = new CCWPolygon(vertices);

		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(5 / 2, 5 / 2));
		viewPoints.add(new Point2D.Double(9 / 4, 3 / 1));

		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 120);
	}

	// Second data challenge (HMS)
	private static void DC2() {
		CCWPolygon inputPol;
		List<Point2D> viewPoints;

		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(171601 / 1000, 96672 / 1000));
		vertices.add(new Point2D.Double(170845 / 1000, 159416 / 1000));
		vertices.add(new Point2D.Double(176892 / 1000, 151101 / 1000));
		vertices.add(new Point2D.Double(180294 / 1000, 171889 / 1000));
		vertices.add(new Point2D.Double(101297 / 1000, 171889 / 1000));
		vertices.add(new Point2D.Double(100919 / 1000, 151101 / 1000));
		vertices.add(new Point2D.Double(109991 / 1000, 159038 / 1000));
		vertices.add(new Point2D.Double(111124 / 1000, 97428 / 1000));
		vertices.add(new Point2D.Double(102431 / 1000, 104610 / 1000));
		vertices.add(new Point2D.Double(102053 / 1000, 83065 / 1000));
		vertices.add(new Point2D.Double(179160 / 1000, 78529 / 1000));
		vertices.add(new Point2D.Double(177649 / 1000, 93271 / 1000));
		vertices.add(new Point2D.Double(165553 / 1000, 87601 / 1000));
		inputPol = new CCWPolygon(vertices);

		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(106211 / 1000, 93838 / 1000));
		viewPoints.add(new Point2D.Double(167821 / 1000, 83443 / 1000));
		viewPoints.add(new Point2D.Double(104699 / 1000, 162629 / 1000));
		viewPoints.add(new Point2D.Double(175948 / 1000, 162818 / 1000));

		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 4);
	}

	// Third data challenge (erroneous) (JMN)
	private static void DC3Original() {
		CCWPolygon inputPol;
		List<Point2D> viewPoints;

		// vertices are not in CCW order, the algorithm fails.
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(1, 6));
		vertices.add(new Point2D.Double(7, 6));
		vertices.add(new Point2D.Double(7, 3 / 2));
		vertices.add(new Point2D.Double(6, 0));
		vertices.add(new Point2D.Double(5, 1));
		vertices.add(new Point2D.Double(4, 5));
		vertices.add(new Point2D.Double(4, 3));
		vertices.add(new Point2D.Double(2, 3));
		vertices.add(new Point2D.Double(0, 5));
		inputPol = new CCWPolygon(vertices);

		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(3, 5));

		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 50);
	}

	// Third data challenge, corrected CCW version (JMN)
	private static void DC3Corrected() {
		CCWPolygon inputPol;
		List<Point2D> viewPoints;

		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(1, 6));
		vertices.add(new Point2D.Double(0, 5));
		vertices.add(new Point2D.Double(2, 3));
		vertices.add(new Point2D.Double(4, 3));
		vertices.add(new Point2D.Double(4, 5));
		vertices.add(new Point2D.Double(5, 1));
		vertices.add(new Point2D.Double(6, 0));
		vertices.add(new Point2D.Double(7, 3 / 2));
		vertices.add(new Point2D.Double(7, 6));
		inputPol = new CCWPolygon(vertices);

		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(3, 5));

		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 50);
	}

	/**
	 * Viewpoint is located in the polygon's interior. The polygon is convex.
	 */
	private static void interiorConvex() {
		CCWPolygon inputPol;
		List<Point2D> viewPoints;

		// z interior, polygon convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));

		inputPol = new CCWPolygon(vertices);
		// Point2D z = new Point2D.Double(4, 4);

		// System.out.println("inputPol: " + pol.vertices);

		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(4, 4));

		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 50);
	}

	/**
	 * Viewpoint is located in the polygon's interior. The polygon is not convex.
	 */
	private static void interiorNotConvex() {
		CCWPolygon inputPol;
		List<Point2D> viewPoints;

		// z interior, polygon not convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));

		inputPol = new CCWPolygon(vertices);

		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(4, 4));
		// viewPoints.add(new Point2D.Double(4, 3.5));
		// viewPoints.add(new Point2D.Double(4, 3.3));
		// viewPoints.add(new Point2D.Double(4, 3.2));
		// viewPoints.add(new Point2D.Double(4, 3.1));
		// viewPoints.add(new Point2D.Double(4, 3.0));
		// viewPoints.add(new Point2D.Double(3.1, 2.98));
		// viewPoints.add(new Point2D.Double(2.8, 2.98));
		// viewPoints.add(new Point2D.Double(2.1, 2.98));
		// viewPoints.add(new Point2D.Double(2.005, 2.98));

		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 50);
	}


	/**
	 * Viewpoint is located on one of the polygon's edges. The polygon is convex.
	 */
	private static void onEdgeConvex() {
		CCWPolygon inputPol;
		List<Point2D> viewPoints;

		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));
		inputPol = new CCWPolygon(vertices);

		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(4, 2));


		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 50);
	}
	
	/**
	 * Viewpoint is located on one of the polygon's edges. The polygon is not convex.
	 */
	public static void onEdgeNotConvex() {

		CCWPolygon inputPol;
		List<Point2D> viewPoints;
		
		// z is on edge of boundary, polygon is not convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));
		inputPol = new CCWPolygon(vertices);
		
		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(-2, 3));


		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 40);
	}
	
	/**
	 * Viewpoint is located on one of the polygon's vertices. The polygon is convex.
	 */
	public static void onVertexIsConvex() {
		
		CCWPolygon inputPol;
		List<Point2D> viewPoints;
		
		// z is on boundary (polygon vertex), polygon is convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));
		inputPol = new CCWPolygon(vertices);
		
		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(6, 2));


		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 50);
	}
	
	/**
	 * Viewpoint is located on one of the polygon's vertices. The polygon is not convex.
	 */
	public static void onVertexNotConvex() {

		CCWPolygon inputPol;
		List<Point2D> viewPoints;
		
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));
		inputPol = new CCWPolygon(vertices);
		
		viewPoints = new ArrayList<>();
		viewPoints.add(new Point2D.Double(1, 4));


		assert (inputPol != null && viewPoints != null);
		Figure fig = new Figure(inputPol, viewPoints, 50);
	}
}

/**
 * Used to visualize visibility polygons.
 */
class Figure extends JPanel {

	private Path2D inputPol;
	private List<Path2D> visPolygons;
	private List<Path2D> viewPoints;

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		// AffineTransform at = g2d.getTransform();

		// translate origin to center
		// g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
		g2d.translate(this.getWidth() / 6, this.getHeight() / 1.3);

		// invert y axis
		g2d.scale(1, -1);

		// restore transformation for conventional rendering
		// g2d.setTransform(at);

		// rotates JPanel contents by 180 degrees
		// int x = this.getWidth() / 2;
		// int y = this.getHeight() / 2;
		// g2d.rotate(Math.toRadians(180.0), x, y);

		// g2d.translate(this.getWidth() / 2, this.getHeight() / 2);

		// draws input polygon
		g2d.setColor(Color.BLACK);
		g2d.fill(inputPol);

		// draws visibility polygons
		g2d.setColor(Color.YELLOW);
		for (Path2D curr : visPolygons)
			g2d.fill(curr);

		// draws viewPoints
		g2d.setColor(Color.RED);
		for (Path2D curr : viewPoints)
			g2d.fill(curr);

		g2d.dispose();
	}

	/**
	 * Visualizes the visibility polygon. Also works with multiple viewPoints.
	 * @param inputPol Simple Polygon inside of which the viewpoints lie.
	 * @param viewPoints Viewpoints from which the visibility polygons will be computed.
	 * @param scaling Factor for scaling the visualization.
	 */
	public Figure(CCWPolygon inputPol, List<Point2D> viewPoints, int scaling) {

		// computes visibility polygons (VP's) for each viewPoint individually
		List<CCWPolygon> CCWVisPolygons = VisibilityPolygon.computeVisPol(inputPol, viewPoints);

		// System.out.println("input pol: " + inputPol.vertices);
		// System.out.println("vis pol: " + CCWVisPolygons.get(0).vertices);

		// scales and converts CCW VP's to Path2D's
		visPolygons = CCWVisPolygons.stream().map(x -> x.scale(scaling).getPolygon()).collect(Collectors.toList());

		// scales and converts input polygon to Path2D
		this.inputPol = inputPol.scale(scaling).getPolygon();

		// converts Point2D to Path2D
		this.viewPoints = new ArrayList<>();
		for (Point2D curr : viewPoints) {
			Path2D.Double temp = new Path2D.Double();
			temp.append(new Ellipse2D.Double(curr.getX() * scaling, curr.getY() * scaling, 5, 5), true);
			this.viewPoints.add(temp);
		}
		
		JFrame frame = new JFrame();
		frame.setTitle("Visibility");
		frame.setSize(800, 700);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		Container contentPane = frame.getContentPane();
		contentPane.add(this);
		frame.setVisible(true);
	}
}
