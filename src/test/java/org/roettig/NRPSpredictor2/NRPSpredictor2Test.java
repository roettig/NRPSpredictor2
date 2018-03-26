package org.roettig.NRPSpredictor2;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.roettig.NRPSpredictor2.extraction.ADomain;
import org.roettig.NRPSpredictor2.predictors.BacterialNRPSPredictor;
import org.roettig.NRPSpredictor2.predictors.BacterialNRPSPredictor2;
import org.roettig.NRPSpredictor2.predictors.Detection;

public class NRPSpredictor2Test 
{
	public void testNRPSpredictor1Generic(
			String signature8A, 
			String singleClassLabel,
			String threeClassLabel
			)
	{
		List<ADomain> adoms = new ArrayList<ADomain>();
		
		ADomain adom = new ADomain(); 
		adom.sig8a   = signature8A;
		
		adoms.add(adom);
		
		BacterialNRPSPredictor bactPred = new BacterialNRPSPredictor();
		bactPred.predict(adoms);
		
		boolean hasSinglePrediction     = false;
		boolean hasThreeClassPrediction = false;
		
		for(Detection det: adom.getDetections())
		{
			if(det.getLabel().equalsIgnoreCase(singleClassLabel))
			{
				hasSinglePrediction = true;
			}
			if( det.getLabel().equalsIgnoreCase(threeClassLabel))
			{
				hasThreeClassPrediction = true;
			}
		}
		
		assertTrue(hasSinglePrediction);
		if(null!=threeClassLabel)
			assertTrue(hasThreeClassPrediction);
	}
	
	
	public void testNRPSpredictor2Generic(
			String signature8A, 
			String singleClassLabel,
			String threeClassLabel
			)
	{
		List<ADomain> adoms = new ArrayList<ADomain>();
		
		ADomain adom = new ADomain(); 
		adom.sig8a   = signature8A;
		
		adoms.add(adom);
		
		BacterialNRPSPredictor2 bactPred2 = new BacterialNRPSPredictor2();
		bactPred2.predict(adoms);
		
		boolean hasSinglePrediction     = false;
		boolean hasThreeClassPrediction = false;
		
		for(Detection det: adom.getDetections())
		{
			if(det.getLabel().equalsIgnoreCase(singleClassLabel))
			{
				hasSinglePrediction = true;
			}
			if( det.getLabel().equalsIgnoreCase(threeClassLabel))
			{
				hasThreeClassPrediction = true;
			}
		}
		
		assertTrue(hasSinglePrediction);
		if(null!=threeClassLabel)
			assertTrue(hasThreeClassPrediction);
	}
	
	@Test
	public void testPredictorNRPSpredictor1()
	{
		testNRPSpredictor1Generic
		(		
				"ADLAFEPFMRQINGLLVGEINEYAFTETAFVTAI",
				BacterialNRPSPredictor.SMALL_CLUSTER[BacterialNRPSPredictor.SMALL_CLUSTER_AAD],
				BacterialNRPSPredictor.LARGE_CLUSTER[BacterialNRPSPredictor.LARGE_CLUSTER_ASP_ASN_GLU_GLN_AAD]
		);
		
		testNRPSpredictor1Generic
		(		
				"LNNSFDASTLDAWMIVGGDLNGYGPTEATTFSAT",
				BacterialNRPSPredictor.SMALL_CLUSTER[BacterialNRPSPredictor.SMALL_CLUSTER_VAL_LEU_ILE_ABU_IVA],
				BacterialNRPSPredictor.LARGE_CLUSTER[BacterialNRPSPredictor.LARGE_CLUSTER_GLY_ALA_VAL_LEU_ILE_ABU_IVA]
		);
		
		
	}
	
	@Test
	public void testPredictorNRPSpredictor2()
	{
		
		
		testNRPSpredictor2Generic
		(		
				"ADLAFEPFMRQINGLLVGEINEYAFTETAFVTAI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_AAD],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"FWATFDLAVYEANTNVAGECNLYGPSETTTYSSW",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ALA],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"VAWAFDVFSADRDFVCGSDVQAYGVTEASIDSTC",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ARG],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"YWASFDLTVTSTKLIVGGEFNEYGPTETVVGCMI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ASN],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"YGVSADLGHTLGKIMLGGEVNHYGPTETTVGILT",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ASP],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"LAQAFDAAISEMTVVVAGEINGYGPTETTVCVTM",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_BHT],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"TDISFDLSVYDGNSMLSGDIAMGGATEASIWSNA",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_CYS],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"SRSIYAMSSPGGALQVGGAQQVFGMAEGLVNYTR",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_DHB],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"LDAAFDPSLYAVHLGTGGDRNTYGPTETTLCATW",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_DHPG],
				null//BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"HWMTFDASVWEAQLFCGGEHNLYGPTETCIDATF",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_GLN],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"RCMAFDVSVWEWHFFSGGKHNRYGPTETAINVTH",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_GLU],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"FAMTFDIAALEHQGLVGGEVNLYGPTETTIWSTA",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_GLY],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"LDHAFDVSCYEVHLLTGGDRHLYGPTETTLCVTQ",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_HPG],
				null//BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"VETSFDGFTFDGFVLFGGEIHVYGPTETTVFATF",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ILE],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"SAIAFDLSIGEAYLLLAGEINGWGPAETCVFSTL",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_IVA],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"NAISFDFSILEMYLFAGGEINGWGPTETTVVGSI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_LEU],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"YDHWFDAAWQPADTALGGEFNCYGPTETTVEAVV",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_LYS],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"RWQAFDISINELYLWCGGEYHGYGPAETTIGVSH",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ORN],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testNRPSpredictor2Generic
		(
				"VPLAFDAALWELTLVVAGETNAYGPTEAAVCTTI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_PHE],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"LWQVFDYSVQESYACQGGEHNHYGPAESQLITGY",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_PIP],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"LWHAFDVAAQESYAAQAGEHNHYGPAETHVMTGI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_PRO],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"RWMTFDVSVWEWHFFCSGEHNLYGPTEAAVDVTY",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_SER],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"LATHFDFSVWEGNQVFGGEVNMYGITETTVHVSH",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_THR],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"LDRVFDVSMADPVMVSGGDHNEYGVTEATVVSTV",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_TRP],
				null //BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testNRPSpredictor2Generic
		(
				"LAQAFDAAVSEMTVVVAGEINAYGPTETTVCASM",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_TYR],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);

		testNRPSpredictor2Generic
		(
				"LNNSFDASTLDAWMIVGGDLNGYGPTEATTFSAT",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_VAL],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
	}
}
