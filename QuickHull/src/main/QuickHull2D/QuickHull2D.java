package main.QuickHull2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import main.QuickHull.Point;
import main.QuickHull.QuickHull;

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
	public Collection<Point> getBorderPoints(Collection<Point> pointsInput) {
		if (pointsInput.size() < 4)
			return pointsInput;

		List<Point> borderPoints = new LinkedList<Point>();
		List<Point> points = new LinkedList<Point>(pointsInput);

		Point leftSidePoint = getLeftSidePoint(points);
		Point rightSidePoint = getRightSidePoint(points);

		splitInUpperAndLower(leftSidePoint, rightSidePoint, points,
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
	 * @param left
	 *            der Punkt de am weitesten links liegt
	 * @param right
	 *            der Punkt der am weitesten rechts liegt
	 * @param points
	 *            die Liste der Punkte
	 * @param borderPoints
	 *            die zu befüllende Liste, welche die Randpunkte darstellt
	 */
	private void splitInUpperAndLower(Point left, Point right,
			Collection<Point> points, List<Point> borderPoints) {

		int pointsSize = points.size();

		List<Point> upperSet = new ArrayList<Point>(pointsSize);
		List<Point> lowerSet = new ArrayList<Point>(pointsSize);

		points.stream().forEach(current -> {
			double diffFromNormal = getDifferenceFromNormal(left, right, current);
			
			if (diffFromNormal > 0) {
				upperSet.add(current);
			} else if (diffFromNormal < 0){
				lowerSet.add(current);
			}
		});

		borderPoints.add(left);
		calculateBorder(left, right, upperSet, borderPoints);

		borderPoints.add(right);
		calculateBorder(right, left, lowerSet, borderPoints);
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
	 * @param check
	 *            Punkt, dessen Abstand berechnet werden soll.
	 * @return
	 */
	private double getDifferenceFromNormal(Point a, Point b, Point check) {
		// normale
		double nx = -(b.getY() - a.getY());
		double ny = b.getX() - a.getX();
		// x - p
		double Xaminusp = check.getX() - a.getX();
		double Yaminusp = check.getY() - a.getY();
		// (x-p) * n = 0 ==> falls punkt auf gerade
		return Xaminusp * nx + Yaminusp * ny;
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
	protected void calculateBorder(Point left, Point right, List<Point> points,
			List<Point> borderPoints) {

		if (points.size() == 0)
			return;

		Point uppestPoint = getUppestPoint(left, right, points);

		List<Point> leftUpperSet = getAllPointsOver(left, uppestPoint, points);
		calculateBorder(left, uppestPoint, leftUpperSet, borderPoints);

		borderPoints.add(uppestPoint);

		List<Point> rightUpperSet = getAllPointsOver(uppestPoint, right, points);
		calculateBorder(uppestPoint, right, rightUpperSet, borderPoints);
	}

	/**
	 * Bestimmt alle Punkte, welcher oberhalb einer Linie liegen, die durch zwei
	 * Punkte aufgespannt wird.
	 * 
	 * @param left
	 *            linker Punkt der Linie
	 * @param right
	 *            recher Punkt der Linie
	 * @param points
	 *            Liste der Punkte
	 * @return Liste der Punkte die oberhalb der Linie liegen
	 */
	protected List<Point> getAllPointsOver(Point left, Point right,
			List<Point> points) {
		return points
				.stream()
				.filter(p -> getDifferenceFromNormal(left, right, p) > 0)
				.collect(
						Collectors.toCollection(() -> new ArrayList<Point>(
								points.size())));
	}

	/**
	 * Bestimmt den Punkt aus einer Punktliste, welcher im 2-Dimensionalen Raum
	 * den größten am weitesten von der Linie entfernt ist, welcher von zwei
	 * Punkten aufgespannt wird
	 * 
	 * @param left
	 *            linker Punkt der Linie
	 * @param right
	 *            rechter Punkt der Linie
	 * @param points
	 *            die Punktliste
	 * @return der Punkt der am weitesten von der Linie entfernt ist
	 */
	protected Point getUppestPoint(Point left, Point right, List<Point> points) {
		return points
				.stream()
				.max((a, b) -> (int) Math.signum(getDifferenceFromNormal(left,
						right, a) - getDifferenceFromNormal(left, right, b)))
				.get();
	}
}
