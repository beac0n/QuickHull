package main.testperformance;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import main.Point;
import main.Point2D;
import main.QuickHull;
import main.QuickHull2D;

import org.junit.Test;

public class QuickHull2DTest extends QuickHullTest {

	protected int pointCap = 4000000;
	
	@Test
	public void testPointsBestCasePerformance() throws IOException {
		System.out.println("=== testPointsBestCasePerformance ===");

		Point a = new Point2D(-pointCap, -pointCap);
		Point b = new Point2D(-pointCap, pointCap);
		Point c = new Point2D(pointCap, pointCap);
		Point d = new Point2D(pointCap, -pointCap);

		List<Point> pointList = null;

		for (int i = 1000; i < pointCap; i *= 2) {
			pointList = getRandomPoints(-pointCapHalf, pointCap, -pointCapHalf,
					pointCap, i - 4);

			pointList.add(a);
			pointList.add(b);
			pointList.add(c);
			pointList.add(d);

			System.out.println(i + " points: " + timeForPoints(pointList, 10)
					+ " ms");
		}
	}

	@Test
	public void testPointsRandomPerformance() {
		System.out.println("=== testPointsRandomPerformance ===");
		for (int i = 1000; i < pointCap; i *= 2) {
			List<Point> pointList = getRandomGaussianPoints(i, false);
			System.out.println(i + " points: " + timeForPoints(pointList, 10)
					+ " ms");
		}
	}

	@Test
	public void testPointsCirclePerformance() {
		System.out.println("=== testPointsCirclePerformance ===");
		for (int i = 1000; i < pointCap; i *= 2) {
			List<Point> pointList = getRandomSphereGaussianPoints(i, false);
			System.out.println(i + " points: " + timeForPoints(pointList, 10)
					+ " ms");
		}
	}

	protected QuickHull createQuickHullObject() {
		return new QuickHull2D();
	}

	protected List<Point> getRandomPoints(int xMin, int rangeX, int yMin,
			int rangeY, int count) {
		List<Point> pointList = new LinkedList<Point>();

		for (int i = 0; i < count; ++i) {
			double tempX = Math.random() * rangeX + xMin;
			double tempY = Math.random() * rangeY + yMin;

			pointList.add(new Point2D(tempX, tempY));
		}

		return pointList;
	}
}
