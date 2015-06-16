package org.roettig.NRPSpredictor2.hmmer;

import static org.junit.Assert.*;
import java.io.File;
import java.util.List;

import org.junit.Test;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainAlignment;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainHit;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.QueryResult;
import org.roettig.NRPSpredictor2.resources.ResourceManager;
import org.roettig.NRPSpredictor2.util.Helper;

public class HMMerTest
{
	private static float evalue = 0.00001f;
	
	@Test
	public void testOneFullSequence() throws Exception
	{
		HMMPfam hmmpfam = new HMMPfam();
		hmmpfam.debugModeOn();
		File model = Helper.deployFile(ResourceManager.class.getResourceAsStream("aa-activating.aroundLys.hmm"));
		hmmpfam.run( evalue, model, new File("srctest/testdata/one-domain.fa"));
		
		
		List<QueryResult> res = hmmpfam.getResults();
		assertEquals(1,res.size());
		
		QueryResult firstQueryResult = res.get(0);
		
		List<DomainHit> domainHits = firstQueryResult.getHits();
		assertEquals(2,domainHits.size());
		assertEquals(1.555906e-59,domainHits.get(0).evalue,1e-6);
		assertEquals(5.116907E-24,domainHits.get(1).evalue,1e-6);
		
		assertEquals(1.963560e+02,domainHits.get(0).score,1e-6);
		assertEquals(7.837100e+01,domainHits.get(1).score,1e-6);
		
	}
	
	@Test
	public void testTwoFullSequences() throws Exception
	{
		HMMPfam hmmpfam = new HMMPfam();
		hmmpfam.debugModeOn();
		File model = Helper.deployFile(ResourceManager.class.getResourceAsStream("aa-activating.aroundLys.hmm"));
		hmmpfam.run( evalue, model, new File("srctest/testdata/two-domains.fa"));
		
		
		List<QueryResult> res = hmmpfam.getResults();
		assertEquals(2,res.size());
		
		QueryResult firstQueryResult = res.get(0);
		
		List<DomainHit> domainHits = firstQueryResult.getHits();
		
		assertEquals(2,domainHits.size());
		
		assertEquals(1.555906e-59,domainHits.get(0).evalue,1e-6);
		assertEquals(5.116907e-24,domainHits.get(1).evalue,1e-6);
		
		assertEquals(1.963560e+02,domainHits.get(0).score,1e-6);
		assertEquals(7.837100e+01,domainHits.get(1).score,1e-6);
		
		List<DomainAlignment> alis = firstQueryResult.getAlignments();
		assertEquals(2,alis.size());
		
		assertEquals("KGVmv + ++vn ++w+++   +++++      +d+v+q+++ +sFD+Svwe+f+++++G++LV++++e    ++Dp a++++++++g+T+++++Ps+l ++++     tp+ a   + +L+ v++ GEaL+++l+r ++++ +  a   l+N+YGPTE++V++++",alis.get(0).match);
		assertEquals("KGVmveHrnvvnlvkwlneryflfgeeddllgesdrvLqfssAysFDaSvweifgaLLnGgtLVivpkefsetrlDpeaLaalieregiTvlnltPsllnllldaaeeatpdfapedlssLrrvlvGGEaLspslarrlrerfpdragvrliNaYGPTEtTVcaTi",alis.get(0).target);
		assertEquals("KGVMVGQTAIVNRLLWMQN---HYPLTG-----EDVVAQKTP-CSFDVSVWEFFWPFIAGAKLVMAEPE---AHRDPLAMQQFFAEYGVTTTHFVPSMLAAFVASL---TPQTARQSCVTLKQVFCSGEALPADLCREWQQLTG--A--PLHNLYGPTEAAVDVSW",alis.get(0).query);
		
		assertEquals("d+++L+a+L+++LP +MVP +  + +L++lPL++NGKlDRkALP P+  a",alis.get(1).match);
		assertEquals("daaeLRahLaarLPdYMVPsaVkfvvLdalPLTpNGKlDRkALPaPdaaa",alis.get(1).target);
		assertEquals("DTSALQAQLRETLPPHMVPVV--LLQLPQLPLSANGKLDRKALPLPELKA",alis.get(1).query);
				
		QueryResult secondQueryResult = res.get(1);
		
		domainHits = secondQueryResult.getHits();
		assertEquals(5,domainHits.size());
		assertEquals(1.763813e-32,domainHits.get(0).evalue,1e-6);
		assertEquals(7.095885e-28,domainHits.get(1).evalue,1e-6);
		assertEquals(1.518558e-32,domainHits.get(2).evalue,1e-6);
		assertEquals(2.802617e-31,domainHits.get(3).evalue,1e-6);
		assertEquals(2.555401e-48,domainHits.get(4).evalue,1e-6);
		
		assertEquals(1.064830e+02,domainHits.get(0).score,1e-6);
		assertEquals(9.118700e+01,domainHits.get(1).score,1e-6);
		assertEquals(1.066990e+02,domainHits.get(2).score,1e-6);
		assertEquals(1.024930e+02,domainHits.get(3).score,1e-6);
		assertEquals(1.590990e+02,domainHits.get(4).score,1e-6);
		
		alis = secondQueryResult.getAlignments();
		assertEquals(5,alis.size());
		
		assertEquals("KGVm+eHrn vn+++w  +   +f ++  l    ++ L+ +s ++FD++v+e f++L +Gg+  +v+     ++  +        ++ i  +n +Ps+l++lld+            ++s+++v v+GEaL   l++ l+e  +     rl N+YGP EtT+++++",alis.get(0).match);
		assertEquals("KGVmveHrnvvnlvkwlneryflfgeeddllgesdrvLqfssAysFDaSvweifgaLLnGgtLVivpkefsetrlDpeaLaalieregiTvlnltPsllnllldaaeeatpdfapedlssLrrvlvGGEaLspslarrlrerfpdragvrliNaYGPTEtTVcaTi",alis.get(0).target);
		assertEquals("KGVMIEHRNTVNFLTWAHN---AFEGSV-L----EKTLFSTS-LNFDLAVYECFAPLTSGGSIKVVRNV--LELQHG--------EHDIGLINTVPSALKALLDVN---------GLPDSVHTVNVAGEALKRNLVENLFEKTGV-Q--RLCNLYGPSETTTYSSW",alis.get(0).query);
		
		assertEquals("d++ LR +L+++LP+YM+P a  +v+Lda+PLTpNGKl R ALPaP+ ++",alis.get(1).match);
		assertEquals("daaeLRahLaarLPdYMVPsaVkfvvLdalPLTpNGKlDRkALPaPdaaa",alis.get(1).target);
		assertEquals("DIDNLRGWLQEQLPAYMIPVA--YVRLDAMPLTPNGKLHRQALPAPESDL",alis.get(1).query);

		assertEquals("KGVm+eHrn vn+++w      +f  +  l    ++ L+ +s ++FD++v+e f++L +Gg+  +v      ++  +        ++ i  +n +Ps+l++ll+++           + s+++v v+GEaL  sl++ l+e  +     rl N+YGP EtT+++++",alis.get(2).match);
		assertEquals("KGVmveHrnvvnlvkwlneryflfgeeddllgesdrvLqfssAysFDaSvweifgaLLnGgtLVivpkefsetrlDpeaLaalieregiTvlnltPsllnllldaaeeatpdfapedlssLrrvlvGGEaLspslarrlrerfpdragvrliNaYGPTEtTVcaTi",alis.get(2).target);
		assertEquals("KGVMIEHRNTVNFLTWAHT---AFDASA-L----EKTLFSTS-LNFDLAVYECFAPLTSGGSIKVVKNV--LELQHG--------EHDIGLINTVPSALKALLEVD---------GLPTSVHTVNVAGEALKRSLVESLFENTGV-Q--RLCNLYGPSETTTYSSW",alis.get(2).query);

		assertEquals("d + LR+hL+++LP+YMVP+   +v L+a+PLTpNGKlDRkALPaPd +a",alis.get(3).match);
		assertEquals("daaeLRahLaarLPdYMVPsaVkfvvLdalPLTpNGKlDRkALPaPdaaa",alis.get(3).target);
		assertEquals("DGDSLRSHLQEKLPEYMVPAI--YVLLEAMPLTPNGKLDRKALPAPDVDA",alis.get(3).query);
				
		assertEquals("KGV+v+Hr++++lv + ++  ++f+ +d       rv++ s+  +FDaS+ +++++LLnGg++V++ ++   ++l+p  +++l+ ++ +Tvl+ t  l+++++            e++s+Lr ++vGG+ L+p++ +r+++        +l N+YGPTE+T+++  ",alis.get(4).match);
		assertEquals("KGVmveHrnvvnlvkwlneryflfgeeddllgesdrvLqfssAysFDaSvweifgaLLnGgtLVivpkefsetrlDpeaLaalieregiTvlnltPsllnllldaaeeatpdfapedlssLrrvlvGGEaLspslarrlrerfpdragvrliNaYGPTEtTVcaTi",alis.get(4).target);
		assertEquals("KGVLVPHRAISRLVINNGY--ADFNAQD-------RVAFASN-PAFDASTLDVWAPLLNGGCVVVIGQH---DLLSPLNFQRLLLEQSVTVLWMTAGLFHQYASGL--------GEAFSRLRYLIVGGDVLDPAVIARVLANNAP-Q--HLLNGYGPTEATTFSAT",alis.get(4).query);
	}
}
