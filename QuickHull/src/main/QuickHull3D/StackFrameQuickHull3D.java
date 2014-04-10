package main.QuickHull3D;

import java.util.Collection;
import java.util.Optional;

import main.QuickHull.Point;

public class StackFrameQuickHull3D {
	private Point left;
	private Point right;
	private Point far;
	private Collection<Point> points;
	
	public StackFrameQuickHull3D(Point left, Point right, Point far, Collection<Point> points) {
		this.left = left;
		this.right = right;
		this.points = points;
		this.far = far;
	}
	
	public Point getLeft() {
		return left;
	}

	public Point getRight() {
		return right;
	}
	
	public Point getFar() {
		return far;
	}

	public Collection<Point> getPoints() {
		return points;
	}
}
