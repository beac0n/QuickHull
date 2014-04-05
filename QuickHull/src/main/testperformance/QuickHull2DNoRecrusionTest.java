package main.testperformance;

import main.QuickHull;
import main.QuickHull2DNoRecrusion;

public class QuickHull2DNoRecrusionTest extends QuickHull2DTest {
	
	protected QuickHull createQuickHullObject() {
		return new QuickHull2DNoRecrusion();
	}	
}
