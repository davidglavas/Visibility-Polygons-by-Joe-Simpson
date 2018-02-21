package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import visibilityPolygon.CCWPolygon;
import visibilityPolygon.CommonUtils;
import visibilityPolygon.VisibilityPolygon;
import visibilityPolygon.CommonUtils.Pair;
import visibilityPolygon.VisibilityPolygon.VsRep;

public class TestPreprocessing {

	@Test
	public void testPreprocess1() {

		// z interior, polygon convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(4, 4);

//		Pair<VsRep, Double> t = VisibilityPolygon.preprocess(pol, z);
		Pair<VsRep, Double> t = accessPrivatePreprocess(pol, z);

		VsRep v = t.first;
		double initialAngle = t.second;

		assertTrue(CommonUtils.epsEquals(v.get(0).getPoint().toCartesian(), new Point2D.Double(2, 0), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(1).getPoint().toCartesian(), new Point2D.Double(-2, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(2).getPoint().toCartesian(), new Point2D.Double(-2, -2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(3).getPoint().toCartesian(), new Point2D.Double(2, -2), CommonUtils.Eps));

		assertTrue(v.v.size() == 4);
		assert (v.get(0).getDisplacement() == 0);

		/*
		 * Outputs angular displacements System.out.println("First: "); for (int
		 * i = 0; i < v.v.size(); i++) System.out.println(v.get(i).alpha);
		 */

		assertTrue(CommonUtils.epsEquals(initialAngle, 1.5707963267948966));
	}

	@Test
	public void testPreprocess2() {

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

//		Pair<VsRep, Double> t = VisibilityPolygon.preprocess(pol, z);
		Pair<VsRep, Double> t = accessPrivatePreprocess(pol, z);

		VsRep v = t.first;
		double initialAngle = t.second;

		assertTrue(CommonUtils.epsEquals(v.get(0).getPoint().toCartesian(), new Point2D.Double(2, 0), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(1).getPoint().toCartesian(), new Point2D.Double(0, 3), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(2).getPoint().toCartesian(), new Point2D.Double(2, 5), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(3).getPoint().toCartesian(), new Point2D.Double(0, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(4).getPoint().toCartesian(), new Point2D.Double(-2, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(5).getPoint().toCartesian(), new Point2D.Double(-2, -2), CommonUtils.Eps));

		assertTrue(v.v.size() == 6);
		assertTrue(v.get(0).getDisplacement() == 0);

		/*
		 * Outputs angular displacements System.out.println("Second: "); for
		 * (int i = 0; i < v.v.size(); i++) System.out.println(v.get(i).alpha);
		 */

		assertTrue(CommonUtils.epsEquals(initialAngle, 1.5707963267948966));
	}

	@Test
	public void testPreprocess3() {
		// z boundary edge, polygon convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(4, 2);

//		Pair<VsRep, Double> t = VisibilityPolygon.preprocess(pol, z);
		Pair<VsRep, Double> t = accessPrivatePreprocess(pol, z);

		VsRep v = t.first;
		double initialAngle = t.second;

		assertTrue(CommonUtils.epsEquals(v.get(0).getPoint().toCartesian(), new Point2D.Double(2, 0), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(1).getPoint().toCartesian(), new Point2D.Double(2, 4), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(2).getPoint().toCartesian(), new Point2D.Double(0, 4), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(3).getPoint().toCartesian(), new Point2D.Double(-2, 0), CommonUtils.Eps));

		assertTrue(v.v.size() == 4);
		assertTrue(v.get(0).getDisplacement() == 0);

		/*
		 * Outputs angular displacements System.out.println("First: "); for (int
		 * i = 0; i < v.v.size(); i++) System.out.println(v.get(i).alpha);
		 */

		assertTrue(CommonUtils.epsEquals(initialAngle, 0));
	}

	@Test
	public void testPreprocess4() {

		// z boundary edge, polygon not convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(-2, 3);

//		Pair<VsRep, Double> t = VisibilityPolygon.preprocess(pol, z);
		Pair<VsRep, Double> t = accessPrivatePreprocess(pol, z);

		VsRep v = t.first;
		double initialAngle = t.second;

		assertTrue(CommonUtils.epsEquals(v.get(0).getPoint().toCartesian(), new Point2D.Double(1, 0), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(1).getPoint().toCartesian(), new Point2D.Double(1, 8), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(2).getPoint().toCartesian(), new Point2D.Double(-3, 6), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(3).getPoint().toCartesian(), new Point2D.Double(-1, 3), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(4).getPoint().toCartesian(), new Point2D.Double(-3, 1), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(5).getPoint().toCartesian(), new Point2D.Double(-1, 0), CommonUtils.Eps));

		assertTrue(v.v.size() == 6);
		assertTrue(v.get(0).getDisplacement() == 0);

		/*
		 * Outputs angular displacements System.out.println("Second: "); for
		 * (int i = 0; i < v.v.size(); i++) System.out.println(v.get(i).alpha);
		 */

		assertTrue(CommonUtils.epsEquals(initialAngle, -1.5707963267948966));
	}

	@Test
	public void testPreprocess5() {
		// z on boundary (polygon vertex), polygon convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(6, 6));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(2, 2));
		vertices.add(new Point2D.Double(6, 2));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(6, 2);

//		Pair<VsRep, Double> t = VisibilityPolygon.preprocess(pol, z);
		Pair<VsRep, Double> t = accessPrivatePreprocess(pol, z);
		
		VsRep v = t.first;
		double initialAngle = t.second;

		assertTrue(CommonUtils.epsEquals(v.get(0).getPoint().toCartesian(), new Point2D.Double(4, 0), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(1).getPoint().toCartesian(), new Point2D.Double(4, 2), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(2).getPoint().toCartesian(), new Point2D.Double(0, 4), CommonUtils.Eps));
		// assertTrue(CommonUtils.epsEquals(v.get(3).p.toCartesian(), new
		// Point2D.Double(0, 0), CommonUtils.Eps));

		// for (VertDispl curr : v.v)
		// System.out.println(curr.p.toCartesian());

		assertTrue(v.v.size() == 3);
		assertTrue(v.get(0).getDisplacement() == 0);

		/*
		 * Outputs angular displacements System.out.println("First: "); for (int
		 * i = 0; i < v.v.size(); i++) System.out.println(v.get(i).alpha);
		 */

		assertTrue(CommonUtils.epsEquals(initialAngle, 1.5707963267948966)); // 90
																				// degrees
	}

	@Test
	public void testPreprocess6() {

		// z on boundary (polygon vertex), polygon is not convex
		List<Point2D> vertices = new ArrayList<>();
		vertices.add(new Point2D.Double(-2, 2));
		vertices.add(new Point2D.Double(6, 2));
		vertices.add(new Point2D.Double(4, 6));
		vertices.add(new Point2D.Double(1, 4));
		vertices.add(new Point2D.Double(-1, 6));
		vertices.add(new Point2D.Double(-2, 4));

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(1, 4);

//		Pair<VsRep, Double> t = VisibilityPolygon.preprocess(pol, z);
		Pair<VsRep, Double> t = accessPrivatePreprocess(pol, z);
		
		
		VsRep v = t.first;
		double initialAngle = t.second;

		assertTrue(CommonUtils.epsEquals(v.get(0).getPoint().toCartesian(), new Point2D.Double(2.8284271247461903, 0),
				CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(1).getPoint().toCartesian(),
				new Point2D.Double(2.121320343559643, 2.121320343559643), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(2).getPoint().toCartesian(),
				new Point2D.Double(0.7071067811865467, 3.5355339059327378), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(3).getPoint().toCartesian(),
				new Point2D.Double(-4.949747468305833, -2.1213203435596415), CommonUtils.Eps));
		assertTrue(CommonUtils.epsEquals(v.get(4).getPoint().toCartesian(),
				new Point2D.Double(-0.7071067811865467, -3.5355339059327378), CommonUtils.Eps));

		assertTrue(v.size() == 5);
		assertTrue(v.get(0).getDisplacement() == 0);

		/*
		 * for (int i = 0; i < v.v.size(); i++) System.out.println("cart: " +
		 * v.get(i).p.toCartesian() + ", polar: " + v.get(i).p);
		 * 
		 * 
		 * Outputs angular displacements System.out.println("Second: "); for
		 * (int i = 0; i < v.v.size(); i++) System.out.println(v.get(i).alpha);
		 */

		assertTrue(CommonUtils.epsEquals(initialAngle, 2.356194490192345)); // 135
																			// degrees
	}

	@Test
	public void testPreprocessJMN() {

		// data challenge 3
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

		CCWPolygon pol = new CCWPolygon(vertices);
		Point2D z = new Point2D.Double(3, 5);

		
		// Pair<VsRep, Double> t = VisibilityPolygon.preprocess(pol, z);
		Pair<VsRep, Double> t = accessPrivatePreprocess(pol, z);

		VsRep v = t.first;
		double initialAngle = t.second;

		// assertTrue(CommonUtils.epsEquals(v.get(0).p.toCartesian(), new
		// Point2D.Double(2.8284271247461903, 0), CommonUtils.Eps));
		// assertTrue(CommonUtils.epsEquals(v.get(1).p.toCartesian(), new
		// Point2D.Double(2.121320343559643, 2.121320343559643),
		// CommonUtils.Eps));
		// assertTrue(CommonUtils.epsEquals(v.get(2).p.toCartesian(), new
		// Point2D.Double(0.7071067811865467, 3.5355339059327378),
		// CommonUtils.Eps));
		// assertTrue(CommonUtils.epsEquals(v.get(3).p.toCartesian(), new
		// Point2D.Double(-4.949747468305833, -2.1213203435596415),
		// CommonUtils.Eps));
		// assertTrue(CommonUtils.epsEquals(v.get(4).p.toCartesian(), new
		// Point2D.Double(-0.7071067811865467, -3.5355339059327378),
		// CommonUtils.Eps));
		//
		// assertTrue(v.v.size() == 5);
		// assertTrue(v.get(0).alpha == 0);

//		for (int i = 0; i < v.v.size(); i++)
//			System.out.println("cart: " + v.get(i).p.toCartesian() + ", polar: " + v.get(i).p);

		/*
		 * Outputs angular displacements System.out.println("Second: "); for
		 * (int i = 0; i < v.v.size(); i++) System.out.println(v.get(i).alpha);
		 */

		// assertTrue(CommonUtils.epsEquals(initialAngle, 2.356194490192345));
		// // 135 degrees
	}
	
	// access preprocess via reflection api
	private Pair<VsRep, Double> accessPrivatePreprocess(CCWPolygon pol, Point2D z) {
		Pair<VsRep, Double> t = null;
		// reflection to test private method
		try {
			Class[] paramTypes = { CCWPolygon.class, Point2D.class };
			Method method = VisibilityPolygon.class.getDeclaredMethod("preprocess", paramTypes);
			method.setAccessible(true);

			t = (Pair<VsRep, Double>) method.invoke(null, pol, z);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return t;
	}
}
