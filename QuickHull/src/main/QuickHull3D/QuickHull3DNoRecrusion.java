package main.QuickHull3D;

import java.util.Collection;
import java.util.LinkedList;
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
			Point curRight = curFrame.getRight();
			Point curFar = curFrame.getFar();
			Collection<Point> curPoints = curFrame.getPoints();			
			
			if (curPoints.size() < 4) {
				convexHull.addAll(curPoints);
				continue;
			}

			Point mdPoint = getMostDistantPoint(curLeft, curRight, curFar, curPoints);

			convexHull.add(mdPoint);
			
			Collection<Point> firstCloud = new LinkedList<Point>();
			Collection<Point> secondCloud = new LinkedList<Point>();
			Collection<Point> thirdCloud = new LinkedList<Point>();
			
			curPoints.stream().forEach(curPoint -> 
			{
				if(getDifferenceFromNormal(mdPoint, curFar, curLeft, curPoint) > 0) {
					firstCloud.add(curPoint);
				}
				else if(getDifferenceFromNormal(curLeft, curRight, mdPoint, curPoint) > 0) {
					secondCloud.add(curPoint);
				}
				else if(getDifferenceFromNormal(curRight, curFar, mdPoint, curPoint) > 0) {
					thirdCloud.add(curPoint);
				}
			});
			
			stack.push(new StackFrameQuickHull3D(curRight, curFar, mdPoint, thirdCloud));
			stack.push(new StackFrameQuickHull3D(curLeft, curRight, mdPoint, secondCloud));
			stack.push(new StackFrameQuickHull3D(mdPoint, curFar, curLeft, firstCloud));
		}
	}
}
