package main.QuickHull3D;

import java.util.Collection;
import java.util.Stack;

import main.QuickHull.Point;

public class QuickHull3DNoRecrusion extends QuickHull3D {
	/**
	 * Nicht-rekursiver Teil des Quickhull-Algorithmuss, der nach und nach der konvexen Hülle
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
		
		Stack<StackFrameQuickHull3D> stack = new Stack<StackFrameQuickHull3D>();		
		stack.push(new StackFrameQuickHull3D(left, right, far, points));
		
		StackFrameQuickHull3D curFrame;
		
		while(!stack.isEmpty()) {
			curFrame = stack.pop();
			Point curLeft = curFrame.getLeft();
			Point currRight = curFrame.getRight();
			Point curFar = curFrame.getFar();
			Collection<Point> curPoints = curFrame.getPoints();			
			
			if (curPoints.size() < 4) {
				convexHull.addAll(curPoints);
				continue;
			}

			Point uppest = getUppestPoint(curLeft, currRight, curFar, curPoints);

			convexHull.add(uppest);

			Collection<Point> rightUpperList 
			= getAllPointsOver(uppest, curFar, curLeft, curPoints);

			Collection<Point> leftUpperList 
			= getAllPointsOver(curLeft, currRight, uppest, curPoints);

			Collection<Point> farUpperList 
			= getAllPointsOver(currRight, curFar, uppest, curPoints);
			
			stack.push(new StackFrameQuickHull3D(currRight, curFar, uppest, farUpperList));
			stack.push(new StackFrameQuickHull3D(curLeft, currRight, uppest, leftUpperList));
			stack.push(new StackFrameQuickHull3D(uppest, curFar, curLeft, rightUpperList));
		}
		

	}
}
