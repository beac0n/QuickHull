package main.QuickHull.test.logic;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.QuickHull.Point;
import main.QuickHull.QuickHull;
import main.QuickHull3D.Point3D;

public abstract class QuickHullTest {

	private Color backgroundColor = new Color(0, 0, 0);
	private Color normalPointsColor = new Color(255, 0, 0);
	private Color borderPointsColor = new Color(255, 255, 255);

	protected int pointCap = 10_000;
	protected int pointCapHalf = pointCap / 2;

	protected String outFilePre = "_";

	protected abstract QuickHull createQuickHullObject();

	private String getX3DcolorString(Color color) {
		String returnValue = String.valueOf(color.getRed() / 255) + " ";
		returnValue += String.valueOf(color.getGreen() / 255) + " ";
		returnValue += String.valueOf(color.getBlue() / 255);

		return returnValue;
	}

	private String getX3DfileHeadWithColor(Color color) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <!DOCTYPE X3D PUBLIC \"ISO//Web3D//DTD X3D 3.0//EN\" \"http://www.web3d.org/specifications/x3d-3.0.dtd\">"
				+ "<X3D profile='Interchange' version='3.0'  xmlns:xsd='http://www.w3.org/2001/XMLSchema-instance' xsd:noNamespaceSchemaLocation "
				+ "=' http://www.web3d.org/specifications/x3d-3.0.xsd '> <Scene> <Background groundColor='1 1 1' skyColor='"
				+ getX3DcolorString(backgroundColor)
				+ "'/> "
				+ "<NavigationInfo type='\"EXAMINE\" \"WALK\" \"FLY\" \"ANY\"\'/>	<Shape> <Appearance> <Material emissiveColor='"
				+ getX3DcolorString(color)
				+ "'/> </Appearance> <PointSet> <Coordinate point='";
	}

	protected Collection<Point> getBorderAndWriteToFile(
	Collection<Point> points, String filename)
	throws FileNotFoundException {
		return getBorderAndWriteToFile(points, filename, true);
	}

	protected Collection<Point> getBorderAndWriteToFile(
			Collection<Point> points, String filename, boolean writeToFile)
			throws FileNotFoundException {
		QuickHull qh = createQuickHullObject();

		PrintWriter out = new PrintWriter(filename + ".x3d");

		String x3dFileHead = getX3DfileHeadWithColor(normalPointsColor);

		out.print(x3dFileHead);

		out.flush();

		System.out.print("now calculating convex hull... ");
		long startTime = System.nanoTime();
		Collection<Point> border = qh.getBorderPoints(points);
		long endTime = System.nanoTime();
		double timeInMs = (endTime - startTime) / 1_000_000d;

		if(writeToFile) {
		
		System.out.print("convex hull calculated, took " + timeInMs
				+ " ms, now writing to " + filename + ".x3d... ");
		Iterator<Point> iter = points.iterator();

		while (iter.hasNext()) {
			Point cur = iter.next();
			out.print(cur.getX() + " " + cur.getY() + " " + cur.getZ() + " ");
			out.flush();
		}

		String x3dFileMiddle = "'/> </PointSet> </Shape>	<Shape>	<Appearance> <Material emissiveColor='"
				+ getX3DcolorString(borderPointsColor)
				+ "'/> </Appearance> <PointSet>	<Coordinate point='";

		out.print(x3dFileMiddle);

		writePointsToWriter(out, border);

		String x3dFileTail = "'/> </PointSet> </Shape> ";
		String x3dFileEnd = "</Scene></X3D>";

		out.print(x3dFileTail + x3dFileEnd);
		out.flush();
		out.close();

		writePointsToX3Dfile(filename + "_borders", x3dFileHead, border,
				x3dFileTail, borderPointsColor);

		System.out.println("done");
		}
		
		return border;
	}

	private void writePointsToX3Dfile(String filename, String x3dFileHead,
			Collection<Point> border, String x3dFileTail, Color color)
			throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filename + ".x3d");
		out.print(getX3DfileHeadWithColor(color));
		writePointsToWriter(out, border);
		out.print(x3dFileTail);

		Iterator<Point> iter = border.iterator();

		StringBuilder indexString = new StringBuilder();
		StringBuilder colorsString = new StringBuilder();
		StringBuilder pointString = new StringBuilder();

		String oneoneone = "1 1 1";

		int i = 0;

		while (iter.hasNext()) {
			Point curr = iter.next();

			String iSpace = i + " ";

			indexString.append(iSpace);

			if (iter.hasNext()) {
				colorsString.append(oneoneone + ", ");
				pointString.append(curr.getX() + " " + curr.getY() + " "
						+ curr.getZ() + ", ");
			} else {
				colorsString.append(oneoneone);
				pointString.append(curr.getX() + " " + curr.getY() + " "
						+ curr.getZ());
			}

			++i;
			out.flush();
		}

		out.write("<Shape><IndexedLineSet coordIndex=\"");
		out.write(indexString.toString());
		out.flush();
		out.write("0\" colorIndex=\"");
		out.write(indexString.toString());
		out.flush();
		out.write("0\"><Color color=\"");
		out.write(colorsString.toString());
		out.flush();
		out.write("\"/><Coordinate point=\"");
		out.write(pointString.toString());
		out.flush();
		out.write("\"/></IndexedLineSet></Shape>");

		out.write("</Scene></X3D>");

		out.flush();
		out.close();
	}

	private void writePointsToWriter(PrintWriter out, Collection<Point> border) {
		Iterator<Point> iter;
		iter = border.iterator();

		while (iter.hasNext()) {
			Point cur = iter.next();
			out.print(cur.getX() + " " + cur.getY() + " " + cur.getZ() + " ");
			out.flush();
		}
	}

	protected HashSet<Point> getRandomSphereGaussianPoints(long pointsCount) {
		HashSet<Point> pointList = new HashSet<Point>();

		Random rand = new Random();

		for (int i = 0; i < pointsCount; ++i) {
			double tempX = rand.nextGaussian() * 10000;
			double tempY = rand.nextGaussian() * 10000;
			double tempZ = rand.nextGaussian() * 10000;

			double len = Math.sqrt(tempX * tempX + tempY * tempY + tempZ
					* tempZ);

			tempX /= len;
			tempY /= len;
			tempZ /= len;

			pointList.add(new Point3D(tempX, tempY, tempZ));
		}

		return pointList;
	}

	protected List<Point> getRandomSpecialSphereGaussianPoints(
			long pointsCount, boolean is3D) {
		List<Point> pointList = new LinkedList<Point>();

		Random rand = new Random();

		for (int i = 0; i < pointsCount; ++i) {
			double tempX = rand.nextGaussian();
			double tempY = rand.nextGaussian();
			double tempZ = 1.0;
			if (is3D)
				tempZ = rand.nextGaussian();

			double len = Math.sqrt(tempX * tempX + tempY * tempY);
			if (is3D)
				len = Math.sqrt(tempX * tempX + tempY * tempY + tempZ * tempZ);

			tempX /= len;
			tempY /= len;
			if (is3D)
				tempZ /= len;

			double endZ = tempZ * tempY;
			if (!is3D)
				endZ = 1;

			pointList.add(new Point3D(tempX * tempY, tempX * tempZ, endZ));
		}

		return pointList;
	}

	protected List<Point> getRandomGaussianPoints(int count, boolean is3D) {
		List<Point> pointList = new LinkedList<Point>();

		Random rand = new Random();

		long stretcher = 10000;

		for (int i = 0; i < count; ++i) {
			double tempX = rand.nextGaussian() * stretcher;
			double tempY = rand.nextGaussian() * stretcher;
			double tempZ = 1.0;

			if (is3D) {
				tempZ = rand.nextGaussian() * stretcher;
			}

			pointList.add(new Point3D(tempX, tempY, tempZ));
		}

		return pointList;
	}
}
