package org.roettig.NRPSpredictor2.util;

public class StatisticsHelper 
{
	/**
	 * Computes the unbiased sample mean.
	 * 
	 * @param x array of values 
	 * 
	 * @return
	 */
	public static double mean(double x[])
	{
		double mean = 0;
		
		for(int i=0;i<x.length;i++)
		{
			mean += x[i];
		}
		
		return mean/(x.length);
	}

	/**
	 * Computes the corrected (unbiased) sample variance.
	 * 
	 * @param x array of values 
	 * 
	 * @return
	 */
	public static double var(double x[])
	{
		double var = 0;
		
		double mean = mean(x);
		
		for(int i=0;i<x.length;i++)
		{
			var += (x[i] -mean)*(x[i] -mean);
		}
		
		return var/(x.length-1);
	}
	
	/**
	 * Computes the corrected (unbiased) sample standard deviation.
	 * 
	 * @param x array of values
	 * 
	 * @return
	 */
	public static double stddev(double x[])
	{
		return Math.sqrt(var(x));
	}
}
