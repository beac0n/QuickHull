package main.QuickHull2D;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

import main.QuickHull.Point;

public class QuickHull2DNoRecrusion extends QuickHull2D {

	/**
	 * Teil des Quickhull-Algorithmuss, der nach und nach den Rand der
	 * 2-Dimensionalen Punktwolke berechnet.
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
	@Override
	protected void calculateBorder(Point left, Point right,
			List<Point> points, List<Point> borderPoints) {

		Stack<StackFrameQuickHull2D> stack = new Stack<StackFrameQuickHull2D>();

		stack.push(new StackFrameQuickHull2D(left, right, points, null));

		StackFrameQuickHull2D curFrame;
		Point highPoint;

		while (!stack.isEmpty()) {
			curFrame = stack.pop();
			
			Point curLeft = curFrame.getLeft();
			Point curRight = curFrame.getRight();
			List<Point> curPoints = curFrame.getPoints();

			if (curFrame.getBorderPoint().isPresent()) {
				borderPoints.add(curFrame.getBorderPoint().get());
			}

			curPoints.remove(curLeft);
			curPoints.remove(curRight);

			if (curPoints.size() > 0) {
				highPoint = getUppestPoint(curLeft, curRight, curPoints);
				curPoints.remove(highPoint);

				List<Point> firstSet = getAllPointsOver(curLeft, highPoint, curPoints);

				List<Point> secondSet = getAllPointsOver(highPoint, curRight, curPoints);

				stack.push(new StackFrameQuickHull2D(highPoint, curRight, secondSet, highPoint));
				stack.push(new StackFrameQuickHull2D(curLeft, highPoint, firstSet, null));
			}
		}
	}
}
