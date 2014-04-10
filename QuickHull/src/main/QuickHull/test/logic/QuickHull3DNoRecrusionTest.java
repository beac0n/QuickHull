package main.QuickHull.test.logic;

import main.QuickHull.QuickHull;
import main.QuickHull3D.QuickHull3DNoRecrusion;

public class QuickHull3DNoRecrusionTest extends QuickHull3DTest {

	{
		 outFilePre = "_noRecrusion_";
	}
	
	protected QuickHull createQuickHullObject() {
		return new QuickHull3DNoRecrusion();
	}
}
