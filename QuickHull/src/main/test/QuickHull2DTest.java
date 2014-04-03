package main.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import main.Point;
import main.Point2D;
import main.QuickHull;
import main.QuickHull2D;

import org.junit.Test;

public class QuickHull2DTest extends QuickHullTest {

	protected int pointCap = 1000000;
	protected int pointCapHalf = pointCap / 2;

	@Test
	public void testPointsBestCase() throws IOException {

		Point a = new Point2D(-oneTimePointCap, -oneTimePointCap);
		Point b = new Point2D(-oneTimePointCap, oneTimePointCap);
		Point c = new Point2D(oneTimePointCap, oneTimePointCap);
		Point d = new Point2D(oneTimePointCap, -oneTimePointCap);

		List<Point> pointList = getRandomPoints(-oneTimePointCap/2,
				oneTimePointCap, -oneTimePointCap/2, oneTimePointCap,
				oneTimePointCap - 4);

		pointList.add(a);
		pointList.add(b);
		pointList.add(c);
		pointList.add(d);

		List<Point> currentBorderPoints = getBorderAndWriteToFile(pointList,
				"testPointsBestCase");

		assertTrue(currentBorderPoints.size() == 4);
		assertTrue(currentBorderPoints.contains(a));
		assertTrue(currentBorderPoints.contains(b));
		assertTrue(currentBorderPoints.contains(c));
		assertTrue(currentBorderPoints.contains(d));
	}

	@Test
	public void testPointsRandom() throws IOException, InterruptedException {
		List<Point> pointList = new LinkedList<Point>();
		pointList = getRandomGaussianPoints(oneTimePointCap, false);
		getBorderAndWriteToFile(pointList, "testPointsRandom");
	}

	@Test
	public void testPointsCircle() throws InterruptedException,
			FileNotFoundException {
		List<Point> pointList = getRandomSphereGaussianPoints(oneTimePointCap,
				false);

		List<Point> border = getBorderAndWriteToFile(pointList,
				"testPointsCircle");
		assertEquals(pointList.size(), border.size());
	}

	@Test
	public void testPointsBestCasePerformance() throws IOException {
		System.out.println("=== testPointsBestCasePerformance ===");
		
		Point a = new Point2D(-pointCap, -pointCap);
		Point b = new Point2D(-pointCap, pointCap);
		Point c = new Point2D(pointCap, pointCap);
		Point d = new Point2D(pointCap, -pointCap);

		List<Point> pointList = null;

		for (int i = 1000; i < pointCap; i *= 2) {
			pointList = getRandomPoints(-pointCapHalf, pointCap,
					-pointCapHalf, pointCap, i - 4);

			pointList.add(a);
			pointList.add(b);
			pointList.add(c);
			pointList.add(d);

			System.out.println(i + " points: "
					+ timeForPoints(pointList, 10) + " ms");
		}
	}

	@Test
	public void testPointsRandomPerformance() {
		System.out.println("=== testPointsRandomPerformance ===");
		for (int i = 1000; i < pointCap; i *= 2) {
			List<Point> pointList = getRandomGaussianPoints(i, false);
			System.out.println(i + " points: "
					+ timeForPoints(pointList, 10) + " ms");
		}
	}

	@Test
	public void testPointsCirclePerformance() {
		System.out.println("=== testPointsCirclePerformance ===");
		for (int i = 1000; i < pointCapHalf; i *= 2) {
			List<Point> pointList = getRandomSphereGaussianPoints(i, false);
			System.out.println(i + " points: "
					+ timeForPoints(pointList, 10) + " ms");
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
