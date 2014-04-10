package main.QuickHull.test.performance;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import main.QuickHull.Point;
import main.QuickHull.QuickHull;
import main.QuickHull2D.Point2D;
import main.QuickHull2D.QuickHull2D;

import org.junit.Test;

public class QuickHull2DTest extends QuickHullTest {
	@Test
	public void testPointsBestCasePerformance() throws IOException {
		System.out.println("=== testPointsBestCasePerformance ===");

		int pointCapHalf = pointCap / 2;

		Point a = new Point2D(-pointCap, -pointCap);
		Point b = new Point2D(-pointCap, pointCap);
		Point c = new Point2D(pointCap, pointCap);
		Point d = new Point2D(pointCap, -pointCap);

		for (int i = 1000; i < pointCap; i *= 2) {
			HashSet<Point> pointList = getRandomPoints(-pointCapHalf, pointCap,
					-pointCapHalf, pointCap, i - 4);

			pointList.add(a);
			pointList.add(b);
			pointList.add(c);
			pointList.add(d);

			System.out.println(i + " points: "
					+ timeForPoints(pointList, rounds) + " ms");
		}
	}

	@Test
	public void testPointsRandomPerformance() {
		System.out.println("=== testPointsRandomPerformance ===");
		for (int i = 1000; i < pointCap; i *= 2) {
			HashSet<Point> pointList = getRandomGaussianPoints(i, false);
			System.out.println(i + " points: "
					+ timeForPoints(pointList, rounds) + " ms");
		}
	}

	@Test
	public void testPointsCirclePerformance() {
		System.out.println("=== testPointsCirclePerformance ===");

		for (int i = 1000; i < pointCap; i *= 2) {
			List<Point> pointList = new LinkedList<Point>();

			for (double j = 0; j < 2 * Math.PI; j += (2 * Math.PI) / i) {
				double x = Math.cos(j);
				double y = Math.sin(j);

				pointList.add(new Point2D(x, y));
			}
			System.out.println(i + " points: "
					+ timeForPoints(pointList, rounds) + " ms");
		}
	}

	protected QuickHull createQuickHullObject() {
		return new QuickHull2D();
	}

	protected HashSet<Point> getRandomPoints(int xMin, int rangeX, int yMin,
			int rangeY, int count) {
		HashSet<Point> pointList = new HashSet<Point>();

		for (int i = 0; i < count; ++i) {
			double tempX = Math.random() * rangeX + xMin;
			double tempY = Math.random() * rangeY + yMin;

			pointList.add(new Point2D(tempX, tempY));
		}

		return pointList;
	}
}
