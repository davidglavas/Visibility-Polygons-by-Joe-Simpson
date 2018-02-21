package visibilityPolygon;

public enum Orientation {
	COUNTERCLOCKWISE, CLOCKWISE, COLLINEAR;
	
	/* Converts turn to int representation
    * 1 -> counterclockwise turn (left)
    * -1 -> clockwise turn (right)
    * 0 -> points are collinear
    */
	public int toInt() {
		if (this == Orientation.COUNTERCLOCKWISE)
			return 1;
		else if (this == Orientation.CLOCKWISE)
			return -1;
		else
			return 0;	// collinear
	}
}
