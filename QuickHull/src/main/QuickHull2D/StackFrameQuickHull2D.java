package main.QuickHull2D;

import java.util.List;
import java.util.Optional;

import main.QuickHull.Point;

public class StackFrameQuickHull2D {
	
	private Point left;
	private Point right;
	private List<Point> points;
	private Optional<Point> borderPoint;
	
	public StackFrameQuickHull2D(Point left, Point right, List<Point> points, Point borderPoint) {
		this.left = left;
		this.right = right;
		this.points = points;
		this.borderPoint = Optional.ofNullable(borderPoint);
	}
	
	public Point getLeft() {
		return left;
	}

	public Point getRight() {
		return right;
	}

	public List<Point> getPoints() {
		return points;
	}

	public Optional<Point> getBorderPoint() {
		return borderPoint;
	}

}
