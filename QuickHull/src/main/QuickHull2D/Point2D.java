package main.QuickHull2D;

import java.util.Collection;

import main.QuickHull.Point;

/**
 * Diese Klasse soll einen Punkt im zweidimensionalen Raum darstellen. Die
 * z-Achse ist hierbei immer 1
 * 
 * @author Maximilian Schempp
 * 
 */
public class Point2D implements Point {

	private double x;
	private double y;

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getZ() {
		return 1;
	}

	public int hashCode() {
		
		long p1 = 73856093;
		long p2 = 19349663;
		
		long xBits = Double.doubleToLongBits(x);
		long yBits = Double.doubleToLongBits(y);
		
		return (int) (xBits*p1 ^ yBits*p2);
	}

	public boolean equals(Object obj) {
		if(obj == null) return false;
		Point p = (Point) obj;
		return (getX() == p.getX() && getY() == p.getY() && getZ() == p.getZ());
	}
}
