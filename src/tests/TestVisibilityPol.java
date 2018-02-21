package tests;

import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import visibilityPolygon.CCWPolygon;
import visibilityPolygon.CommonUtils;
import visibilityPolygon.VisibilityPolygon;

 public class TestVisibilityPol {

	@Test
	public void interiorConvex() {
		// z interior, polygon convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(4, 4);

		CCWPolygon VP = VisibilityPolygon.computeVisPol(pol, z);
		
//		for (Point2D curr : VP.vertices)
//			System.out.println(curr);
		
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(0), new Point2D.Double(4, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(1), new Point2D.Double(2, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(2), new Point2D.Double(6, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(3), new Point2D.Double(6, 6), CommonUtils.Eps));
		
		assertTrue(VP.getVertices().size() == 4);
	}
	
	@Test
	public void interiorNotConvex() {

		// z interior, polygon not convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(4, 4);

		CCWPolygon VP = VisibilityPolygon.computeVisPol(pol, z);
		
//		for (Point2D curr : VP.vertices)
//			System.out.println(curr);

		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(0), new Point2D.Double(4, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(1), new Point2D.Double(1, 4), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(2), new Point2D.Double(-2, 4), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(3), new Point2D.Double(-2, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(4), new Point2D.Double(6, 2), CommonUtils.Eps));
		
		assertTrue(VP.getVertices().size() == 5);
	}
	
	@Test
	public void onEdgeConvex() {
		// z on edge of boundary, polygon is convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(4, 2);
		
		CCWPolygon VP = VisibilityPolygon.computeVisPol(pol, z);
		
//		for (Point2D curr : VP.vertices)
//			System.out.println(curr);

		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(0), new Point2D.Double(6, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(1), new Point2D.Double(6, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(2), new Point2D.Double(4, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(3), new Point2D.Double(2, 2), CommonUtils.Eps));
		
		assertTrue(VP.getVertices().size() == 4);
	}
	
	@Test
	public void onEdgeNotConvex() {

		// z is on edge of boundary, polygon is not convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(-2, 3);

		CCWPolygon VP = VisibilityPolygon.computeVisPol(pol, z);
		
//		for (Point2D curr : VP.vertices)
//			System.out.println(curr);
		
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(0), new Point2D.Double(-2, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(1), new Point2D.Double(6, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(2), new Point2D.Double(4.428571428571428, 5.142857142857142), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(3), new Point2D.Double(1, 4), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(4), new Point2D.Double(-1, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(5), new Point2D.Double(-2, 4), CommonUtils.Eps));
		
		assertTrue(VP.getVertices().size() == 6);
	}
	
	@Test
	public void onVertexIsConvex() {
		// z is on boundary (polygon vertex), polygon is convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(6, 2);

		CCWPolygon VP = VisibilityPolygon.computeVisPol(pol, z);
		
//		for (Point2D curr : VP.vertices)
//			System.out.println(curr);
		
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(0), new Point2D.Double(6, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(1), new Point2D.Double(4, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(2), new Point2D.Double(2, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(3), new Point2D.Double(6, 2), CommonUtils.Eps));
		
		assertTrue(VP.getVertices().size() == 4);
	}
	
	@Test
	public void onVertexNotConvex() {

		// z is on boundary (polygon vertex), polygon is not convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(1, 4);

		CCWPolygon VP = VisibilityPolygon.computeVisPol(pol, z);
		
//		for (Point2D curr : VP.vertices)
//			System.out.println(curr);
		
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(0), new Point2D.Double(-1, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(1), new Point2D.Double(-2, 4), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(2), new Point2D.Double(-2, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(3), new Point2D.Double(6, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(4), new Point2D.Double(4, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(VP.getVertices().get(5), new Point2D.Double(1, 4), CommonUtils.Eps));
		
		assertTrue(VP.getVertices().size() == 6);
	}
	

}
