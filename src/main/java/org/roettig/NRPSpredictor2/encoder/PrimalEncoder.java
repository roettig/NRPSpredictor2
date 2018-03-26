package org.roettig.NRPSpredictor2.encoder;

/**
 * This interface gives a feature encoding method
 * for strings (=sequence of characters).
 * 
 * @author roettig
 *
 */
public interface PrimalEncoder
{
	/**
	 * Encode given string as n-dimensional feature vector.
	 * 
	 * @param s the string to encode(character-wise)
	 * 
	 * @return feature vector (as double array)
	 */
	double[] encode(String s);
}
