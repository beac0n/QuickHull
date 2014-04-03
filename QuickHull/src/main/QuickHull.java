package main;

import java.util.Iterator;
import java.util.List;

public abstract class QuickHull {
	
	public abstract List<Point> getBorderPoints(List<Point> pointSetInput);

	/**
	 * bestimmt den Punkt aus der Liste der am weitesten links liegt,
	 * also den kleinsten x-Wert hat
	 * 
	 * @param list die Liste mit den Punkten
	 * @return der Punkt mit dem geringsten x-Wert
	 */
	protected Point getRightSidePoint(List<Point> list) {
		Iterator<Point> iter = list.iterator();
		
		Point rightSidePoint = iter.next();
		
		while(iter.hasNext()) {
			Point curr = iter.next();
			if (curr.getX() > rightSidePoint.getX()) {
				rightSidePoint = curr;
			}
		}

		return rightSidePoint;
	}

	/**
	 * bestimmt den Punt aus der Liste, welcher am weitesten rechts liegt,
	 * also den größten x-Wert hat
	 * 
	 * @param list die Liste der Punkte
	 * @return der Punkt der am weitesten rechts liegt
	 */
	protected Point getLeftSidePoint(List<Point> list) {
		Iterator<Point> iter = list.iterator();
		
		Point leftSidePoint = iter.next();

		while(iter.hasNext()) {
			Point curr = iter.next();
			if(curr.getX() < leftSidePoint.getX()) {
				leftSidePoint = curr;
			}
		}

		return leftSidePoint;
	}
	
}
