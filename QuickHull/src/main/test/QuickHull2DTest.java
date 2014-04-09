package main.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import main.Point;
import main.Point2D;
import main.QuickHull;
import main.QuickHull2D;

import org.junit.Test;

public class QuickHull2DTest extends QuickHullTest {

	protected String outFilePre = "_";

	@Test
	public void testPointsBestCase() throws IOException {

		Point a = new Point2D(-pointCap, -pointCap);
		Point b = new Point2D(-pointCap, pointCap);
		Point c = new Point2D(pointCap, pointCap);
		Point d = new Point2D(pointCap, -pointCap);

		List<Point> pointList = getRandomPoints(-pointCap / 2, pointCap,
				-pointCap / 2, pointCap, pointCap - 4);

		pointList.add(a);
		pointList.add(b);
		pointList.add(c);
		pointList.add(d);

		Collection<Point> currentBorderPoints = getBorderAndWriteToFile(
				pointList, "2D" + outFilePre + "testPointsBestCase");

		assertEquals(4, currentBorderPoints.size());
		assertTrue(currentBorderPoints.contains(a));
		assertTrue(currentBorderPoints.contains(b));
		assertTrue(currentBorderPoints.contains(c));
		assertTrue(currentBorderPoints.contains(d));
	}

	@Test
	public void testPointsRandom() throws IOException, InterruptedException {
		List<Point> pointList = new LinkedList<Point>();
		pointList = getRandomGaussianPoints(pointCap, false);
		getBorderAndWriteToFile(pointList, "2D" + outFilePre
				+ "testPointsRandom");
	}

	@Test
	public void testPointsCircle() throws InterruptedException,
			FileNotFoundException {
		List<Point> pointList = new LinkedList<Point>();

		for (double i = 0; i < 2 * Math.PI; i += (2 * Math.PI) / pointCap) {
			double x = Math.cos(i);
			double y = Math.sin(i);

			pointList.add(new Point2D(x, y));
		}

		Collection<Point> border = getBorderAndWriteToFile(pointList, "2D"
				+ outFilePre + "testPointsCircle");
		assertEquals(pointList.size(), border.size());
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
