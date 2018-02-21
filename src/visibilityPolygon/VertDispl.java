package visibilityPolygon;

/**
 * Encapsulates polar points with their corresponding angular displacement as used in Joe&Simpson's 1985 paper.
 */
public class VertDispl {
	private PolarPoint2D p;
	private double alpha;	// angular displacement
	
	public PolarPoint2D getPoint() {
		return p;
	}
	
	public double getDisplacement() {
		return alpha;
	}
	
	public VertDispl(PolarPoint2D p, double alpha) {
		this.p = p;
		this.alpha = alpha;		
	}
	
	@Override
	public String toString() {
		return "(" + p.toCartesian() + ", " + alpha + ")";
	}

}
