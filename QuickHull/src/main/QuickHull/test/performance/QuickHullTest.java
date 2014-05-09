package main.QuickHull.test.performance;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import main.QuickHull.Point;
import main.QuickHull.QuickHull;
import main.QuickHull2D.Point2D;
import main.QuickHull3D.Point3D;

public abstract class QuickHullTest {

	protected int pointCap = 3_000_000;
	protected int rounds = 20;

	protected abstract QuickHull createQuickHullObject();

	protected double timeForPoints(Collection<Point> pointList, int repeat) {
		double overallTime = 0;
		for (int i = 0; i < repeat; ++i) {
			double currTime = measureTimeOnce(pointList);
			overallTime += currTime;
		}
		return overallTime / (double) repeat;
	}

	protected double measureTimeOnce(Collection<Point> pointList) {
		QuickHull qh = createQuickHullObject();
		
		long startTime = System.nanoTime();
		qh.getBorderPoints(pointList);
		long stopTime = System.nanoTime();
		
		long elapsedTime = stopTime - startTime;

		return elapsedTime / 1_000_000d;
	}

	protected HashSet<Point> getRandomSphereGaussianPoints(long pointsCount) {
		HashSet<Point> pointList = new HashSet<Point>();

		Random rand = new Random();

		for (int i = 0; i < pointsCount; ++i) {
			double tempX = rand.nextGaussian() * 10_000;
			double tempY = rand.nextGaussian() * 10_000;
			double tempZ = rand.nextGaussian() * 10_000;

			double len = Math.sqrt(tempX * tempX + tempY * tempY + tempZ * tempZ);

			tempX /= len;
			tempY /= len;
			tempZ /= len;

			pointList.add(new Point3D(tempX, tempY, tempZ));
		}		

		return pointList;
	}

	protected HashSet<Point> getRandomGaussianPoints(int count, boolean is3D) {
		HashSet<Point> pointList = new HashSet<Point>();

		Random rand = new Random();

		for (int i = 0; i < count; ++i) {
			double tempX = rand.nextGaussian();
			double tempY = rand.nextGaussian();
			double tempZ = rand.nextGaussian();
			
			if(is3D) pointList.add(new Point3D(tempX, tempY, tempZ));
			else pointList.add(new Point2D(tempX, tempY));
		}

		return pointList;
	}

}
