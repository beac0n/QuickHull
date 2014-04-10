package main.QuickHull.test.performance;

import main.QuickHull.QuickHull;
import main.QuickHull2D.QuickHull2DNoRecrusion;

public class QuickHull2DNoRecrusionTest extends QuickHull2DTest {
	
	protected QuickHull createQuickHullObject() {
		return new QuickHull2DNoRecrusion();
	}	
}
