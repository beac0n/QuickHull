package main;

/**
 * Diese Klasse soll einen Punkt im zweidimensionalen Raum darstellen.
 * Die z-Achse ist hierbei immer 1
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
}
