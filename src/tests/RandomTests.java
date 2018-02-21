package tests;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import visibilityPolygon.Orientation;

public class RandomTests extends Applet {

	public static void main(String[] args) {
		Orientation ori = Orientation.COUNTERCLOCKWISE;
//		ori = Orientation.CLOCKWISE;
		ori = Orientation.COLLINEAR;
		
		if (Orientation.CLOCKWISE == ori)
			System.out.println("works");
	
		System.out.println(5*ori.toInt());
	}
	
	

	private static void stackToList() {
		Deque<Integer> s = new ArrayDeque<>();

		for (int i = 0; i < 5; i++)
			s.push(i);

		System.out.println("stack: " + s);
		System.out.println("first: " + s.getFirst());
		System.out.println("last: " + s.getLast());

		List<Integer> l = new ArrayList<>(s);

		System.out.println("list: " + l);
		System.out.println("first: " + l.get(0));
		System.out.println("last: " + l.get(l.size() - 1));
	}

	private static void listToStack() {
		List<Integer> l = new ArrayList<>();

		for (int i = 0; i < 5; i++)
			l.add(i);

		System.out.println("list: " + l);
		System.out.println("first: " + l.get(0));
		System.out.println("last: " + l.get(l.size() - 1));

		Deque<Integer> s = new ArrayDeque<>(l);

		System.out.println("stack: " + s);
		System.out.println("first: " + s.getFirst());
		System.out.println("last: " + s.getLast());

	}

}
