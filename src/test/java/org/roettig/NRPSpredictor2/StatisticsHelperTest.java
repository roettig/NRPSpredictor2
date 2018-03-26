package org.roettig.NRPSpredictor2;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.roettig.NRPSpredictor2.encoder.PrimalWoldEncoder;
import org.roettig.NRPSpredictor2.util.StatisticsHelper;

public class StatisticsHelperTest 
{
	@Test
	public void testMean()
	{
		assertEquals(0.001923076923076976, StatisticsHelper.mean(PrimalWoldEncoder.Z1),1e-8);
		assertEquals(0.0011538461538461635, StatisticsHelper.mean(PrimalWoldEncoder.Z2),1e-8);
		assertEquals(0.0015384615384615096, StatisticsHelper.mean(PrimalWoldEncoder.Z3),1e-8);
	}
	
	@Test
	public void testStdev()
	{
		assertEquals(2.6160275521955336, StatisticsHelper.stddev(PrimalWoldEncoder.Z1),1e-8);
		assertEquals(1.8589595518420015, StatisticsHelper.stddev(PrimalWoldEncoder.Z2),1e-8);
		assertEquals(1.545268112160973, StatisticsHelper.stddev(PrimalWoldEncoder.Z3),1e-8);
	}
}
