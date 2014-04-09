package main;

import java.util.Collection;

/**
 * Abstrakte Klasse die einige Methoden für alle QuickHull implementierungen
 * bereitstellt, sowie die Methode getBorderPoints definiert, welche die Punkte
 * berechnet, welche die konvexe Hülle der Punkte darstellt
 * 
 * @author Maximilian Schempp
 * 
 */
public abstract class QuickHull {

	/**
	 * Berechnet die Punkte der konvexen Hüllen zu der Punktwolke, welche durch
	 * pointSetInput definiert wird.
	 * 
	 * @param pointSetInput
	 * @return
	 */
	public abstract Collection<Point> getBorderPoints(Collection<Point> pointSetInput);

	/**
	 * bestimmt den Punkt aus der Liste der am weitesten links liegt, also den
	 * kleinsten x-Wert hat
	 * 
	 * @param pointSet
	 *            die Liste mit den Punkten
	 * @return der Punkt mit dem geringsten x-Wert
	 */
	protected Point getRightSidePoint(Collection<Point> pointSet) {
		return pointSet.stream().max((a,b) -> (int) Math.signum(a.getX() - b.getX())).get();
	}

	/**
	 * bestimmt den Punt aus der Liste, welcher am weitesten rechts liegt, also
	 * den größten x-Wert hat
	 * 
	 * @param pointSet
	 *            die Liste der Punkte
	 * @return der Punkt der am weitesten rechts liegt
	 */
	protected Point getLeftSidePoint(Collection<Point> pointSet) {	
		return  pointSet.stream().min((a,b) -> (int) Math.signum(a.getX() - b.getX())).get();
	}
	
	/**
	 * bestimmt den Punt aus der Liste, welcher am weitesten in der Tiefe liegt, also
	 * den größten z-Wert hat
	 * 
	 * @param list
	 *            die Liste der Punkte
	 * @return der Punkt mit dem größten z-Wert
	 */
	protected Point getFarSidePoint(Collection<Point> list) {
		return list.stream().max((a,b) -> (int) Math.floor(a.getZ() - b.getZ())).get();
	}

	/**
	 * bestimmt den Punkt aus der Liste der am nähesten liegt, also den
	 * kleinsten z-Wert hat
	 * 
	 * @param list
	 *            die Liste mit den Punkten
	 * @return der Punkt mit dem geringsten z-Wert
	 */
	protected Point getNearSidePoint(Collection<Point> list) {
		return list.stream().min((a,b) -> (int) Math.floor(a.getZ() - b.getZ())).get();
	}

	/**
	 * bestimmt den Punt aus der Liste, welcher am weitesten oben liegt, also
	 * den größten y-Wert hat
	 * 
	 * @param list
	 *            die Liste der Punkte
	 * @return der Punkt mit dem größten y-Wert
	 */
	protected Point getTopSidePoint(Collection<Point> list) {
		return list.stream().max((a,b) -> (int) Math.floor(a.getY() - b.getY())).get();
	}

	/**
	 * bestimmt den Punt aus der Liste, welcher am weitesten unten liegt, also
	 * den geringsten y-Wert hat
	 * 
	 * @param list
	 *            die Liste der Punkte
	 * @return der Punkt der am weitesten unten liegt
	 */
	protected Point getLowSidePoint(Collection<Point> list) {
		return list.stream().min((a,b) -> (int) Math.floor(a.getY() - b.getY())).get();
	}

}
