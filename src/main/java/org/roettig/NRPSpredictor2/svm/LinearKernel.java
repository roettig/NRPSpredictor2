package org.roettig.NRPSpredictor2.svm;

/**
 * Implements a linear kernel function in eucidean space of feature vector.
 *  
 * @author roettig
 *
 */
public class LinearKernel 
implements 
	KernelFunction
{

	@Override
	public double compute(FeatureVector x, FeatureVector y)
	{
		return FeatureVector.getSimilarity(x, y);
	}

}
