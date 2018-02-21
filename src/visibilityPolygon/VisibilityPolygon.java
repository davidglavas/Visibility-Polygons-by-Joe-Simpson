package visibilityPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import visibilityPolygon.CommonUtils.Pair;
import visibilityPolygon.CommonUtils.Ray2D;

/**
 * This class can be used to compute the visibility polygon from a point inside
 * of a simple polygon (given as n vertices in CCW order) in O(n) time.
 * 
 * Usage example:
  
        // initialize polygon vertices in CCW order
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));

		// initialize polygon
		CCWPolygon pol = new CCWPolygon(vertices);
		// initialize viewpoint
		Point2D z = new Point2D.Double(4, 4);

		// VP contains the visibility polygon from z in pol in CCW order.
		CCWPolygon VP = VisibilityPolygon.computeVisPol(pol, z);
 * 
 * 
 * 
 * Based on: Joe and Simpson's (1985) visibility polygon algorithm.
 * 		     https://cs.uwaterloo.ca/research/tr/1985/CS-85-38.pdf
 * 
 */
public class VisibilityPolygon {

	/**
	 * Computes visibility polygon from one viewpoint.
	 * @param pol Simple polygon.
	 * @param z Viewpoint inside (boundary is fine too) of the polygon.
	 * @return Visibility polygon in CCW order.
	 */
	public static CCWPolygon computeVisPol(CCWPolygon pol, Point2D z) {

		if (pol.getVertices().size() < 3)
			return null;

		// TODO
		// check if point is in polygon (point on the boundary is considered inside)
		// if (!pol.containsPoint(z))
		// return null;

		// Computes and returns the visibility polygon.
		return compute(pol, z);
	}
	
	
	/**
	 * Computes the visibility polygons from each of the viewpoints individually.
	 * @param inputPol	The polygon in which the viewPoints are contained.
	 * @param viewPoints	The viewpoints from which we are computing the visibility polygon.
	 * @return	List of visibility polygons for the individual viewPoints.
	 */
	public static List<CCWPolygon> computeVisPol(CCWPolygon inputPol, List<Point2D> viewPoints) {

		if (inputPol.getVertices().size() < 3)
			return null;

		// TODO
		// check if point is in polygon (point on the boundary is considered inside)
		// if (!pol.containsPoint(z))
		// return null;
		
		List<CCWPolygon> visPolygons = new ArrayList<>();
		
		// computes and stores the VP for each viewPoint
		for (Point2D curr : viewPoints) {
			CCWPolygon currVP = compute(inputPol, curr);
			visPolygons.add(currVP);
		}
		
		// one VP per viewPoint
		assert(visPolygons.size() == viewPoints.size());
		
		return visPolygons;
	}

	/**
	 * Computes visibility polygon from z in pol.
	 * @param pol Input polygon.
	 * @param z Viewpoint in pol.
	 * @return Visibility polygon in CCW order.
	 */
	private static CCWPolygon compute(CCWPolygon pol, Point2D z) {
		// list v, satisfies assumptions made in paper (section 2, paragraph 1 and 2).
		Pair<VsRep, Double> temp = preprocess(pol, z);

		VsRep vs = temp.first;
		double initAngle = temp.second;

		VertDispl v0 = vs.get(0);

		Deque<VertDispl> s0 = new LinkedList<>();
		s0.push(v0);

		assert (vs.n > 1);

		List<VertDispl> s; // used to store the vertices of the visibility
							// polygon
		if (CommonUtils.epsGEQ(vs.get(1).getDisplacement(), v0.getDisplacement()))
			s = advance(vs, s0, 0);
		else
			s = scan(vs, s0, 0, null, Orientation.CLOCKWISE);	// -1 is Clockwise

		assert (CommonUtils.epsEquals(s.get(s.size() - 1).getPoint().toCartesian(), v0.getPoint().toCartesian()));

//		System.out.println("s0 deque: " + s0);
		
		// converts stack containing the visibility polygon into final ccw visibility
		// polygon
		return postprocess(s, vs, z, initAngle);
	}

	// Pushes vertices on the stack.
	private static List<VertDispl> advance(VsRep v, Deque<VertDispl> s, int iprev) {

		int n = v.n - 1;

		assert (iprev + 1 <= n);

		if (CommonUtils.epsLEQ(v.get(iprev + 1).getDisplacement(), CommonUtils.PI2)) {
			int i = iprev + 1;
			s.push(v.get(i));

			if (i == n)
				return new ArrayList<VertDispl>(s);

			if (CommonUtils.epsLess(v.get(i + 1).getDisplacement(), v.get(i).getDisplacement())
					&& CommonUtils.pointTurn(v.get(i - 1).getPoint().toCartesian(), v.get(i).getPoint().toCartesian(),
							v.get(i + 1).getPoint().toCartesian()) == Orientation.CLOCKWISE) { // -1 is RightTurn
				
				return scan(v, s, i, null, Orientation.CLOCKWISE); // -1 is Clockwise
			} else if (CommonUtils.epsLess(v.get(i + 1).getDisplacement(), v.get(i).getDisplacement())
					&& CommonUtils.pointTurn(v.get(i - 1).getPoint().toCartesian(), v.get(i).getPoint().toCartesian(),
							v.get(i + 1).getPoint().toCartesian()) == Orientation.COUNTERCLOCKWISE) { // 1 is LeftTurn

				return retard(v, s, i);
			} else {
				return advance(v, s, i);
			}
		} else {
			VertDispl v0 = v.get(0);

			if (s.peekFirst().getDisplacement() < CommonUtils.PI2) {
				Ray2D ray = new Ray2D(CommonUtils.origin2D, v0.getPoint().theta);

				Point2D isect = LineSegment.intersectSegmentWithRay(
						new LineSegment(v.get(iprev).getPoint().toCartesian(), v.get(iprev + 1).getPoint().toCartesian()), ray);

				assert (isect != null);

				VertDispl st = displacementInBetween(new PolarPoint2D(isect), v.get(iprev), v.get(iprev + 1));
				s.push(st);
			}

			return scan(v, s, iprev, v0, Orientation.COUNTERCLOCKWISE); // 1 is Counterclockwise
		}

	}

	// Pops vertices from stack
	private static List<VertDispl> retard(VsRep v, Deque<VertDispl> sOld, int iprev) {
		VertDispl sj1 = sOld.peekFirst();
		List<VertDispl> ssTail = new ArrayList<>(sOld).subList(1, sOld.size());
		
		Pair<Deque<VertDispl>, VertDispl> temp = locateSj(v.get(iprev), v.get(iprev + 1), sj1, ssTail);
		Deque<VertDispl> s = temp.first;
		VertDispl sjNext = temp.second;
		
		VertDispl sj = s.peekFirst();
		
		if (sj.getDisplacement() < v.get(iprev + 1).getDisplacement()) {
			int i = iprev + 1;	
			
			VertDispl vi = v.get(i);
			Point2D p = LineSegment.intersectSegmentWithRay(new LineSegment(sj.getPoint().toCartesian(), sjNext.getPoint().toCartesian()), new Ray2D(CommonUtils.origin2D, vi.getPoint().theta)); 
			VertDispl st1 = displacementInBetween(new PolarPoint2D(p), sj, sjNext);
			
			if (st1 != null)
				s.push(st1);
			
			s.push(vi);
			
			// paper does i == v.n
			if (i == v.n - 1) {
				return new ArrayList<VertDispl>(s);
			}
			else if (CommonUtils.epsGEQ(v.get(i+1).getDisplacement(), vi.getDisplacement()) && CommonUtils.pointTurn(v.get(i-1).getPoint().toCartesian(), vi.getPoint().toCartesian(), v.get(i+1).getPoint().toCartesian()) == Orientation.CLOCKWISE) { // -1 is RighTurn
				return advance(v, s, i);
			} else if (CommonUtils.epsGreater(v.get(i+1).getDisplacement(), vi.getDisplacement()) && CommonUtils.pointTurn(v.get(i-1).getPoint().toCartesian(), vi.getPoint().toCartesian(), v.get(i+1).getPoint().toCartesian()) == Orientation.COUNTERCLOCKWISE) {  // 1 is LeftTurn
				s.pop();
				return scan(v, s, i, vi, Orientation.COUNTERCLOCKWISE);  // 1 is Counterclockwise
			} else {
				s.pop();
				return retard(v, s, i);
			}
		} else {
			if (CommonUtils.epsEquals(v.get(iprev + 1).getDisplacement(), sj.getDisplacement()) &&
				CommonUtils.epsGreater(v.get(iprev + 2).getDisplacement(), v.get(iprev + 1).getDisplacement()) &&
				CommonUtils.pointTurn(v.get(iprev).getPoint().toCartesian(), v.get(iprev + 1).getPoint().toCartesian(), v.get(iprev + 2).getPoint().toCartesian()) == Orientation.CLOCKWISE) {  // -1 is RightTurn
				
				s.push(v.get(iprev + 1));
				return advance(v, s, iprev + 1);
				
			} else {
				VertDispl w = intersectWithWindow(v.get(iprev), v.get(iprev + 1), sj, sjNext);

				assert(w != null);
				return scan(v, s, iprev, w, Orientation.CLOCKWISE); // -1 is Clockwise
			}
		}
	}
	
	// Skips invisible vertices
	private static List<VertDispl> scan(VsRep v, Deque<VertDispl> s, int iprev, VertDispl windowEnd, Orientation ori) {
		int i = iprev + 1;
		
		if (i+1 == v.n) return new ArrayList<VertDispl>(s);
		
		if (ori == Orientation.CLOCKWISE &&		// -1 is Clockwise
			CommonUtils.epsGreater(v.get(i+1).getDisplacement(), s.peekFirst().getDisplacement()) && 
			CommonUtils.epsGEQ(s.peekFirst().getDisplacement(), v.get(i).getDisplacement())) {
			
			VertDispl intersec = intersectWithWindow(v.get(i), v.get(i+1), s.peekFirst(), windowEnd);
			
			if (intersec != null && !(windowEnd != null && CommonUtils.epsEquals(intersec.getPoint().toCartesian(), windowEnd.getPoint().toCartesian()))) {
				s.push(intersec);
				return advance(v, s, i);
			} else {
				return scan(v, s, i, windowEnd, ori);
			}
			
			
		} else if (ori == Orientation.COUNTERCLOCKWISE &&		// 1 is Counterclockwise
				   CommonUtils.epsLEQ(v.get(i+1).getDisplacement(), s.peekFirst().getDisplacement()) &&
				   s.peekFirst().getDisplacement() < v.get(i).getDisplacement()) {
			
			if (intersectWithWindow(v.get(i), v.get(i+1), s.peekFirst(), windowEnd) != null) {
				return retard(v, s, i);
			} else {
				return scan(v, s, i, windowEnd, ori);
			}
		} else {
			return scan(v, s, i, windowEnd, ori);
		}
		
	}

	/**
	 * Preprocesses a polygon pol and a point z within it such that assumptions
	 * made in Joe and Simpson's 1985 paper (see section 2, paragraph 1 and 2)
	 * are satisfied.
	 * 
	 * @param pol
	 *            Polygon stored as vertices in counter-clockwise order.
	 * @param z
	 *            Point inside of the polygon (boundary and interior are
	 *            considered inside).
	 * @return list v of vertices that satisfy all assumptions about the input
	 *         made in section 2 of the paper (Joe&Simpson, 1985).
	 */
	private static Pair<VsRep, Double> preprocess(CCWPolygon pol, Point2D z) {

		pol = pol.shiftToOrigin(z);

		boolean zIsVertex = pol.getVertices().contains(CommonUtils.origin2D);

		// determines v0
		PolarPoint2D v0 = getInitialVertex(pol, zIsVertex);

		assert (!v0.equals(z));

		// converts Cartesian vertices of polygon to polar and stores them in
		// list l
		List<PolarPoint2D> l = pol.getVertices().stream().map(x -> new PolarPoint2D(x)).collect(Collectors.toList());

		// adjusts positions such that v0 at the beginning
		placeV0First(l, v0);

		assert (l.get(0).equals(v0));
		
		// if z is on boundary?? then [v0, v1, ..., vk, z] -> [z, v0, v1, ...,
		// vk]
		adjustPositionOfz(l, zIsVertex, z);

		// rotate all points of the shifted polygon clockwise such that v0 lies
		// on the x axis
		for (PolarPoint2D curr : l) {
			if (!curr.isOrigin())
				curr.rotateClockWise(v0.theta);
		}

		assert (l.get(0).theta == 0);

		return new Pair(new VsRep(l, zIsVertex), v0.theta);
	}

	/**
	 * Converts the final stack content into the visibility polygon.
	 * @param pre_s Stack content after the algorithm terminated.
	 * @param vs 
	 * @param z Viewpoint which became the origin after shifting the original input polygon during the preprocessing.
	 * @param initAngle Angle corresponding to how much the original input polygon was rotated during the preprocessing.
	 * @return Final visibility polygon in CCW order.
	 */
	private static CCWPolygon postprocess(List<VertDispl> pre_s, VsRep vs, Point2D z, double initAngle) {
		if (vs.zIsVertex)
			pre_s.add(0, new VertDispl(new PolarPoint2D(CommonUtils.origin2D), 0));
		
		// reverse order of stack to establish CCW order of final visibility polygon
		Collections.reverse(pre_s);
		
		// convert VertDispl to PolarPoint2D
		List<PolarPoint2D> rotatedPol = pre_s.stream().map(v -> v.getPoint()).collect(Collectors.toList());
		
		// rotates points back to original position before the rotation in preprocess()
		for (PolarPoint2D curr : rotatedPol) {
			curr.rotateClockWise(-initAngle);
		}
		
		// convert PolarPoint2D to Point2D
		List<Point2D> shiftedPol = rotatedPol.stream().map(v -> v.toCartesian()).collect(Collectors.toList());
		
		// shifts points back to their position before the shift in preprocess()
		for (Point2D curr : shiftedPol)
			curr.setLocation(curr.getX() + z.getX(), curr.getY() + z.getY());
		
		// TODO remove aligned vertices
		
		return new CCWPolygon(shiftedPol);
	}

	
	/*#######################################################################
	 * Now follow subroutines used by advanced, retard, scan and preprocess.
	 */
	
	/**
	  * Computes the intersection between the line segment [a, b] and the window [orig, endpoint]
	  */
	private static VertDispl intersectWithWindow(VertDispl a, VertDispl b, VertDispl orig, VertDispl endpoint) {
		LineSegment s1 = new LineSegment(a.getPoint(), b.getPoint());
		
		Point2D res;
		if (endpoint != null) {
			LineSegment s2 = new LineSegment(orig.getPoint(), endpoint.getPoint());
			res = LineSegment.intersectSegments(s1, s2);
		}
		else {
			Ray2D ray = new Ray2D(orig.getPoint().toCartesian(), orig.getDisplacement());
			res = LineSegment.intersectSegmentWithRay(s1, ray);
		}
		
		return displacementInBetween(new PolarPoint2D(res), a, b);
	}
	
	/**
	 * Computes angular displacement for a point s of a segment between v1 and v2.
	 * @param s Point of a segment.
	 * @param v1 First vertex.
	 * @param v2 Second vertex.
	 * @return Angular displacement between v1 and v2 from the perspective of s.
	 */
	private static VertDispl displacementInBetween(PolarPoint2D s, VertDispl v1, VertDispl v2) {
		double bot = Math.min(v1.getDisplacement(), v2.getDisplacement());
		double top = Math.max(v1.getDisplacement(), v2.getDisplacement());
		
		if (CommonUtils.epsEquals(bot, top))
			return new VertDispl(s, bot);

		
		double temp = s.theta;
		while (CommonUtils.epsGreater(temp, top))	// normalizes the anglex
			temp -= CommonUtils.PI2;

		while (CommonUtils.epsLess(temp, bot))	// normalizes the angle
			temp += CommonUtils.PI2;

		assert (CommonUtils.epsLEQ(bot, temp) && CommonUtils.epsLEQ(temp, top));
		return new VertDispl(s, temp);
	}

	/**
	 * Vertices from the stack are popped until a sj is found that satisfies one of the two conditions from the paper (see Remark 3).
	 * @return New stack and the last popped element.
	 */
	private static Pair<Deque<VertDispl>, VertDispl> locateSj(VertDispl vi, VertDispl vi1, VertDispl sj1,
			List<VertDispl> ss) {
		
		VertDispl sj = ss.get(0);
		List<VertDispl> sTail = ss.subList(1, ss.size());
		
		if (CommonUtils.epsLess(sj.getDisplacement(), vi1.getDisplacement()) && CommonUtils.epsLEQ(vi1.getDisplacement(), sj1.getDisplacement())) {
			return new Pair<Deque<VertDispl>, VertDispl>(new ArrayDeque<>(ss), sj1);
		}
		
		Point2D y = LineSegment.intersectSegments(new LineSegment(vi.getPoint().toCartesian(), vi1.getPoint().toCartesian()), new LineSegment(sj.getPoint().toCartesian(), sj1.getPoint().toCartesian())); 
		
		if (CommonUtils.epsLEQ(vi1.getDisplacement(), sj.getDisplacement()) && 
			CommonUtils.epsLEQ(sj.getDisplacement(), sj1.getDisplacement()) &&
			CommonUtils.epsNEquals(y, sj.getPoint().toCartesian()) && 
			CommonUtils.epsNEquals(y, sj1.getPoint().toCartesian()) && 
			y != null) {
			
			return new Pair<Deque<VertDispl>, VertDispl>(new ArrayDeque<>(ss), sj1);
		}

		return locateSj(vi, vi1, sj, sTail);
	}
	
	/**
	 * Computes the angular displacements.
	 * @param v Vertices whose angular displacement we compute.
	 * @return Vertices and their angles.
	 */
	private static List<VertDispl> computeAngularDisplacements(List<PolarPoint2D> v) {

		// used to store the result, vertices with their corresponding angular
		// displacement
		List<VertDispl> ret = new ArrayList<>();

		for (int i = 0; i < v.size(); i++) {

			if (i == 0) {
				ret.add(new VertDispl(v.get(0), v.get(0).theta));
			} else {
				PolarPoint2D vi = v.get(i);
				PolarPoint2D viprev = v.get(i - 1);
				double phi = vi.theta;
				double rawAngle = Math.abs(phi - viprev.theta);

				assert (rawAngle < CommonUtils.PI2);

				double angle = Math.min(rawAngle, CommonUtils.PI2 - rawAngle);
				int sigma = CommonUtils.pointTurn(CommonUtils.origin2D, viprev.toCartesian(), vi.toCartesian()).toInt();
				double alpha_vi = ret.get(i - 1).getDisplacement() + sigma * angle;

				assert (Math.abs(alpha_vi - ret.get(i - 1).getDisplacement()) < CommonUtils.PI);

				ret.add(new VertDispl(vi, alpha_vi));
			}
		}

		return ret;
	}

	private static void adjustPositionOfz(List<PolarPoint2D> l, boolean zIsVertex, Point2D z) {
		if (zIsVertex) {
			// removes z from list (z is origin because we shifted the polygon)
			boolean temp = l.remove(new PolarPoint2D(0, 0));
			assert (temp);

			l.add(0, new PolarPoint2D(0, 0));
		}
	}

	private static void placeV0First(List<PolarPoint2D> l, PolarPoint2D v0) {
		assert (l.contains(v0));
		/*
		 * List l can't have duplicates since this would imply a self
		 * intersecting polygon and we assume simple polygons as input.
		 */
		assert (l.indexOf(v0) == l.lastIndexOf(v0));

		Collections.rotate(l, -l.indexOf(v0));	// Rotates the elements such that v0 is on index 0.

		assert (l.get(0).equals(v0));
	}

	/**
	 * Computes the initial vertex v0.
	 * @param shiftedPol Shifted input polygon.
	 * @param zIsVertex Determines if the viewpoint z is a vertex of the input polygon.
	 * @return Initial vertex v0.
	 */
	private static PolarPoint2D getInitialVertex(CCWPolygon shiftedPol, boolean zIsVertex) {
		List<LineSegment> es = shiftedPol.getEdges();
		List<Point2D> vs = shiftedPol.getVertices();

		// If z is vertex then take vertex adjacent to z.
		if (zIsVertex) {
			Point2D ret;

			/* Find segment whose endpoint a is origin, pick the adjacent
			   endpoint b. */
			for (LineSegment curr : es) {
				if (CommonUtils.epsEquals(curr.a, CommonUtils.origin2D, CommonUtils.Eps))
					return new PolarPoint2D(curr.b);
			}

		}

		// If z is on an edge, return the vertex next to it.
		for (LineSegment curr : es) {
			if (curr.pointOnSegment(CommonUtils.origin2D, curr))
				return new PolarPoint2D(curr.b);
		}

		// used to store all visible (from z) vertices of the polygon
		List<Point2D> visible = new ArrayList<Point2D>();

		for (Point2D v : vs) {
			if (shiftedPol.visibleFromOrigin(v))
				visible.add(v);
		}

		assert (!visible.isEmpty());

		List<PolarPoint2D> visiblePolar = new ArrayList<>();
		for (Point2D curr : visible)
			visiblePolar.add(new PolarPoint2D(curr));

		PolarPoint2D closestVisibleVertex = visiblePolar.get(0);

		for (PolarPoint2D curr : visiblePolar) {
			if (curr.r < closestVisibleVertex.r && (curr.r > 0 || CommonUtils.epsEquals(curr.r, 0, CommonUtils.Eps)))
				closestVisibleVertex = curr;
		}

		return closestVisibleVertex;
	}

	// Encapsulates vertices, their angular displacements, if the viewpoint is a vertex and n as described in the paper.
	public static class VsRep {
		public List<VertDispl> v;
		public boolean zIsVertex;
		int n;

		public VsRep(List<PolarPoint2D> vs, boolean zIsVertex) {
			this.zIsVertex = zIsVertex;
			n = (zIsVertex) ? vs.size() - 1 : vs.size();

			if (zIsVertex)
				v = computeAngularDisplacements(vs.subList(1, vs.size()));
			else
				v = computeAngularDisplacements(vs);
		}

		public VertDispl get(int i) {
			return v.get(i);
		}

		public int size() {
			return v.size();
		}
	}
}
