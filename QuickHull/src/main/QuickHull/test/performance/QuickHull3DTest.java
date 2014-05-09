package main.QuickHull.test.performance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import main.QuickHull.Point;
import main.QuickHull.QuickHull;
import main.QuickHull3D.Point3D;
import main.QuickHull3D.QuickHull3D;

import org.junit.Test;

public class QuickHull3DTest extends QuickHullTest {	
	
	@Test
	public void testPointsBestCasePerformance() throws IOException {
		System.out.println("=== testPointsBestCasePerformance ===");

		Point frontBottomLeft = new Point3D(0, 0, 0);
		Point frontBottomRight = new Point3D(10000, 0, 0);
		Point frontTopLeft = new Point3D(0, 10000, 0);
		Point frontTopRight = new Point3D(10000, 10000, 0);

		Point backBottomLeft = new Point3D(0, 0, 10000);
		Point backBottomRight = new Point3D(10000, 0, 10000);
		Point backTopLeft = new Point3D(0, 10000, 10000);
		Point backTopRight = new Point3D(10000, 10000, 10000);

		List<Point> pointList;

		for (int i = 1000; i < pointCap; i *= 2) {

			pointList = new LinkedList<Point>();
			pointList = getRandomPoints(1000, 8000, 1000, 8000, 1000, 8000,
					i - 8);

			pointList.add(frontBottomLeft);
			pointList.add(frontBottomRight);
			pointList.add(frontTopLeft);
			pointList.add(frontTopRight);

			pointList.add(backBottomLeft);
			pointList.add(backBottomRight);
			pointList.add(backTopLeft);
			pointList.add(backTopRight);

			System.out.println(i + " points: " + timeForPoints(pointList, rounds)
					+ " ms");
		}
	}

	@Test
	public void testPointsRandomPerformance() throws FileNotFoundException {
		System.out.println("=== testPointsRandomPerformance ===");
		HashSet<Point> pointList;

		for (int i = 1000; i < pointCap; i *= 2) {
			pointList = getRandomGaussianPoints(i, true);
			System.out.println(i + " points: " + timeForPoints(pointList, rounds)
					+ " ms");
		}
	}

	@Test
	public void testPointsSpherePerformance() throws InterruptedException,
			FileNotFoundException {
		System.out.println("=== testPointsSpherePerformance ===");
		HashSet<Point> pointList = new HashSet<Point>();
		
		for (int i = 1000; i < pointCap; i *= 2) {
			
			pointList = getRandomSphereGaussianPoints(i);

			System.out.print(i + " points: " );
			System.out.println(timeForPoints(pointList, rounds)
					+ " ms");
		}
	}

	protected List<Point> getRandomPoints(int xMin, int rangeX, int yMin,
			int rangeY, int zMin, int rangeZ, int count) {
		List<Point> pointList = new LinkedList<Point>();

		for (int i = 0; i < count; ++i) {
			double tempX = Math.random() * rangeX + xMin;
			double tempY = Math.random() * rangeY + yMin;
			double tempZ = Math.random() * rangeZ + zMin;
			pointList.add(new Point3D(tempX, tempY, tempZ));
		}

		return pointList;
	}

	protected QuickHull createQuickHullObject() {
		return new QuickHull3D();
	}
}
