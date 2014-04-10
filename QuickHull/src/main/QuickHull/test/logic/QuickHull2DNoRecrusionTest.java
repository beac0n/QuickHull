package main.QuickHull.test.logic;

import main.QuickHull.QuickHull;
import main.QuickHull2D.QuickHull2DNoRecrusion;

public class QuickHull2DNoRecrusionTest extends QuickHull2DTest {

	{
		 outFilePre = "_noRecrusion_";
	}
	
	protected QuickHull createQuickHullObject() {
		return new QuickHull2DNoRecrusion();
	}
}
