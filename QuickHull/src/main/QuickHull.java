package main;

import java.util.Iterator;
import java.util.List;

/**
 * Abstrakte Klasse die einige Methoden für alle QuickHull implementierungen bereitstellt,
 * sowie die Methode getBorderPoints definiert, welche die Punkte berechnet, welche die
 * konvexe Hülle der Punkte darstellt
 * 
 * @author Maximilian Schempp
 *
 */
public abstract class QuickHull {
	
	/**
	 * Berechnet die Punkte der konvexen Hüllen zu der Punktwolke,
	 * welche durch pointSetInput definiert wird.
	 * 
	 * @param pointSetInput
	 * @return
	 */
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
	
	protected Point getFarSidePoint(List<Point> list) {
		Iterator<Point> iter = list.iterator();

		Point farSidePoint = iter.next();

		while (iter.hasNext()) {
			Point curr = iter.next();
			if (curr.getZ() > farSidePoint.getZ()) {
				farSidePoint = curr;
			}
		}

		return farSidePoint;
	}
	
	protected Point getNearSidePoint(List<Point> list) {
		Iterator<Point> iter = list.iterator();

		Point nearSidePoint = iter.next();

		while (iter.hasNext()) {
			Point curr = iter.next();
			if (curr.getZ() < nearSidePoint.getZ()) {
				nearSidePoint = curr;
			}
		}

		return nearSidePoint;
	}

	protected Point getTopSidePoint(List<Point> list) {
		Iterator<Point> iter = list.iterator();

		Point topSidePoint = iter.next();

		while (iter.hasNext()) {
			Point curr = iter.next();
			if (curr.getY() > topSidePoint.getY()) {
				topSidePoint = curr;
			}
		}

		return topSidePoint;
	}

	protected Point getLowSidePoint(List<Point> list) {
		Iterator<Point> iter = list.iterator();

		Point lowSidePoint = iter.next();

		while (iter.hasNext()) {
			Point curr = iter.next();
			if (curr.getY() < lowSidePoint.getY()) {
				lowSidePoint = curr;
			}
		}

		return lowSidePoint;
	}
	
}
