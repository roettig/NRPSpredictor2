package org.roettig.NRPSpredictor2.svm;

import static org.junit.Assert.*;

import org.junit.Test;

public class LinearKernelTest 
{
	@Test
	public void testBasic()
	{
		FeatureVector x = new FeatureVector(new double[]{1,0});
		FeatureVector y = new FeatureVector(new double[]{0,1});
		
		LinearKernel lk = new LinearKernel();
		
		assertEquals(0.0,lk.compute(x, y),1e-8);
		
		x = new FeatureVector(new double[]{1,1});
		y = new FeatureVector(new double[]{1,1});

		assertEquals(2.0,lk.compute(x, y),1e-8);
	}
}
