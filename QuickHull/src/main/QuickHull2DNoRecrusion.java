package main;

import java.util.List;
import java.util.Optional;
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
		Stack<Optional> borderPointStack = new Stack<Optional>();

		leftSidePointStack.push(leftSidePoint);
		rightSidePointStack.push(rightSidePoint);
		pointSetStack.push(pointSet);
		borderPointStack.push(Optional.empty());

		Point currentLeftSidePoint;
		Point currentRightSidePoint;
		List<Point> currentPointSet;
		Point uppestPoint;
		Optional<Point> currBorderPoint;
		
		while (!leftSidePointStack.isEmpty()) {
			currentLeftSidePoint = leftSidePointStack.pop();
			currentRightSidePoint = rightSidePointStack.pop();
			currentPointSet = pointSetStack.pop();
			currBorderPoint = borderPointStack.pop();
			
			if(currBorderPoint.isPresent()) {
				borderPoints.add(currBorderPoint.get());
			}

			currentPointSet.remove(currentLeftSidePoint);
			currentPointSet.remove(currentRightSidePoint);

			if (currentPointSet.size() > 0) {

				uppestPoint = getUppestPoint(currentLeftSidePoint,
						currentRightSidePoint, currentPointSet);
				currentPointSet.remove(uppestPoint);

				List<Point> leftUpperSet = getAllPointsOver(
						currentLeftSidePoint, uppestPoint, currentPointSet);

				List<Point> rightUpperSet = getAllPointsOver(uppestPoint,
						currentRightSidePoint, currentPointSet);

				// second recursive call
				leftSidePointStack.push(uppestPoint); 
				rightSidePointStack.push(currentRightSidePoint);
				pointSetStack.push(rightUpperSet);
				borderPointStack.push(Optional.of(uppestPoint));

				// first recursive call
				leftSidePointStack.push(currentLeftSidePoint);
				rightSidePointStack.push(uppestPoint);
				pointSetStack.push(leftUpperSet);						
				borderPointStack.push(Optional.empty());
			}
		}
	}
}
