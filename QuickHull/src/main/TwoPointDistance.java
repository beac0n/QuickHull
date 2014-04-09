package main;

public class TwoPointDistance {
	private Point a;
	private Point b;
	private double distance = 0;

	public TwoPointDistance(Point a, Point b) {
		this.a = a;
		this.b = b;
	}

	public boolean equals(Object obj) {
		TwoPointDistance temp = (TwoPointDistance) obj;
		return (a.equals(temp.getA()) && b.equals(temp.getB()) 
				|| a.equals(temp.getB()) && b.equals(temp.getA()));
	}

	public int hashCode() {
		return a.hashCode() ^ b.hashCode();
	}

	public double getDistance() {
		return distance;
	}

	public double generateDistance() {
		if (a.equals(b) || distance > 0)
			return distance;

		double dX = a.getX() - b.getX();
		double dY = a.getY() - b.getY();
		double dZ = a.getZ() - b.getZ();

		distance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
		return distance;
	}

	public Point getA() {
		return a;
	}

	public Point getB() {
		return b;
	}

}
