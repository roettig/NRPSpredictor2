package org.roettig.NRPSpredictor2.extraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class ADomSigExtractorTest 
{
	@Test
	public void testADomExtraction()
	throws
		Exception
	{
		ADomSigExtractor e = new ADomSigExtractor();
		
		e.setADomainTopline(   "KGVmveHrnvvnlvkwlneryflfgeeddllgesdrvLqfssAysFDaSvweifgaLLnGgtLVivpkefsetrlDpeaLaalieregiTvlnltPsllnllldaaeeatpdfapedlssLrrvlvGGEaLspslarrlrerfpdragvrliNaYGPTEtTVcaTi");
		e.setADomainDownline(  "KGVMVGQTAIVNRLLWMQN---HYPLTG-----EDVVAQKTP-CSFDVSVWEFFWPFIAGAKLVMAEPE---AHRDPLAMQQFFAEYGVTTTHFVPSMLAAFVASL---TPQTARQSCVTLKQVFCSGEALPADLCREWQQLTG--A--PLHNLYGPTEAAVDVSW");
        
		e.setLysDomainTopline( "daaeLRahLaarLPdYMVPsaVkfvvLdalPLTpNGKlDRkALPaPdaaa");
        e.setLysDomainDownline("DTSALQAQLRETLPPHMVPVV--LLQLPQLPLSANGKLDRKALPLPELKA");
		
        e.extract();
		
        assertEquals("RWMTFDVSVWEWHFFCSGEHNLYGPTEAAVDVSW",e.get8ASignature());
        assertEquals("DVWHFSLVDK",e.getStachelhausCode());
	}
}
