package main.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.Point;
import main.Point3D;
import main.QuickHull;
import main.QuickHull3D;

import org.junit.Test;

public class QuickHull3DTest extends QuickHullTest {

	private static int pointCap = 50000;
	private static int pointCapHalf = pointCap/2;

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

			System.out.println(i + " points: "
					+ timeForPoints(pointList, 20) + " ms");

		}

	}

	@Test
	public void testPointsRandomPerformance() throws FileNotFoundException {
		System.out.println("=== testPointsRandomPerformance ===");
		List<Point> pointList;

		for (int i = 1000; i < pointCap; i *= 2) {
			pointList = new LinkedList<Point>();
			pointList = getRandomGaussianPoints(i, true);
			System.out.println(i + " points: "
					+ timeForPoints(pointList, 20) + " ms");
		}
	}

	@Test
	public void testPointsSpherePerformance() throws InterruptedException, FileNotFoundException {
		System.out.println("=== testPointsSpherePerformance ===");
		List<Point> pointList = new ArrayList<Point>();

		Random rand = new Random();

		for (int i = 5; i < 100001; i *= 2) {
			for (int j = 0; j < i; ++j) {
				double tempX = rand.nextGaussian();
				double tempY = rand.nextGaussian();
				double tempZ = rand.nextGaussian();

				double vectorLength = Math.sqrt(tempX * tempX + tempY * tempY
						+ tempZ * tempZ);

				tempX = tempX / vectorLength;
				tempY = tempY / vectorLength;
				tempZ = tempZ / vectorLength;

				Point tempPoint = new Point3D(tempX, tempY, tempZ);

				if (!pointList.contains(tempPoint)) {
					pointList.add(tempPoint);
				}
			}

			System.out.println(i + " points: "
					+ timeForPoints(pointList, 20) + " ms");
		}
	}

	@Test
	public void testPointsBestCase() throws IOException {
		
		Point a = new Point3D(-pointCap, -pointCap, -pointCap);
		Point b = new Point3D(pointCap, pointCap, -pointCap);
		Point c = new Point3D(-pointCap, pointCap, -pointCap);
		Point d = new Point3D(pointCap, -pointCap, -pointCap);

		Point e = new Point3D(-pointCap, -pointCap, pointCap);
		Point f = new Point3D(pointCap, pointCap, pointCap);
		Point g = new Point3D(-pointCap, pointCap, pointCap);
		Point h = new Point3D(pointCap, -pointCap, pointCap);

		List<Point> pointList 
		= getRandomPoints(-pointCapHalf, pointCap, -pointCapHalf, pointCap, -pointCapHalf,
				pointCap, pointCap - 8);

		pointList.add(a);
		pointList.add(b);
		pointList.add(c);
		pointList.add(d);

		pointList.add(e);
		pointList.add(f);
		pointList.add(g);
		pointList.add(h);

		QuickHull3D qh3d = new QuickHull3D();
		
		List<Point> borderPoints = qh3d.getBorderPoints(pointList);
	//= getBorderAndWriteToFile(pointList, "testBestCase3D");
		
		assertEquals(8, borderPoints.size());
		assertTrue(borderPoints.contains(a));
		assertTrue(borderPoints.contains(b));
		assertTrue(borderPoints.contains(c));
		assertTrue(borderPoints.contains(d));

		assertTrue(borderPoints.contains(e));
		assertTrue(borderPoints.contains(f));
		assertTrue(borderPoints.contains(g));
		assertTrue(borderPoints.contains(h));
	}

	@Test
	public void testPointsRandom() throws IOException, InterruptedException {
		List<Point> pointList = new LinkedList<Point>();
		pointList = getRandomGaussianPoints(oneTimePointCap, true);
		getBorderAndWriteToFile(pointList, "testPointsRandom3D");
	}

	@Test
	public void testPointsSphere() throws InterruptedException,
			FileNotFoundException {
		List<Point> pointList = getRandomSphereGaussianPoints(pointCap, true);
		List<Point> border = getBorderAndWriteToFile(pointList,
				"testPointsSphere");
		assertTrue(border.containsAll(pointList));
		assertEquals(pointList.size(), border.size());
	}
	
	@Test
	public void testPointsSpecialSphere() throws InterruptedException,
			FileNotFoundException {
		List<Point> pointList = getRandomSpecialSphereGaussianPoints(pointCap, true);
		getBorderAndWriteToFile(pointList, "testPointsSpecialSphere");
		
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
