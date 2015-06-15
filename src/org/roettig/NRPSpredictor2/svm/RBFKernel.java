package org.roettig.NRPSpredictor2.svm;

/**
 * Implements a radial basis function kernel in eucidean space of feature vector.
 *  
 * @author roettig
 *
 */
public class RBFKernel 
implements 
	KernelFunction
{
	/**
	 * The RBF parameter gamma.
	 */
	private double gamma;
	
	/**
	 * Ctor with desired argument for gamma parameter.
	 *  
	 * @param gamma
	 */
	public RBFKernel(double gamma)
	{
		this.gamma = gamma;
	}
	
	@Override
	public double compute(FeatureVector x, FeatureVector y)
	{
		double squaredDistance = FeatureVector.distSquared(x, y);
		return Math.exp( -gamma*squaredDistance);
	}
	
	/**
	 * Gets the value of the RBF parameter gamma.
	 * 
	 * @return
	 */
	public double getGamma()
	{
		return gamma;
	}

	/**
	 * Sets the value of the RBF parameter gamma.
	 * 
	 * @return
	 */
	public void setGamma(double gamma)
	{
		this.gamma = gamma;
	}
	
}
