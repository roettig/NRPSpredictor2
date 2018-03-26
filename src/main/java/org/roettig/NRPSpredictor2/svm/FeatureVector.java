package org.roettig.NRPSpredictor2.svm;

public class FeatureVector
{
	/**
	 * Double array of feature values.
	 */
	private double[] feats;
	
	/**
	 * Ctor with desires double array of feature values.
	 * 
	 * @param feats double array of feature values
	 */
	public FeatureVector(double[] feats)
	{
		this.feats = feats;
	}
	
	/**
	 * Ctor with desires dimension of feature values.
	 * 
	 * @param n dimension
	 */
	public FeatureVector(int n)
	{
		this.feats = new double[n];
	}
	
	/**
	 * Gets the feature values as double array.
	 *  
	 * @return
	 */
	public double[] getFeatures()
	{
		return feats;
	}
	
	/**
	 * Sets the i-th feature value.
	 * 
	 * @param i
	 * @param value
	 */
	public void setFeatureValue(int i, double value)
	{
		if(i<0 || i>feats.length)
			throw new IllegalArgumentException("Feature index out of bounds.");
		
		feats[i] = value;
	}

	/**
	 * Get the i-th feature value.
	 * 
	 * @param i
	 * 
	 * @return
	 */
	public double getFeatureValue(int i)
	{
		if(i<0 || i>feats.length)
			throw new IllegalArgumentException("Feature index out of bounds.");
		
		return feats[i];
	}
	
	/**
	 * Static helper method to calculate the euclidean distance between
	 * two feature vectors.
	 *  
	 * @param v1 first feature vector
	 * @param v2 second feature vector
	 * 
	 * @return
	 */
	public final static double dist(FeatureVector v1, FeatureVector v2)
	{
		return Math.sqrt(distSquared(v1, v2));
	}
	
	/**
	 * Static helper method to calculate the squared euclidean distance between
	 * two feature vectors.
	 *  
	 * @param v1 first feature vector
	 * @param v2 second feature vector
	 * 
	 * @return
	 */
	public final static double distSquared(FeatureVector v1, FeatureVector v2)
	{
		double[] f1 = v1.getFeatures();
		double[] f2 = v2.getFeatures();

		if(f1.length!=f2.length)
		{
			throw new IllegalArgumentException("FectureVectors do not have the same length.");
		}
		
		double sum = 0.0;
		
		for(int i=0;i<f1.length;i++)
		{
			double d = f1[i]-f2[i]; 
			sum += d*d;
		}	
		
		return sum;
	}
	
	/**
	 * Make a feature vector from the given double array.
	 * 
	 * @param fts double arrayof feature values
	 * 
	 * @return
	 */
	public static FeatureVector makeFVec(double[] fts)
	{
		return new FeatureVector(fts);
	}
	
	/**
	 * Computes the similarity of two feature vectors using the inner product
	 * or scalar product of eclidean space.
	 * 
	 * @param x first feature vector
	 * @param y second feature vector
	 * 
	 * @return
	 */
	public static double getSimilarity(FeatureVector x, FeatureVector y)
	{
		double[] f1 = x.getFeatures();
		double[] f2 = y.getFeatures();

		if(f1.length!=f2.length)
		{
			throw new IllegalArgumentException("FectureVectors do not have the same length.");
		}
		
		double sum = 0;
		
		for(int i=0;i<f1.length;i++)
			sum += f1[i]*f2[i];
		
		return sum;
	}
}