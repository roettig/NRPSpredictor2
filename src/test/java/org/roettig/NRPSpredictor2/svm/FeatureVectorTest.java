package org.roettig.NRPSpredictor2.svm;

import static org.junit.Assert.*;

import org.junit.Test;

public class FeatureVectorTest 
{
	@Test
	public void testDistance()
	{
		FeatureVector x = new FeatureVector(new double[]{1,0});
		FeatureVector y = new FeatureVector(new double[]{0,1});
		
		assertEquals(Math.sqrt(2.0),FeatureVector.dist(x, y),1e-8);
		assertEquals(2.0,FeatureVector.distSquared(x, y),1e-8);
	}
}
