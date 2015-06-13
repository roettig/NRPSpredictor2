package org.roettig.NRPSpredictor2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.roettig.NRPSpredictor2.extraction.ADomain;
import org.roettig.NRPSpredictor2.predictors.BacterialNRPSPredictor2;
import org.roettig.NRPSpredictor2.predictors.Detection;

public class NRPSpredictor2Test 
{
	public void testPredictorGeneric(
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
	public void testPredictor()
	{
		
		
		testPredictorGeneric
		(		
				"ADLAFEPFMRQINGLLVGEINEYAFTETAFVTAI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_AAD],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"FWATFDLAVYEANTNVAGECNLYGPSETTTYSSW",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ALA],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"VAWAFDVFSADRDFVCGSDVQAYGVTEASIDSTC",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ARG],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"YWASFDLTVTSTKLIVGGEFNEYGPTETVVGCMI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ASN],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"YGVSADLGHTLGKIMLGGEVNHYGPTETTVGILT",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ASP],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"LAQAFDAAISEMTVVVAGEINGYGPTETTVCVTM",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_BHT],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testPredictorGeneric
		(
				"TDISFDLSVYDGNSMLSGDIAMGGATEASIWSNA",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_CYS],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"SRSIYAMSSPGGALQVGGAQQVFGMAEGLVNYTR",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_DHB],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testPredictorGeneric
		(
				"LDAAFDPSLYAVHLGTGGDRNTYGPTETTLCATW",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_DHPG],
				null//BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"HWMTFDASVWEAQLFCGGEHNLYGPTETCIDATF",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_GLN],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"RCMAFDVSVWEWHFFSGGKHNRYGPTETAINVTH",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_GLU],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"FAMTFDIAALEHQGLVGGEVNLYGPTETTIWSTA",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_GLY],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"LDHAFDVSCYEVHLLTGGDRHLYGPTETTLCVTQ",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_HPG],
				null//BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testPredictorGeneric
		(
				"VETSFDGFTFDGFVLFGGEIHVYGPTETTVFATF",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ILE],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"SAIAFDLSIGEAYLLLAGEINGWGPAETCVFSTL",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_IVA],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"NAISFDFSILEMYLFAGGEINGWGPTETTVVGSI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_LEU],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"YDHWFDAAWQPADTALGGEFNCYGPTETTVEAVV",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_LYS],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"RWQAFDISINELYLWCGGEYHGYGPAETTIGVSH",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_ORN],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHILIC]
		);
		
		testPredictorGeneric
		(
				"VPLAFDAALWELTLVVAGETNAYGPTEAAVCTTI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_PHE],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testPredictorGeneric
		(
				"LWQVFDYSVQESYACQGGEHNHYGPAESQLITGY",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_PIP],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"LWHAFDVAAQESYAAQAGEHNHYGPAETHVMTGI",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_PRO],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"RWMTFDVSVWEWHFFCSGEHNLYGPTEAAVDVTY",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_SER],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"LATHFDFSVWEGNQVFGGEVNMYGITETTVHVSH",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_THR],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
		testPredictorGeneric
		(
				"LDRVFDVSMADPVMVSGGDHNEYGVTEATVVSTV",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_TRP],
				null //BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);
		
		testPredictorGeneric
		(
				"LAQAFDAAVSEMTVVVAGEINAYGPTETTVCASM",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_TYR],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_AROMATIC]
		);

		testPredictorGeneric
		(
				"LNNSFDASTLDAWMIVGGDLNGYGPTEATTFSAT",
				BacterialNRPSPredictor2.SINGLE_CLASS[BacterialNRPSPredictor2.SINGLE_CLASS_VAL],
				BacterialNRPSPredictor2.THREE_CLASS[BacterialNRPSPredictor2.THREE_CLASS_HYDROPHOBIC_ALIPHATIC]
		);
		
	}
}
