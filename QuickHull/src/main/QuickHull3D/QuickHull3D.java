package main.QuickHull3D;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
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

import main.QuickHull.Point;
import main.QuickHull.QuickHull;
import main.QuickHull.TwoPointDistance;

/**
 * Klasse, welche das Berechnen der Randpunkte einer Punktwolke im
 * 3-Dimensionalen Raum kapselt
 * 
 * @author Maximilian Schempp
 * 
 */
public class QuickHull3D extends QuickHull {

	private PrintWriter out;

	private void printPointsAndShape(Point a, Point b, Point c, Collection<Point> points) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		if (out == null)
			try {
				out = new PrintWriter(timestamp.toString()+".x3d");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		double randColorR = (Math.random());
		double randColorG = (Math.random());
		double randColorB = (Math.random());

		out.println("<Shape><IndexedFaceSet coordIndex='0 1 2'>"
				+ "<Color color='" + randColorR + " " + randColorG + " "
				+ randColorB + " " + randColorR + " " + randColorG + " "
				+ randColorB + " " + randColorR + " " + randColorG + " "
				+ randColorB + "'/>" + "<Coordinate point='");
		out.print(a.getX() + " " + a.getY() + " " + a.getZ() + " ");
		out.print(b.getX() + " " + b.getY() + " " + b.getZ() + " ");
		out.print(c.getX() + " " + c.getY() + " " + c.getZ());
		out.print("'/></IndexedFaceSet></Shape>");
		out.println();

		out.flush();

		out.print("<Shape><Appearance><Material emissiveColor='" + randColorR
				+ " " + randColorG + " " + randColorB + "'/> </Appearance>"
				+ "<PointSet><Coordinate point='");

		Iterator<Point> iter = points.iterator();

		while (iter.hasNext()) {
			Point cur = iter.next();
			out.print(cur.getX() + " " + cur.getY() + " " + cur.getZ() + " ");
		}

		out.println("'/></PointSet></Shape>");
		out.println();
		out.println();

		out.flush();
	}

	/**
	 * Liefert für eine gegebene Liste an Punkten alle Randpunkte.
	 * 
	 * @param pointInput
	 *            Die Liste der gegebenen Punkte
	 * @return eine Liste der Randpunkte
	 */
	public Collection<Point> getBorderPoints(Collection<Point> pointInput) {
		if (pointInput.size() < 5) return pointInput;

		LinkedList<Point> points = new LinkedList<Point>(pointInput);
		HashSet<Point> convexHull = new HashSet<Point>(points.size()*2);

		List<Point> initHull = getInitialhull(points);

		convexHull.addAll(initHull);

		Collection<Point> firstCloud 
		= getAllPointsOver(initHull.get(2), initHull.get(1), initHull.get(0), points);

		Collection<Point> secondCloud 
		= getAllPointsOver(initHull.get(0), initHull.get(1), initHull.get(2), points);

		calculateBorder(initHull.get(2), initHull.get(1), initHull.get(0), firstCloud, convexHull);
		calculateBorder(initHull.get(0), initHull.get(1), initHull.get(2), secondCloud, convexHull);

		return convexHull;
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
	private List<Point> getInitialhull(Collection<Point> points) {

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
		Optional<TwoPointDistance> lrPoints = getMaxDistanceOfFixedDimension(
				leftPoints, rightPoints, false);
		Optional<TwoPointDistance> tlPoints = getMaxDistanceOfFixedDimension(
				topPoints, lowPoints, false);
		Optional<TwoPointDistance> fnPoints = getMaxDistanceOfFixedDimension(
				farPoints, nearPoints, true);

		if (lrPoints.isPresent()) {
			distances.add(lrPoints.get());
		} else {
			distances.add(getMaxDistanceOf(leftPoints, rightPoints));
		}

		if (tlPoints.isPresent()) {
			distances.add(tlPoints.get());
		} else {
			distances.add(getMaxDistanceOf(topPoints, lowPoints));
		}

		if (fnPoints.isPresent()) {
			distances.add(fnPoints.get());
		} else {
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

		List<Point> returnValue = new ArrayList<Point>();

		returnValue.add(maxfirst);
		returnValue.add(maxsecond);
		returnValue.add(maxdistPointLine);

		return returnValue;
	}

	private Optional<TwoPointDistance> getMaxDistanceOfFixedDimension(
			Collection<Point> sideA, Collection<Point> sideB, boolean isZ) {
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

		return distances.stream().max(
				(a, b) -> (int) Math.signum(a.generateDistance()
						- b.generateDistance()));
	}

	private TwoPointDistance getMaxDistanceOf(Collection<Point> sideA,
			Collection<Point> sideB) {
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

	private Collection<Point> getAllEqualX(Collection<Point> points, Point left) {
		return points.stream().filter(a -> a.getX() == left.getX())
				.collect(Collectors.toCollection(() -> new ArrayList<>()));
	}

	private Collection<Point> getAllEqualY(Collection<Point> points, Point left) {
		return points.stream().filter(a -> a.getY() == left.getY())
				.collect(Collectors.toCollection(() -> new ArrayList<>()));
	}

	private Collection<Point> getAllEqualZ(Collection<Point> points, Point left) {
		return points.stream().filter(a -> a.getZ() == left.getZ())
				.collect(Collectors.toCollection(() -> new ArrayList<>()));
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
	 * der 3-Dimensionalen Punktwolke berechnet.
	 * 
	 * @param left
	 *            linker Punkt der aufzuspannenden Linie
	 * @param right
	 *            rechter Punkt der aufzuspannenden Linie
	 * @param points
	 *            momentane Liste der Punkte
	 * @param convexHull
	 *            Liste der Randpunkte
	 */
	protected void calculateBorder(Point left, Point right, Point far,
			Collection<Point> points, Collection<Point> convexHull) {

		//printPointsAndShape(left, right, far, points);

		if (points.size() < 4) {
			convexHull.addAll(points);
			return;
		}

		Point uppest = getUppestPoint(left, right, far, points);

		convexHull.add(uppest);

		Collection<Point> rightUpperList = getAllPointsOver(uppest, far, left,
				points);

		Collection<Point> leftUpperList = getAllPointsOver(left, right, uppest,
				points);

		Collection<Point> farUpperList = getAllPointsOver(right, far, uppest,
				points);

		calculateBorder(uppest, far, left, rightUpperList, convexHull);
		calculateBorder(left, right, uppest, leftUpperList, convexHull);
		calculateBorder(right, far, uppest, farUpperList, convexHull);
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
	protected Point getUppestPoint(Point left, Point right, Point far,
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
	protected Collection<Point> getAllPointsOver(Point left, Point right,
			Point far, Collection<Point> points) {

		LinkedList<Point> retVal = new LinkedList<Point>();

		points.stream()
				.filter(currPoint -> getDifferenceFromNormal(left, right, far,
						currPoint) > 0)
				.filter(currPoint -> currPoint.getOwner() == null
						|| currPoint.getOwner().equals(points))
				.forEach(currPoint -> {
					retVal.add(currPoint);
					currPoint.setOwner(retVal);
				});

		return retVal;
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
	private double getDifferenceFromNormal(Point left, Point right, Point far, Point x) {
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
