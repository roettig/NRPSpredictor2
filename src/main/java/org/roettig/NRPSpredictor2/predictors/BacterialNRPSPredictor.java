package org.roettig.NRPSpredictor2.predictors;

import org.roettig.NRPSpredictor2.encoder.PrimalRauschEncoder;
import org.roettig.NRPSpredictor2.extraction.ADomain;

public class BacterialNRPSPredictor 
extends 
	PredictorBase
{
	/**
	 * Large cluster prediction labels.
	 */
	public static String LARGE_CLUSTER[] = { "phe,trp,phg,tyr,bht", "ser,thr,dhpg,hpg",
			"gly,ala,val,leu,ile,abu,iva", "asp,asn,glu,gln,aad", "cys",
			"orn,lys,arg", "pro,pip", "dhb,sal" };

	public static int LARGE_CLUSTER_PHE_TRP_PHG_TYR_BHT         = 0;
	public static int LARGE_CLUSTER_SER_THR_DHPG_HPG            = 1;
	public static int LARGE_CLUSTER_GLY_ALA_VAL_LEU_ILE_ABU_IVA = 2;
	public static int LARGE_CLUSTER_ASP_ASN_GLU_GLN_AAD         = 3;
	public static int LARGE_CLUSTER_CYS                         = 4;
	public static int LARGE_CLUSTER_ORN_LYS_ARG                 = 5;
	public static int LARGE_CLUSTER_PRO_PIP                     = 6;
	public static int LARGE_CLUSTER_DHB_SAL                     = 7;
	
	
	/**
	 *  Small cluster prediction labels.
	 */
	public static String SMALL_CLUSTER[] = { "aad", "val,leu,ile,abu,iva", "arg", "asp,asn",
			"cys", "dhb,sal", "glu,gln", "orn,horn", "tyr,bht", "pro", "ser",
			"dhpg,hpg", "phe,trp", "gly,ala", "thr" };

	public static int SMALL_CLUSTER_AAD                 = 0;
	public static int SMALL_CLUSTER_VAL_LEU_ILE_ABU_IVA = 1;
	public static int SMALL_CLUSTER_ARG                 = 2;
	public static int SMALL_CLUSTER_ASP_ASN             = 3;
	public static int SMALL_CLUSTER_CYS                 = 4;
	public static int SMALL_CLUSTER_DHB_SAL             = 5;
	public static int SMALL_CLUSTER_GLU_GLN             = 6;
	public static int SMALL_CLUSTER_ORN                 = 7;
	public static int SMALL_CLUSTER_TYR_BHT             = 8;
	public static int SMALL_CLUSTER_PRO                 = 9;
	public static int SMALL_CLUSTER_SER                 = 10;
	public static int SMALL_CLUSTER_DHPG_HPG            = 11;
	public static int SMALL_CLUSTER_PHE_TRP             = 12;
	public static int SMALL_CLUSTER_GLY_ALA             = 13;
	public static int SMALL_CLUSTER_THR                 = 14;
	
	
	public BacterialNRPSPredictor()
	{
		encoder = new PrimalRauschEncoder();
		
		String type = ADomain.NRPS1_LARGE_CLUSTER;
		for (String label : LARGE_CLUSTER)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
		type = ADomain.NRPS1_SMALL_CLUSTER;
		for (String label : SMALL_CLUSTER)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
	}
}
