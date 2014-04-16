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

	private void printPointsAndShape(Point a, Point b, Point c,
			Collection<Point> points) {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		if (out == null)
			try {
				out = new PrintWriter(timestamp.toString() + ".x3d");
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
		if (pointInput.size() < 5)
			return pointInput;

		Collection<Point> points = initCloud(pointInput);
		Collection<Point> convexHull = initConvexHull(points);
		Collection<Point> initHull = getInitialhull(points);
		convexHull.addAll(initHull);
		
		Iterator<Point> iter = initHull.iterator();		
		Point a = iter.next();
		Point b = iter.next();
		Point c = iter.next();		

		Collection<Point> firstCloud = initCloud();
		Collection<Point> secondCloud = initCloud();		
		
		points.stream().forEach(curPoint -> 
		{
			if(getDifferenceFromNormal(c, b, a, curPoint) > 0) {
				firstCloud.add(curPoint);
			}
			else if(getDifferenceFromNormal(a, b, c, curPoint) > 0) {
				secondCloud.add(curPoint);
			}
		});

		calculateBorder(c, b, a, firstCloud, convexHull);
		calculateBorder(a, b, c, secondCloud, convexHull);

		return convexHull;
	}
	
	private Collection<Point> initConvexHull(Collection<Point> cloud) {
		return new ArrayList<Point>(cloud.size());
	}
	
	private Collection<Point> initCloud() {
		return new ArrayList<Point>();
	}
	
	private Collection<Point> initCloud(Collection<Point> input) {
		return new ArrayList<Point>(input);
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
	private Collection<Point> getInitialhull(Collection<Point> points) {

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
				.max((a, b) -> Double.compare(a.generateDistance()
						, b.generateDistance())).get();

		Point maxfirst = maxDistance.getB();
		Point maxsecond = maxDistance.getA();

		points.remove(maxfirst);
		points.remove(maxsecond);

		// bestimmte den Punkt der am weitesten von der Linie entfernt ist
		Point maxdistPointLine = getMaxDistantPointFromLine(points, maxfirst,
				maxsecond);
		points.remove(maxdistPointLine);

		Collection<Point> cloud = initCloud();

		cloud.add(maxfirst);
		cloud.add(maxsecond);
		cloud.add(maxdistPointLine);

		return cloud;
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
				(a, b) -> Double.compare(a.generateDistance()
						, b.generateDistance()));
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
				.max((a, b) -> Double.compare(a.generateDistance()
						, b.generateDistance())).get();
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
				.max((a, b) -> Double.compare(getDistanceFromLine(u, p, a)
						, getDistanceFromLine(u, p, b))).get();
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
	 *            Liste der konvexen Hülle
	 */
	protected void calculateBorder(Point left, Point right, Point far,
			Collection<Point> points, Collection<Point> convexHull) {

		// printPointsAndShape(left, right, far, points);

		Optional<Point> mdPointOptional = getMostDistantPoint(left, right, far, points);
		if(!mdPointOptional.isPresent()) return;
		
		Point mdPoint = mdPointOptional.get();		
		convexHull.add(mdPoint);

		Collection<Point> firstCloud = initCloud();
		Collection<Point> secondCloud = initCloud();
		Collection<Point> thirdCloud = initCloud();
		
		points.stream().forEach(curPoint -> 
		{
			if(getDifferenceFromNormal(mdPoint, far, left, curPoint) > 0) {
				firstCloud.add(curPoint);
			}
			else if(getDifferenceFromNormal(left, right, mdPoint, curPoint) > 0) {
				secondCloud.add(curPoint);
			}
			else if(getDifferenceFromNormal(right, far, mdPoint, curPoint) > 0) {
				thirdCloud.add(curPoint);
			}
		});

		calculateBorder(mdPoint, far, left, firstCloud, convexHull);
		calculateBorder(left, right, mdPoint, secondCloud, convexHull);
		calculateBorder(right, far, mdPoint, thirdCloud, convexHull);
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
	protected Optional<Point> getMostDistantPoint(Point left, Point right, Point far,
			Collection<Point> points) {
		
		return points
				.stream().filter(a -> !a.equals(left) && !a.equals(right) && !a.equals(far))
				.max((a, b) -> Double.compare(
						getDifferenceFromNormal(left, right, far, a), 
						getDifferenceFromNormal(left, right, far, b)));
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
	protected double getDifferenceFromNormal(Point left, Point right, Point far,
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
