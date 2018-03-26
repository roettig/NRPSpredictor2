package org.roettig.NRPSpredictor2.svm;

/**
 * Interface of a kernel function.
 * 
 * @author roettig
 *
 */
public interface KernelFunction
{
	/**
	 * Computes the kernel value for two given feature vectors.
	 * 
	 * @param v1 first feature vector
	 * @param v2 second feature vector
	 * @return
	 */
	double compute(FeatureVector x, FeatureVector y);
}
