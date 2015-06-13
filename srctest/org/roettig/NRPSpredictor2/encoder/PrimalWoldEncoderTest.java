package org.roettig.NRPSpredictor2.encoder;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimalWoldEncoderTest 
{
	@Test
	public void testBasic()
	{
		PrimalWoldEncoder woldEncoder = new PrimalWoldEncoder();
		
		String sequence = "ACGX-";
		
		double[] fvec = woldEncoder.encode(sequence);
		
		assertEquals(sequence.length()*3,fvec.length);
		
		int idx = 0;
		
		for(int i=0;i<sequence.length();i++)
		{
			assertEquals(fvec[idx++], PrimalWoldEncoder.getZ1Normalized( sequence.charAt(i) ),1e-8);
			assertEquals(fvec[idx++], PrimalWoldEncoder.getZ2Normalized( sequence.charAt(i) ),1e-8);
			assertEquals(fvec[idx++], PrimalWoldEncoder.getZ3Normalized( sequence.charAt(i) ),1e-8);	
		}
	}
}
