package main.QuickHull.test.performance;

import main.QuickHull.QuickHull;
import main.QuickHull3D.QuickHull3DNoRecrusion;

public class QuickHull3DNoRecrusionTest extends QuickHull3DTest {
	
	protected QuickHull createQuickHullObject() {
		return new QuickHull3DNoRecrusion();
	}
}
