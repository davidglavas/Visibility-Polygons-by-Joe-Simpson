package visibilityPolygon;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CCWPolygon {

	// polygon as vertices in counter-clockwise order
	private List<Point2D> vertices;
	
	/**
	 * Initializes polygon represented as vertices in counter-clockwise order.
	 * @param vertices vertices of the polygon in counter-clockwise order.
	 */
	public CCWPolygon(List<Point2D> vertices) {
		this.vertices = vertices;
	}
	
	public List<Point2D> getVertices() {
		return vertices;
	}
	
	public void addVertex(Point2D newVertex) {
		vertices.add(newVertex);
	}
	
	/**
	 * Example: 
	 * pol.vertices = [v0,v1,v2,v3]
	 * pol.getEdges() returns the following line segments:
	 * <v0,v1>, <v1,v2>, <v2,v3>, <v3,v0>
	 * 
	 * @return edges of the polygon
	 */
	public List<LineSegment> getEdges() {
		
		List<LineSegment> ret = new LinkedList<LineSegment>();
		
		int n = vertices.size();
		
		for (int i = 1; i < n; i++) {
			ret.add(new LineSegment(vertices.get(i-1), vertices.get(i)));
		}
		
		// closes the polygon
		ret.add(new LineSegment(vertices.get(n-1), vertices.get(0)));
		
		assert(ret.get(0).a.equals(ret.get(n-1).b));
		
		return ret;
	}
	
	
	/**
	 * Checks whether any of the polygons edges "block the view" from origin to point v.
	 * An edge e of the polygon blocks the view from origin to point v if and only if
	 * e properly intersects (interiors intersect) the line segment formed by origin and v.
	 * @param v Point whose visibility from origin we are checking.
	 * @return true iff. polygon does not block the view from origin to v.
	 */
	public boolean visibleFromOrigin(Point2D v) {
		
		List<LineSegment> es = getEdges();
		LineSegment e = new LineSegment(CommonUtils.origin2D, v);
		
		for (LineSegment curr : es) {
			if (curr.intersectProper(e))
				return false;	// edge curr of the polygon properly intersects e, hence v is not visible from origin
		}
		
		// no edge of the polygon "blocks the view" (properly intersects e) from origin to v
		return true;
	}
	
	/**
	 * Translates polygon such that p becomes the origin.
	 * @param p New origin upon which we shift.
	 * @return Translated polygon such that p becomes the origin.
	 */
	public CCWPolygon shiftToOrigin(Point2D p) {
		List<Point2D> shiftedPol = new ArrayList<>();
		
		for (Point2D curr : vertices) {
			shiftedPol.add(new Point2D.Double(curr.getX() - p.getX(), curr.getY() - p.getY()));
		}
		
		return new CCWPolygon(shiftedPol);
	}
	
	/**
	 * Converts the polygon into a Path2D representation.
	 * @return the CCWPolygon as Path2D.
	 */
	public Path2D getPolygon() {
		Path2D pol = new Path2D.Double();
		
		pol.moveTo(vertices.get(0).getX(), vertices.get(0).getY());
		
		for (Point2D curr : vertices) {
			pol.lineTo(curr.getX(), curr.getY());
		}
		
		pol.closePath();
		
		return pol;
	}
	
	/**
	 * Uniformly scales the polygon.
	 * @param x Scaling factor.
	 * @return Scaled polygon.
	 */
	public CCWPolygon scale(int x) {
		List<Point2D> scaledPol = new ArrayList<>();
		
		for (Point2D curr : vertices) {
			scaledPol.add(new Point2D.Double(curr.getX()*x, curr.getY()*x));
		}
		
		return new CCWPolygon(scaledPol);
	}
}
