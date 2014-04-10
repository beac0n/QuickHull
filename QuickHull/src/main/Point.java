package main;

import java.util.Collection;

public interface Point {

	public double getX();

	public double getY();

	public double getZ();
	
	public Collection<Point> getOwner();
	public void setOwner(Collection<Point> owner);
}
