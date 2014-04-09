package main;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse, welche das Berechnen der Randpunkte einer Punktwolke im
 * 2-Dimensionalen Raum kapselt
 * 
 * @author Maximilian Schempp
 * 
 */
public class QuickHull2D extends QuickHull {

	/**
	 * Liefert für eine gegebene Liste an Punkten alle Randpunkte
	 * 
	 * @param pointSetInput
	 *            Die Liste der gegebenen Punkte
	 * @return eine Liste der Randpunkte
	 */
	public List<Point> getBorderPoints(List<Point> pointSetInput) {
		List<Point> pointSet = new LinkedList<Point>(pointSetInput);

		List<Point> borderPoints = new LinkedList<Point>();

		Point leftSidePoint = getLeftSidePoint(pointSet);
		Point rightSidePoint = getRightSidePoint(pointSet);

		splitInUpperAndLower(leftSidePoint, rightSidePoint, pointSet,
				borderPoints);

		return borderPoints;
	}

	/**
	 * Teilt eine gegebene Liste an Punkten in zwei weitere Listen auf, welche
	 * zum einen die oberen Punkte repräsentiert und zum anderen die unteren
	 * Punkte. Die Teilung erfolgt anhand der Linie die von den beiden Punkten,
	 * die am weistesten rechts bzw. links liegen, aufgespannt wird. Durch diese
	 * beiden Listen werden dann mit dem Quickhull-Algorithmus alle Randpunkte
	 * berechnet. Die Randpunkte werden in einer übergebenen Liste gespeichert.
	 * 
	 * @param leftSidePoint
	 *            der Punkt de am weitesten links liegt
	 * @param rightSidePoint
	 *            der Punkt der am weitesten rechts liegt
	 * @param currentPointSet
	 *            die Liste der Punkte
	 * @param borderPoints
	 *            die zu befüllende Liste, welche die Randpunkte darstellt
	 */
	private void splitInUpperAndLower(Point leftSidePoint,
			Point rightSidePoint, List<Point> currentPointSet,
			List<Point> borderPoints) {

		currentPointSet.remove(leftSidePoint);
		currentPointSet.remove(rightSidePoint);

		List<Point> upperSet = new LinkedList<>();
		List<Point> lowerSet = new LinkedList<>();

		Iterator<Point> iter = currentPointSet.iterator();

		while (iter.hasNext()) {
			Point current = iter.next();

			if (getDifferenceFromNormal(leftSidePoint, rightSidePoint, current) > 0) {
				upperSet.add(current);
			} else {
				lowerSet.add(current);
			}
		}

		borderPoints.add(leftSidePoint);
		calculateBorder(leftSidePoint, rightSidePoint, upperSet, borderPoints);

		borderPoints.add(rightSidePoint);
		calculateBorder(rightSidePoint, leftSidePoint, lowerSet, borderPoints);
	}

	/**
	 * Berechnet wie weit ein gegebener Punkt von der Linie entfernt liegt, die
	 * durch die anderen beiden übergebenen Punkte aufgespannt wird. Der
	 * Rückgabewert ist ein Maß für die Distanz. Je weiter der Wert von 0
	 * entfernt ist, desto weiter ist der Punkt von der Linie entfernt.
	 * 
	 * @param a
	 *            erster Punkt um Linie aufzuspannen
	 * @param b
	 *            zweiter Punkt um Linie aufzuspannen
	 * @param upperCheck
	 *            Punkt, dessen Abstand berechnet werden soll.
	 * @return
	 */
	private double getDifferenceFromNormal(Point a, Point b, Point upperCheck) {
		// normale
		double nx = -(b.getY() - a.getY());
		double ny = b.getX() - a.getX();
		// x - p
		double Xaminusp = upperCheck.getX() - a.getX();
		double Yaminusp = upperCheck.getY() - a.getY();
		// (x-p) * n = 0 ==> falls punkt auf gerade
		return Xaminusp * nx + Yaminusp * ny;
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
	protected void calculateBorder(Point leftSidePoint, Point rightSidePoint,
			List<Point> pointSet, List<Point> borderPoints) {

		pointSet.remove(leftSidePoint);
		pointSet.remove(rightSidePoint);

		if (pointSet.size() == 0)
			return;

		Point uppestPoint = getUppestPoint(leftSidePoint, rightSidePoint,
				pointSet);
		pointSet.remove(uppestPoint);

		List<Point> leftUpperSet = getAllPointsOver(leftSidePoint, uppestPoint,
				pointSet);
		calculateBorder(leftSidePoint, uppestPoint, leftUpperSet, borderPoints);

		borderPoints.add(uppestPoint);

		List<Point> rightUpperSet = getAllPointsOver(uppestPoint,
				rightSidePoint, pointSet);
		calculateBorder(uppestPoint, rightSidePoint, rightUpperSet,
				borderPoints);
	}

	/**
	 * Bestimmt alle Punkte, welcher oberhalb einer Linie liegen, die durch zwei
	 * Punkte aufgespannt wird.
	 * 
	 * @param leftSidePoint
	 *            linker Punkt der Linie
	 * @param rightSidePoint
	 *            recher Punkt der Linie
	 * @param upperSet
	 *            Lister der Punkte
	 * @return Liste der Punkte die oberhalb der Linie liegen
	 */
	protected List<Point> getAllPointsOver(Point leftSidePoint,
			Point rightSidePoint, List<Point> upperSet) {
		List<Point> returnValue = new LinkedList<Point>();

		Iterator<Point> iter = upperSet.iterator();

		while (iter.hasNext()) {
			Point currPoint = iter.next();
			double differenceFromNormal = getDifferenceFromNormal(
					leftSidePoint, rightSidePoint, currPoint);

			if (differenceFromNormal > 0) {
				returnValue.add(currPoint);
			}
		}

		return returnValue;
	}

	/**
	 * Bestimmt den Punkt aus einer Punktliste, welcher im 2-Dimensionalen Raum
	 * den größten am weitesten von der Linie entfernt ist, welcher von zwei
	 * Punkten aufgespannt wird
	 * 
	 * @param leftSidePoint
	 *            linker Punkt der Linie
	 * @param rightSidePoint
	 *            rechter Punkt der Linie
	 * @param currentPointSet
	 *            die Punktliste
	 * @return der Punkt der am weitesten von der Linie entfernt ist
	 */
	protected Point getUppestPoint(Point leftSidePoint, Point rightSidePoint,
			List<Point> currentPointSet) {

		Point uppestPoint = null;

		double oldDifferenceFromNormal = -1;

		Iterator<Point> iter = currentPointSet.iterator();

		while (iter.hasNext()) {
			Point currentPoint = iter.next();

			double currentDifferenceFromNormal = getDifferenceFromNormal(
					leftSidePoint, rightSidePoint, currentPoint);
			if (currentDifferenceFromNormal > oldDifferenceFromNormal) {
				uppestPoint = currentPoint;
				oldDifferenceFromNormal = currentDifferenceFromNormal;
			}
		}

		return uppestPoint;
	}
}
