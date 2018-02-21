package visibilityPolygon;

import java.awt.geom.Point2D;
import java.util.Objects;

public class PolarPoint2D {
	double r; // distance to p
	double theta; // angle with respect to p

	// polar point from distance and angle (in radians)
	public PolarPoint2D(double r, double theta) {
		this.r = r;
		this.theta = theta;
	}

	// initlialize polar point from cartesian point
	public PolarPoint2D(Point2D p) {
		PolarPoint2D temp = cartesianToPolar(p);
		r = temp.r;
		theta = temp.theta;
	}

	public Point2D toCartesian() {
		return polarToCartesian(this);
	}

	private PolarPoint2D cartesianToPolar(Point2D p) {
		double r = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
		double theta = Math.atan2(p.getY(), p.getX());

		return new PolarPoint2D(r, theta);
	}

	private Point2D.Double polarToCartesian(PolarPoint2D p) {
		double x = Math.cos(p.theta) * p.r;
		double y = Math.sin(p.theta) * p.r;

		return new Point2D.Double(x, y);
	}

	public boolean isOrigin() {
		return CommonUtils.epsEquals(r, 0);
	}

	public void rotateClockWise(double theta) {
		this.theta -= theta;
		normalize(2 * Math.PI);
	}

	// keeps theta in [0, period]
	public void normalize(double period) {
		while (theta <= 0.0)
			theta += period;

		while (theta >= period)
			theta -= period;
	}

	// True iff. PolarPoints have equal r and theta
	@Override
	public boolean equals(Object o) {
		// self check
		if (this == o)
			return true;
		// null check
		if (o == null)
			return false;
		// type check and cast
		if (getClass() != o.getClass())
			return false;
		
		PolarPoint2D p = (PolarPoint2D) o;
		// field comparison
		return CommonUtils.epsEquals(r, p.r, CommonUtils.Eps) && CommonUtils.epsEquals(theta, p.theta, CommonUtils.Eps);
	}
	
	@Override
	public int hashCode() {
	    int hash = 3;
//	    int rHash = (int)(Double.doubleToLongBits(r) ^ (Double.doubleToLongBits(r) >>> 32));
//	    int thetaHash = (int)(Double.doubleToLongBits(theta) ^ (Double.doubleToLongBits(theta) >>> 32));
	    hash = 53 * hash + Double.hashCode(r) + Double.hashCode(theta);
	    return hash;
	}

	@Override
	public String toString() {
		return "[" + r + ", " + theta + "]";
	}
}
