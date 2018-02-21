package visibilityPolygon;

import java.awt.geom.Point2D;

public class CommonUtils {

	// constants
	static double PI = Math.PI;
	static double PI2 = 2 * PI;
	public static double Eps = 0.0000000001;
	static Point2D origin2D = new Point2D.Double(0, 0);

	/** Checks if a and b are approximately equal to account for floating point precision limitations. 
	 * 
	 * For more information see: https://randomascii.wordpress.com/2012/02/25/comparing-floating-point-numbers-2012-edition/
	 * @param a First double.
	 * @param b Second double.
	 * @param epsilon Tolerance.
	 * @return true iff. the absolute difference between a and b is strictly less than epsilon.
	 */
	public static boolean epsEquals(double a, double b, double epsilon) {
		return a == b ? true : Math.abs(a - b) < epsilon;
	}
	
	public static boolean epsEquals(double a, double b) {
		return a == b ? true : Math.abs(a - b) < Eps;
	}
	
	// epsilon not equals
	public static boolean epsEquals(Point2D a, Point2D b, double epsilon) {
		return epsEquals(a.getX(), b.getX(), epsilon) && epsEquals(a.getY(), b.getY(), epsilon);
	}
	
	public static boolean epsEquals(Point2D a, Point2D b) {
		return epsEquals(a.getX(), b.getX(), Eps) && epsEquals(a.getY(), b.getY(), Eps);
	}
	
	public static boolean epsNEquals(double a, double b) {
		return !epsEquals(a, b);
	}
	
	public static boolean epsNEquals(Point2D a, Point2D b) {
		return !epsEquals(a, b);
	}
	
	// epsilon greater or equal
	public static boolean epsGEQ(double a, double b, double epsilon) {
		return epsEquals(a, b, epsilon) || (a > b);
	}
	
	public static boolean epsGEQ(double a, double b) {
		return epsEquals(a, b, Eps) || (a > b);
	}
	
	// epsilon less or equal
	public static boolean epsLEQ(double a, double b) {
		return epsEquals(a, b, Eps) || (a < b);
	}
	
	// epsilon strictly greater
	public static boolean epsGreater(double a, double b) {
		return a > b && epsNEquals(a, b);
	}
	
	// epsilon strictly less
	public static boolean epsLess(double a, double b) {
		return a < b && epsNEquals(a, b);
	}
	
		
//	  /**
//    * Turn between three points
//    * +1 -> Counterclockwise (left) turn
//    * -1 -> Clockwise (right) turn
//    * 0 -> points are collinear
//    * 
//    */
	// Determines the turn orientation between three given points.
	public static Orientation pointTurn(Point2D x, Point2D y, Point2D z) {
		int sign = (int) Math.signum(crossProduct(x, y, z));
		
		switch (sign) {
		case 1:
			return Orientation.COUNTERCLOCKWISE;
		case -1:
			return Orientation.CLOCKWISE;
		case 0:
			return Orientation.COLLINEAR;
		default:
			assert(false); // unreachable
			return null;
		}
	}
	 
	// returns magnitude of the resulting vector
	public static double crossProduct(Point2D a, Point2D b, Point2D c) {
		return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
	}
	
	public static double squaredLengthBA(Point2D a, Point2D b) {
		return (b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() - a.getY());
	}
	
	public static double dotProduct(Point2D a, Point2D b, Point2D c) {
		return (c.getX() - a.getX()) * (b.getX() - a.getX()) + (c.getY() - a.getY()) * (b.getY() - a.getY());
	}
	 
	public static class Pair<F, S> {
		public F first;
		public S second; 

		public Pair(F first, S second) {
			this.first = first;
			this.second = second;
		}
	}
	
	public static class Ray2D {
		Point2D origin;
		double phi;
		
		public Ray2D(Point2D origin, double phi) {
			this.origin = origin;
			this.phi = phi;			
		}
		
	}
	
	public static class Direction {
		double dx, dy;
		
		public Direction(double dx, double dy) {
			this.dx = dx;
			this.dy = dy;
		}
		
		public void scaleBy(double k) {
			dx *= k;
			dy *= k;
		}
		
		public double crossProd(Direction d) {
			return dx * d.dy - dy * d.dx;
		}
		
		public double crossProd(Point2D p) {
			return dx * p.getY() - dy * p.getX();
		}
		
		public double scalarProd(Direction d) {
			return dx * d.dx + dy * d.dy;
		}
	}
	  
}
