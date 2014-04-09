package main.testperformance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.Point;
import main.Point3D;
import main.QuickHull;

public abstract class QuickHullTest {

	protected int pointCap = 200000;

	protected abstract QuickHull createQuickHullObject();

	protected double timeForPoints(List<Point> pointList, int repeat) {
		double overallTime = 0;
		for (int i = 0; i < repeat; ++i) {
			double currTime = measureTimeOnce(pointList);
			overallTime += currTime;
		}
		return overallTime / (double) repeat;
	}

	protected double measureTimeOnce(List<Point> pointList) {
		long startTime = System.nanoTime();

		QuickHull qh = createQuickHullObject();
		qh.getBorderPoints(pointList);

		long stopTime = System.nanoTime();
		long elapsedTime = stopTime - startTime;

		return elapsedTime / 1000000;
	}

	protected List<Point> getRandomSphereGaussianPoints(long pointsCount,
			boolean is3D) {
		Set<Point> pointList = new HashSet<Point>();

		Random rand = new Random();

		for (int i = 0; i < pointsCount; ++i) {
			double tempX = rand.nextGaussian() * 10000;
			double tempY = rand.nextGaussian() * 10000;
			double tempZ = 1.0;
			if (is3D)
				tempZ = rand.nextGaussian() * 10000;

			double len = Math.sqrt(tempX * tempX + tempY * tempY);
			if (is3D)
				len = Math.sqrt(tempX * tempX + tempY * tempY + tempZ * tempZ);

			tempX /= len;
			tempY /= len;
			if (is3D)
				tempZ /= len;

			pointList.add(new Point3D(tempX, tempY, tempZ));
		}

		List<Point> retVal = new ArrayList<Point>();
		retVal.addAll(pointList);

		return retVal;
	}

	protected List<Point> getRandomGaussianPoints(int count, boolean is3D) {
		List<Point> pointList = new LinkedList<Point>();

		Random rand = new Random();

		long stretcher = 10000;

		for (int i = 0; i < count; ++i) {
			double tempX = rand.nextGaussian() * stretcher;
			double tempY = rand.nextGaussian() * stretcher;
			double tempZ = 1.0;

			if (is3D) {
				tempZ = rand.nextGaussian() * stretcher;
			}

			pointList.add(new Point3D(tempX, tempY, tempZ));
		}

		return pointList;
	}

}
