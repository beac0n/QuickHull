package main;

import java.util.List;
import java.util.Stack;

public class QuickHull2DNoRecrusion extends QuickHull2D {

	/**
	 * Teil des Quickhull-Algorithmuss, der nach und nach den Rand der
	 * 2-Dimensionalen Punktwolke berechnet.
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
	@Override
	protected void calculateBorder(Point leftSidePoint, Point rightSidePoint,
			List<Point> pointSet, List<Point> borderPoints) {
		Stack<Point> leftSidePointStack = new Stack<Point>();
		Stack<Point> rightSidePointStack = new Stack<Point>();
		Stack<List<Point>> pointSetStack = new Stack<List<Point>>();

		leftSidePointStack.push(leftSidePoint);
		rightSidePointStack.push(rightSidePoint);
		pointSetStack.push(pointSet);

		Point currentLeftSidePoint = null;
		Point currentRightSidePoint = null;
		List<Point> currentPointSet = null;
		Point uppestPoint = null;

		while (!leftSidePointStack.isEmpty()) {
			currentLeftSidePoint = leftSidePointStack.pop();
			currentRightSidePoint = rightSidePointStack.pop();
			currentPointSet = pointSetStack.pop();

			currentPointSet.remove(currentLeftSidePoint);
			currentPointSet.remove(currentRightSidePoint);

			if (currentPointSet.size() > 0) {

				uppestPoint = getUppestPoint(currentLeftSidePoint,
						currentRightSidePoint, currentPointSet);
				currentPointSet.remove(uppestPoint);

				List<Point> leftUpperSet = getAllPointsOver(
						currentLeftSidePoint, uppestPoint, currentPointSet);

				borderPoints.add(uppestPoint);

				List<Point> rightUpperSet = getAllPointsOver(uppestPoint,
						currentRightSidePoint, currentPointSet);

				// second recursive call
				leftSidePointStack.push(uppestPoint);
				rightSidePointStack.push(currentRightSidePoint);
				pointSetStack.push(rightUpperSet);

				// first recursive call
				leftSidePointStack.push(currentLeftSidePoint);
				rightSidePointStack.push(uppestPoint);
				pointSetStack.push(leftUpperSet);
			}
		}
	}
}
