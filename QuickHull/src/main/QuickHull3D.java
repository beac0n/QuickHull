package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	 * @param pointInput
	 *            Die Liste der gegebenen Punkte
	 * @return eine Liste der Randpunkte
	 */
	public Collection<Point> getBorderPoints(Collection<Point> pointInput) {

		if (pointInput.size() < 5)
			return pointInput;

		HashSet<Point> points = new HashSet<Point>(pointInput);

		List<Point> borderPoints = new LinkedList<Point>();

		List<Point> initHull = getInitialhull(points);

		borderPoints.addAll(initHull);

		points.removeAll(initHull);

		HashSet<Point> behindSet = getAllPointsOver(initHull.get(2),
				initHull.get(1), initHull.get(0), points);
		points.removeAll(behindSet);

		HashSet<Point> rightSet = getAllPointsOver(initHull.get(1),
				initHull.get(3), initHull.get(0), points);
		points.removeAll(rightSet);

		HashSet<Point> leftSet = getAllPointsOver(initHull.get(3),
				initHull.get(2), initHull.get(0), points);
		points.removeAll(leftSet);

		HashSet<Point> lowerSet = getAllPointsOver(initHull.get(2),
				initHull.get(3), initHull.get(1), points);
		points.removeAll(lowerSet);

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
	 * @param points
	 *            Collection von Punkten, aus welchen später die konvexen
	 *            Hüllenpunkte berechnet werden.
	 * @return Liste mit den ersten 4 Randpunkten
	 */
	private List<Point> getInitialhull(HashSet<Point> points) {

		// bestimme die zwei am weitesten entfernten punkte
		Point left = getLeftSidePoint(points);
		Collection<Point> leftPoints = getAllEqualX(points, left);

		Point right = getRightSidePoint(points);
		Collection<Point> rightPoints = getAllEqualX(points, right);

		Point top = getTopSidePoint(points);
		Collection<Point> topPoints = getAllEqualY(points, top);

		Point low = getLowSidePoint(points);
		Collection<Point> lowPoints = getAllEqualY(points, low);

		Point far = getFarSidePoint(points);
		Collection<Point> farPoints = getAllEqualZ(points, far);

		Point near = getNearSidePoint(points);
		Collection<Point> nearPoints = getAllEqualZ(points, near);

		List<TwoPointDistance> distances = new LinkedList<TwoPointDistance>();
		
		// if distant points lie on same dimension
		Optional<TwoPointDistance> lrPoints = getMaxDistanceOfFixedDimension(leftPoints, rightPoints, false);
		Optional<TwoPointDistance> tlPoints = getMaxDistanceOfFixedDimension(topPoints, lowPoints, false);
		Optional<TwoPointDistance> fnPoints = getMaxDistanceOfFixedDimension(farPoints, nearPoints, true);
		
		if(lrPoints.isPresent()) {
			distances.add(lrPoints.get());
		}
		else {
			distances.add(getMaxDistanceOf(leftPoints, rightPoints));
		}
		
		if(tlPoints.isPresent()) {
			distances.add(tlPoints.get());
		}
		else {
			distances.add(getMaxDistanceOf(topPoints, lowPoints));
		}
		
		if(fnPoints.isPresent()) {
			distances.add(fnPoints.get());
		}
		else {
			distances.add(getMaxDistanceOf(farPoints, nearPoints));
		}

		TwoPointDistance maxDistance = distances
				.stream()
				.max((a, b) -> (int) Math.signum(a.generateDistance()
						- b.generateDistance())).get();

		Point maxfirst = maxDistance.getB();
		Point maxsecond = maxDistance.getA();

		points.remove(maxfirst);
		points.remove(maxsecond);

		// bestimmte den Punkt der am weitesten von der Linie entfernt ist
		Point maxdistPointLine = getMaxDistantPointFromLine(points, maxfirst,
				maxsecond);
		points.remove(maxdistPointLine);

		// bestimmte den Punkt der am weitesten von der Ebene entfernt ist
		Point maxdistPointPlane = getMaxDistantPointFromPlane(points, maxfirst,
				maxsecond, maxdistPointLine);
		points.remove(maxdistPointPlane);

		double planeToPoint = getDifferenceFromNormal(maxfirst, maxsecond,
				maxdistPointLine, maxdistPointPlane);

		List<Point> returnValue = new ArrayList<Point>();

		if (planeToPoint < 0) {
			returnValue.add(maxdistPointLine);
			returnValue.add(maxsecond);
			returnValue.add(maxfirst);
		} else {
			returnValue.add(maxfirst);
			returnValue.add(maxsecond);
			returnValue.add(maxdistPointLine);
		}

		returnValue.add(maxdistPointPlane);

		return returnValue;
	}

	private Optional<TwoPointDistance> getMaxDistanceOfFixedDimension(Collection<Point> sideA,
			Collection<Point> sideB, boolean isZ) {
		HashSet<TwoPointDistance> distances = new HashSet<TwoPointDistance>();

		for (Point curA : sideA) {
			for (Point curB : sideB) {
				if (isZ) {
					if (curB.getX() == curA.getX()) {
						distances.add(new TwoPointDistance(curA, curB));
					}
				} else if (curB.getZ() == curA.getZ()) {
					distances.add(new TwoPointDistance(curA, curB));
				}

			}
		}

		return distances
				.stream()
				.max((a, b) -> (int) Math.signum(a.generateDistance()
						- b.generateDistance()));
	}

	private TwoPointDistance getMaxDistanceOf(Collection<Point> sideA, Collection<Point> sideB) {
		HashSet<TwoPointDistance> distances = new HashSet<TwoPointDistance>();

		for (Point curA : sideA) {
			for (Point curB : sideB) {
				distances.add(new TwoPointDistance(curA, curB));

			}
		}

		return distances
				.stream()
				.max((a, b) -> (int) Math.signum(a.generateDistance()
						- b.generateDistance())).get();
	}

	private Collection<Point> getAllEqualX(HashSet<Point> points, Point left) {
		return points.stream().filter(a -> a.getX() == left.getX())
				.collect(Collectors.toCollection(() -> new ArrayList<>()));
	}

	private Collection<Point> getAllEqualY(HashSet<Point> points, Point left) {
		return points.stream().filter(a -> a.getY() == left.getY())
				.collect(Collectors.toCollection(() -> new ArrayList<>()));
	}

	private Collection<Point> getAllEqualZ(HashSet<Point> points, Point left) {
		return points.stream().filter(a -> a.getZ() == left.getZ())
				.collect(Collectors.toCollection(() -> new ArrayList<>()));
	}

	/**
	 * berechnet den Punkt der am weitesten von der Ebene entfernt ist, welche
	 * durch die drei punkte maxfirst maxsecond maxdistPointLine bestimmt wird.
	 * 
	 * @param points
	 *            die Liste von Punkten die existieren
	 * @param p
	 *            der erste Punkt der Ebene
	 * @param q
	 *            der zweite Punkt der Ebene
	 * @param r
	 *            der dritte Punkt der Ebene
	 * @return der Punkt der am weitesten von der Ebene entfernt ist
	 */
	private Point getMaxDistantPointFromPlane(Collection<Point> points,
			Point p, Point q, Point r) {

		return points
				.stream()
				.max((a, b) -> (int) Math.signum(Math
						.abs(getDifferenceFromNormal(p, q, r, a))
						- Math.abs(getDifferenceFromNormal(p, q, r, b)))).get();
	}

	/**
	 * berechnet den Punkt der am weitesten von der Geraden entfernt ist, welche
	 * durch die zwei punkte maxfirst und maxsecond bestimmt wird.
	 * 
	 * @param points
	 *            Liste der Punkte
	 * @param p
	 *            erster Punkt der Geraden
	 * @param q
	 *            zweiter Punkt der Geraden
	 * @return der Punkt der am weitesten von der Geraden entfernt ist
	 */
	private Point getMaxDistantPointFromLine(Collection<Point> points, Point p,
			Point q) {
		// determinantenform: u x (x-p) = 0
		// bestimme u => Richtungsvektor
		Point u = new Point3D(p.getX() - q.getX(), p.getY() - q.getY(),
				p.getZ() - q.getZ());

		return points
				.stream()
				.max((a, b) -> (int) Math.signum(getDistanceFromLine(u, p, a)
						- getDistanceFromLine(u, p, b))).get();
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
	 * @param left
	 *            linker Punkt der aufzuspannenden Linie
	 * @param right
	 *            rechter Punkt der aufzuspannenden Linie
	 * @param points
	 *            momentane Liste der Punkte
	 * @param borderPoints
	 *            Liste der Randpunkte
	 */
	private void calculateBorder(Point left, Point right, Point far,
			HashSet<Point> points, List<Point> borderPoints) {
		if (points.size() == 0)
			return;

		Point uppest = getUppestPoint(left, right, far, points);

		points.remove(uppest);
		borderPoints.add(uppest);

		HashSet<Point> rightUpperList = getAllPointsOver(uppest, far, left,
				points);
		points.removeAll(rightUpperList);

		HashSet<Point> leftUpperList = getAllPointsOver(left, right, uppest,
				points);
		points.removeAll(leftUpperList);

		HashSet<Point> farUpperList = getAllPointsOver(right, far, uppest,
				points);
		points.removeAll(farUpperList);

		calculateBorder(uppest, far, left, rightUpperList, borderPoints);
		calculateBorder(left, right, uppest, leftUpperList, borderPoints);
		calculateBorder(right, far, uppest, farUpperList, borderPoints);
	}

	/**
	 * Bestimmt den Punkt der am weitesten oberhalb einer Ebene liegt.
	 * 
	 * @param left
	 *            erster Punkt der Ebene
	 * @param right
	 *            zweiter Punkt der Ebene
	 * @param far
	 *            dritter Punkt der Ebene
	 * @param points
	 *            Liste von Punkten
	 * @return der Punkt der am weitesten oberhalb der Ebene liegt
	 */
	private Point getUppestPoint(Point left, Point right, Point far,
			Collection<Point> points) {
		return points
				.stream()
				.max((a, b) -> (int) Math.signum(getDifferenceFromNormal(left,
						right, far, a)
						- getDifferenceFromNormal(left, right, far, b))).get();
	}

	/**
	 * Bestimmt alle Punkte oberhalb einer Ebene
	 * 
	 * @param left
	 *            der erste Punkt der Ebene
	 * @param right
	 *            der zweite Punkt der Ebene
	 * @param far
	 *            der dritte Punkt der Ebene
	 * @param points
	 *            die Punktmenge aus der die Punkte zu bestimmen sind
	 * @return die Punkte oberhalb der Ebene
	 */
	private HashSet<Point> getAllPointsOver(Point left, Point right, Point far,
			Collection<Point> points) {
		return points
				.stream()
				.filter(currPoint -> getDifferenceFromNormal(left, right, far,
						currPoint) > 0)
				.collect(Collectors.toCollection(() -> new HashSet<Point>()));
	}

	/**
	 * Berechnet wie weit ein gegebener Punkt von dem Polygon entfernt liegt,
	 * das durch die anderen drei übergebenen Punkte aufgespannt wird. Der
	 * Rückgabewert ist ein Maß für die Distanz. Je weiter der Wert von 0
	 * entfernt ist, desto weiter ist der Punkt von der Linie entfernt.
	 * 
	 * @param left
	 *            erster Punkt um das Polygon aufzuspannen
	 * @param right
	 *            zweiter Punkt um das Polygon aufzuspannen
	 * @param far
	 *            dritter Punkt um das Polygon aufzuspannen
	 * @param x
	 *            Punkt, dessen Abstand berechnet werden soll.
	 * @return
	 */
	private double getDifferenceFromNormal(Point left, Point right, Point far,
			Point x) {

		// Ebenengleichung normale n = (q-p) x (r-p)

		// q-p
		double a1 = right.getX() - left.getX();
		double a2 = right.getY() - left.getY();
		double a3 = right.getZ() - left.getZ();

		// r-p
		double b1 = far.getX() - left.getX();
		double b2 = far.getY() - left.getY();
		double b3 = far.getZ() - left.getZ();

		// (q-p) x (r-p)
		double normalX = a2 * b3 - a3 * b2;
		double normalY = a3 * b1 - a1 * b3;
		double normalZ = a1 * b2 - a2 * b1;

		// (x-p)
		double xMinuspX = x.getX() - left.getX();
		double xMinuspY = x.getY() - left.getY();
		double xMinuspZ = x.getZ() - left.getZ();

		// (x-p) * n
		double endX = xMinuspX * normalX;
		double endY = xMinuspY * normalY;
		double endZ = xMinuspZ * normalZ;

		// Ebenengleichung (a-x) * n = 0
		double end = endX + endY + endZ;

		return end;
	}
}
