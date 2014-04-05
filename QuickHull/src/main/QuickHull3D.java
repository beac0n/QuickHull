package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse, welche das Berechnen der Randpunkte einer Punktwolke im
 * 3-Dimensionalen Raum kapselt
 * 
 * @author Maximilian Schempp
 * 
 */
public class QuickHull3D extends QuickHull {

	/**
	 * Liefert für eine gegebene Liste an Punkten alle Randpunkte.
	 * 
	 * @param pointSetInput
	 *            Die Liste der gegebenen Punkte
	 * @return eine Liste der Randpunkte
	 */
	public List<Point> getBorderPoints(List<Point> pointSetInput) {

		if (pointSetInput.size() < 5)
			return pointSetInput;

		List<Point> pointSet = new ArrayList<Point>(pointSetInput);

		List<Point> borderPoints = new LinkedList<Point>();

		List<Point> initHull = getInitialhull(pointSet);

		borderPoints.addAll(initHull);

		pointSet.removeAll(initHull);

		List<Point> behindSet = getAllPointsOver(initHull.get(2),
				initHull.get(1), initHull.get(0), pointSet);
		pointSet.removeAll(behindSet);
		List<Point> rightSet = getAllPointsOver(initHull.get(1),
				initHull.get(3), initHull.get(0), pointSet);
		pointSet.removeAll(rightSet);
		List<Point> leftSet = getAllPointsOver(initHull.get(3),
				initHull.get(2), initHull.get(0), pointSet);
		pointSet.removeAll(leftSet);
		List<Point> lowerSet = getAllPointsOver(initHull.get(2),
				initHull.get(3), initHull.get(1), pointSet);
		pointSet.removeAll(lowerSet);

		calculateBorder(initHull.get(2), initHull.get(1), initHull.get(0),
				behindSet, borderPoints);
		calculateBorder(initHull.get(1), initHull.get(3), initHull.get(0),
				rightSet, borderPoints);
		calculateBorder(initHull.get(3), initHull.get(2), initHull.get(0),
				leftSet, borderPoints);
		calculateBorder(initHull.get(2), initHull.get(3), initHull.get(1),
				lowerSet, borderPoints);

		return borderPoints;
	}

	/**
	 * Bestimmt die ersten 4 Punkte welche benötigt werden um den Quckhull
	 * Algorithmus im dreidimensionalen Raum auszuführen.
	 * 
	 * @param pointSet
	 *            Liste von Punkten, aus welchen später die konvexen
	 *            Hüllenpunkte berechnet werden.
	 * @return Liste mit den ersten 4 Randpunkten
	 */
	private List<Point> getInitialhull(List<Point> pointSet) {

		// bestimmte die zwei am weitesten entfernten punkte
		Point left = getLeftSidePoint(pointSet);
		Point right = getRightSidePoint(pointSet);

		Point top = getTopSidePoint(pointSet);
		Point low = getLowSidePoint(pointSet);

		Point far = getFarSidePoint(pointSet);
		Point near = getNearSidePoint(pointSet);

		double distLRX = left.getX() - right.getX();
		double distLRY = left.getY() - right.getY();
		double distLRZ = left.getZ() - right.getZ();

		double distLR = distLRX * distLRX + distLRY * distLRY + distLRZ
				* distLRZ;

		double distTLX = top.getX() - low.getX();
		double distTLY = top.getY() - low.getY();
		double distTLZ = top.getZ() - low.getZ();

		double distTL = distTLX * distTLX + distTLY * distTLY + distTLZ
				* distTLZ;

		double distFNX = far.getX() - near.getX();
		double distFNY = far.getY() - near.getY();
		double distFNZ = far.getZ() - near.getZ();

		double distFN = distFNX * distFNX + distFNY * distFNY + distFNZ
				* distFNZ;

		List<Double> temp = new LinkedList<Double>();
		temp.add(distLR);
		temp.add(distTL);
		temp.add(distFN);

		double max = Collections.max(temp);

		Point maxfirst;
		Point maxsecond;

		if (max == distLR) {
			maxfirst = left;
			maxsecond = right;
		} else if (max == distTL) {
			maxfirst = top;
			maxsecond = low;
		} else {
			maxfirst = far;
			maxsecond = near;
		}

		pointSet.remove(maxfirst);
		pointSet.remove(maxsecond);

		Point maxdistPointLine = getMaxDistantPointFromLine(pointSet, maxfirst,
				maxsecond);
		pointSet.remove(maxdistPointLine);

		Point maxdistPointPlane = getMaxDistantPointFromPlane(pointSet,
				maxfirst, maxsecond, maxdistPointLine);
		pointSet.remove(maxdistPointPlane);

		List<Point> returnValue = new LinkedList<Point>();
		returnValue.add(maxfirst);
		returnValue.add(maxsecond);
		returnValue.add(maxdistPointLine);
		returnValue.add(maxdistPointPlane);

		return returnValue;
	}

	/**
	 * berechnet den Punkt der am weitesten von der Ebene entfernt ist, welche
	 * durch die drei punkte maxfirst maxsecond maxdistPointLine bestimmt wird.
	 * 
	 * @param pointSet
	 *            die Liste von Punkten die existieren
	 * @param maxfirst
	 *            der erste Punkt der Ebene
	 * @param maxsecond
	 *            der zweite Punkt der Ebene
	 * @param maxdistPointLine
	 *            der dritte Punkt der Ebene
	 * @return der Punkt der am weitesten von der Ebene entfernt ist
	 */
	private Point getMaxDistantPointFromPlane(List<Point> pointSet,
			Point maxfirst, Point maxsecond, Point maxdistPointLine) {
		Iterator<Point> iter;
		double oldDist;
		iter = pointSet.iterator();

		oldDist = 0;
		Point maxdistPointPlane = null;

		while (iter.hasNext()) {
			Point cur = iter.next();
			double curDist = getDifferenceFromNormal(maxfirst, maxsecond,
					maxdistPointLine, cur);

			if (Math.abs(curDist) > oldDist) {
				maxdistPointPlane = cur;
				oldDist = curDist;
			}
		}
		return maxdistPointPlane;
	}

	/**
	 * berechnet den Punkt der am weitesten von der Geraden entfernt ist, welche
	 * durch die zwei punkte maxfirst und maxsecond bestimmt wird.
	 * 
	 * @param pointSet
	 *            Liste der Punkte
	 * @param maxfirst
	 *            erster Punkt der Geraden
	 * @param maxsecond
	 *            zweiter Punkt der Geraden
	 * @return der Punkt der am weitesten von der Geraden entfernt ist
	 */
	private Point getMaxDistantPointFromLine(List<Point> pointSet,
			Point maxfirst, Point maxsecond) {
		// determinantenform: u x (x-p) = 0
		// bestimme u => Richtungsvektor
		// p == maxfirst
		Point u = new Point3D(maxfirst.getX() - maxsecond.getX(),
				maxfirst.getY() - maxsecond.getY(), maxfirst.getZ()
						- maxsecond.getZ());

		Iterator<Point> iter = pointSet.iterator();

		double oldDist = 0;
		Point maxdistPointLine = null;

		while (iter.hasNext()) {
			Point cur = iter.next();
			double curDist = getDistanceFromLine(u, maxfirst, cur);

			if (curDist > oldDist) {
				maxdistPointLine = cur;
				oldDist = curDist;
			}
		}
		return maxdistPointLine;
	}

	/**
	 * Bestimmt ein Maß des Abstand eines Punktes von einer Geraden. Hierbei
	 * handelt es sich nicht um den genauen Abstand, da der Wert nur zum
	 * Vergleich benötigt wird.
	 * 
	 * @param u
	 *            der Richtungsvektor der Geraden
	 * @param p
	 *            ein Stüztvektor
	 * @param x
	 *            der zu prüfende Punkt
	 * @return ein Maß für den Abstand der Punktes zur Geraden
	 */
	private double getDistanceFromLine(Point u, Point p, Point x) {
		// determinantenform: u x (x-p) = 0

		double b1 = x.getX() - p.getX();
		double b2 = x.getY() - p.getY();
		double b3 = x.getZ() - p.getZ();

		double a1 = u.getX();
		double a2 = u.getY();
		double a3 = u.getZ();

		double axbX = a2 * b3 - a3 * b2;
		double axbY = a3 * b1 - a1 * b3;
		double axbZ = a1 * b2 - a2 * b1;

		double dist = axbX * axbX + axbY * axbY + axbZ * axbZ;

		return dist;
	}

	/**
	 * Rekursiver Teil des Quickhull-Algorithmuss, der nach und nach den Rand
	 * der 2-Dimensionalen Punktwolke berechnet.
	 * 
	 * @param leftSidePoint
	 *            linker Punkt der aufzuspannenden Linie
	 * @param rightSidePoint
	 *            rechter Punkt der aufzuspannenden Linie
	 * @param pointSet
	 *            momentane Liste der Punkte
	 * @param borderPoints
	 *            Liste der Randpunkte
	 */
	private void calculateBorder(Point leftSidePoint, Point rightSidePoint,
			Point farSidePoint, List<Point> pointSet, List<Point> borderPoints) {

		if (pointSet.size() == 0)
			return;

		Point uppestPoint = getUppestPoint(leftSidePoint, rightSidePoint,
				farSidePoint, pointSet);

		if (uppestPoint == null)
			return;

		pointSet.remove(uppestPoint);
		borderPoints.add(uppestPoint);

		List<Point> rightUpperSet = getAllPointsOver(uppestPoint, farSidePoint,
				leftSidePoint, pointSet);
		pointSet.removeAll(rightUpperSet);
		List<Point> leftUpperSet = getAllPointsOver(leftSidePoint,
				rightSidePoint, uppestPoint, pointSet);
		pointSet.removeAll(leftUpperSet);
		List<Point> farUpperSet = getAllPointsOver(rightSidePoint,
				farSidePoint, uppestPoint, pointSet);
		pointSet.removeAll(farUpperSet);

		calculateBorder(uppestPoint, farSidePoint, leftSidePoint,
				rightUpperSet, borderPoints);
		calculateBorder(leftSidePoint, rightSidePoint, uppestPoint,
				leftUpperSet, borderPoints);
		calculateBorder(rightSidePoint, farSidePoint, uppestPoint, farUpperSet,
				borderPoints);
	}

	/**
	 * Bestimmt den Punkt der am weitesten oberhalb einer Ebene liegt.
	 * 
	 * @param leftSidePoint
	 *            erster Punkt der Ebene
	 * @param rightSidePoint
	 *            zweiter Punkt der Ebene
	 * @param farSidePoint
	 *            dritter Punkt der Ebene
	 * @param currentPointSet
	 *            Liste von Punkten
	 * @return der Punkt der am weitesten oberhalb der Ebene liegt
	 */
	private Point getUppestPoint(Point leftSidePoint, Point rightSidePoint,
			Point farSidePoint, List<Point> currentPointSet) {

		Point uppestPoint = null;

		double oldDifferenceFromNormal = 0;

		Iterator<Point> iter = currentPointSet.iterator();

		while (iter.hasNext()) {
			Point currentPoint = iter.next();

			double currentDifferenceFromNormal = getDifferenceFromNormal(
					leftSidePoint, rightSidePoint, farSidePoint, currentPoint);
			if (currentDifferenceFromNormal > oldDifferenceFromNormal) {
				uppestPoint = currentPoint;
				oldDifferenceFromNormal = currentDifferenceFromNormal;
			}
		}

		return uppestPoint;
	}

	/**
	 * Bestimmt alle Punkte oberhalb einer Ebene
	 * 
	 * @param leftSidePoint
	 *            der erste Punkt der Ebene
	 * @param rightSidePoint
	 *            der zweite Punkt der Ebene
	 * @param farSidePoint
	 *            der dritte Punkt der Ebene
	 * @param upperSet
	 *            die Punktmenge aus der die Punkte zu bestimmen sind
	 * @return die Punkte oberhalb der Ebene
	 */
	private List<Point> getAllPointsOver(Point leftSidePoint,
			Point rightSidePoint, Point farSidePoint, List<Point> upperSet) {

		List<Point> returnValue = new LinkedList<Point>();

		Iterator<Point> iter = upperSet.iterator();

		while (iter.hasNext()) {
			Point currPoint = iter.next();
			double differenceFromNormal = getDifferenceFromNormal(
					leftSidePoint, rightSidePoint, farSidePoint, currPoint);

			if (differenceFromNormal > 0) {
				returnValue.add(currPoint);
			}
		}

		return returnValue;
	}

	/**
	 * Berechnet wie weit ein gegebener Punkt von dem Polygon entfernt liegt,
	 * das durch die anderen drei übergebenen Punkte aufgespannt wird. Der
	 * Rückgabewert ist ein Maß für die Distanz. Je weiter der Wert von 0
	 * entfernt ist, desto weiter ist der Punkt von der Linie entfernt.
	 * 
	 * @param leftSidePoint
	 *            erster Punkt um das Polygon aufzuspannen
	 * @param rightSidepoint
	 *            zweiter Punkt um das Polygon aufzuspannen
	 * @param farSidePoint
	 *            dritter Punkt um das Polygon aufzuspannen
	 * @param x
	 *            Punkt, dessen Abstand berechnet werden soll.
	 * @return
	 */
	private double getDifferenceFromNormal(Point leftSidePoint,
			Point rightSidepoint, Point farSidePoint, Point x) {

		// Ebenengleichung normale n = (q-p) x (r-p)

		// q-p
		double a1 = rightSidepoint.getX() - leftSidePoint.getX();
		double a2 = rightSidepoint.getY() - leftSidePoint.getY();
		double a3 = rightSidepoint.getZ() - leftSidePoint.getZ();

		// r-p
		double b1 = farSidePoint.getX() - leftSidePoint.getX();
		double b2 = farSidePoint.getY() - leftSidePoint.getY();
		double b3 = farSidePoint.getZ() - leftSidePoint.getZ();

		// (q-p) x (r-p)
		double normalX = a2 * b3 - a3 * b2;
		double normalY = a3 * b1 - a1 * b3;
		double normalZ = a1 * b2 - a2 * b1;

		// (x-p)
		double xMinuspX = x.getX() - leftSidePoint.getX();
		double xMinuspY = x.getY() - leftSidePoint.getY();
		double xMinuspZ = x.getZ() - leftSidePoint.getZ();

		// (x-p) * n
		double endX = xMinuspX * normalX;
		double endY = xMinuspY * normalY;
		double endZ = xMinuspZ * normalZ;

		// Ebenengleichung (a-x) * n = 0
		double end = endX + endY + endZ;

		return end;
	}
}
