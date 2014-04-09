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
		long p1 = 73856093;
		long p2 = 19349663;
		long p3 = 83492791;
		
		long xBits = Double.doubleToLongBits(x);
		long yBits = Double.doubleToLongBits(y);
		long zBits = Double.doubleToLongBits(z);
		
		return (int) (xBits*p1 ^ yBits*p2 ^ zBits*p3);
	}

	public boolean equals(Object obj) {
		if(obj == null) return false;
		Point p = (Point) obj;
		return (getX() == p.getX() && getY() == p.getY() && getZ() == p.getZ());
	}

}
