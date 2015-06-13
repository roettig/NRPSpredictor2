package org.roettig.NRPSpredictor2.predictors;

import org.roettig.NRPSpredictor2.encoder.PrimalWoldEncoder;
import org.roettig.NRPSpredictor2.extraction.ADomain;

public class BacterialNRPSPredictor2 
extends 
	PredictorBase
{
	public static String THREE_CLASS[]  = {"hydrophilic","hydrophobic-aliphatic","hydrophobic-aromatic"};
	
	public static String SINGLE_CLASS[] = {"aad","ala","arg","asn","asp","bht","cys","dhb","dhpg","gln","glu","gly","hpg","ile","iva","leu","lys","orn","phe","pip","pro","ser","thr","trp","tyr","val"}; 
	
	public static int THREE_CLASS_HYDROPHILIC             = 0;
	
	public static int THREE_CLASS_HYDROPHOBIC_ALIPHATIC  = 1;
	
	public static int THREE_CLASS_HYDROPHOBIC_AROMATIC    = 2;
	
	/**
	 * alpha-Aminoadipic acid (Aad).
	 */
	public static int SINGLE_CLASS_AAD  = 0;
	
	public static int SINGLE_CLASS_ALA  = 1;
	
	public static int SINGLE_CLASS_ARG  = 2;
	
	public static int SINGLE_CLASS_ASN  = 3;
	
	public static int SINGLE_CLASS_ASP  = 4;
	
	/**
	 * beta-hydroxytyrosine.
	 */
	public static int SINGLE_CLASS_BHT  = 5;
	
	public static int SINGLE_CLASS_CYS  = 6;
	
	/**
	 * 2,3-dihydroxy-benzoic acid.
	 */
	public static int SINGLE_CLASS_DHB  = 7;
	
	/**
	 * 3,5-dihydroxy-phenyl-glycin.
	 */
	public static int SINGLE_CLASS_DHPG = 8;
	
	public static int SINGLE_CLASS_GLN  = 9;
	
	public static int SINGLE_CLASS_GLU  = 10;
	
	public static int SINGLE_CLASS_GLY  = 11;
	
	/**
	 * hydoxy-phenyl-glycine.
	 */
	public static int SINGLE_CLASS_HPG  = 12;
	
	public static int SINGLE_CLASS_ILE  = 13;
	
	/**
	 * Isovaline.
	 */
	public static int SINGLE_CLASS_IVA  = 14;
	
	public static int SINGLE_CLASS_LEU  = 15;
	
	public static int SINGLE_CLASS_LYS  = 16;
	
	public static int SINGLE_CLASS_ORN  = 17;
	
	public static int SINGLE_CLASS_PHE  = 18;
	
	public static int SINGLE_CLASS_PIP  = 19;
	
	public static int SINGLE_CLASS_PRO  = 20;
	
	public static int SINGLE_CLASS_SER  = 21;
	
	public static int SINGLE_CLASS_THR  = 22;
	
	public static int SINGLE_CLASS_TRP  = 23;
	
	public static int SINGLE_CLASS_TYR  = 24;
	
	public static int SINGLE_CLASS_VAL  = 25;
	
	public BacterialNRPSPredictor2()
	{
		encoder = new PrimalWoldEncoder();
		
		String type = ADomain.NRPS2_THREE_CLUSTER;
		
		for (String label : THREE_CLASS)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
		
		type = ADomain.NRPS2_SINGLE_CLUSTER;
		
		for (String label : SINGLE_CLASS)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
	}
}
