package main;

/**
 * Diese Klasse stellt einen Punkt im dreidimensionalen Raum dar
 * 
 * @author Maximilian Schempp
 * 
 */
public class Point3D implements Point {

	private double x;
	private double y;
	private double z;

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
		return z;
	}

	public int hashCode() {
		return Double.valueOf(x).hashCode();
	}

	public boolean equals(Object obj) {
		Point p = (Point) obj;
		return (getX() == p.getX() && getY() == p.getY() && getZ() == p.getZ());
	}

}
