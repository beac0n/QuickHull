package main.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

import org.junit.Ignore;
import org.junit.Test;

public class QuickHull3DTest extends QuickHullTest {

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

		List<Point> pointList = getRandomPoints(-pointCapHalf, pointCap,
				-pointCapHalf, pointCap, -pointCapHalf, pointCap, pointCap - 8);

		pointList.add(a);
		pointList.add(b);
		pointList.add(c);
		pointList.add(d);

		pointList.add(e);
		pointList.add(f);
		pointList.add(g);
		pointList.add(h);

		Collection<Point> borderPoints = getBorderAndWriteToFile(pointList, "3D_testPointsBestCase");

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
		pointList = getRandomGaussianPoints(pointCap, true);
		getBorderAndWriteToFile(pointList, "3D_testPointsRandom3D");
	}

	@Test
	public void testPointsSphere() throws InterruptedException,
			FileNotFoundException {
		List<Point> pointList = getRandomSphereGaussianPoints(pointCap, true);
		Collection<Point> border = getBorderAndWriteToFile(pointList,
				"3D_testPointsSphere");
		assertTrue(border.containsAll(pointList));
		assertEquals(pointList.size(), border.size());
	}

	@Test
	public void testPointsSpecialSphere() throws InterruptedException,
			FileNotFoundException {
		List<Point> pointList = getRandomSpecialSphereGaussianPoints(pointCap,
				true);
		getBorderAndWriteToFile(pointList, "3D_testPointsSpecialSphere");

	}

	@Test
	public void test4Points() {
		Point a = new Point3D(-pointCap, -pointCap, -pointCap);
		Point b = new Point3D(pointCap, pointCap, -pointCap);
		Point c = new Point3D(-pointCap, pointCap, -pointCap);
		Point d = new Point3D(pointCap, -pointCap, -pointCap);

		List<Point> pointList = new ArrayList<Point>();

		pointList.add(a);
		pointList.add(b);
		pointList.add(c);
		pointList.add(d);

		QuickHull3D qh3d = new QuickHull3D();
		Collection<Point> bps = qh3d.getBorderPoints(pointList);

		assertEquals(pointList.size(), bps.size());
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
