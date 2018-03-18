# Joe and Simpson's Visibility Polygon Algorithm
An implementation of Joe and Simpson's [visibility polygon algorithm](https://cs.uwaterloo.ca/research/tr/1985/CS-85-38.pdf).

It solves the following problem in O(n) time and space which has been shown to be asymptotically optimal.

**Problem Statement.** Given a viewpoint `z` inside of a simple polygon `P` with `n` vertices, we want to compute the visibility polygon `VP(P, z)`, which consists of all points in `P` visible from the viewpoint `z`. We say that point `p` is visible from point `q` (and conversely, `q` is visible from `p`) if and only if the line segment `pq` lies completely in `P`.

### Remarks:
- General position is not assumed meaning that inputs with three collinear points and/or four cocircular points are handled correctly.
- Viewpoints can lie in the polygon's interior, on an edge, or on a vertex.
- The polygon has to be [simple](https://en.wikipedia.org/wiki/Simple_polygon), the algorithm doesn't work with "obstacles" inside of the polygon.
- This implementation is for educational purposes only, production level code can be found at [CGAL](https://doc.cgal.org/latest/Visibility_2/classCGAL_1_1Simple__polygon__visibility__2.html).
- See my [blogpost](http://davidglavas.me/) for more information.

### Usage example:

``` java
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
```
