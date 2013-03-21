package org.roettig.NRPSpredictor2.predictors;

import org.roettig.NRPSpredictor2.ADomain;
import org.roettig.NRPSpredictor2.encoder.PrimalRauschEncoder;

public class BacterialNRPSPredictor extends PredictorBase
{
	// large cluster predictions
	private static String large_cluster[] = { "phe,trp,phg,tyr,bht", "ser,thr,dhpg,hpg",
			"gly,ala,val,leu,ile,abu,iva", "asp,asn,glu,gln,aad", "cys",
			"orn,lys,arg", "pro,pip", "dhb,sal" };

	// small cluster predictions
	private static String small_cluster[] = { "aad", "val,leu,ile,abu,iva", "arg", "asp,asn",
			"cys", "dhb,sal", "glu,gln", "orn,horn", "tyr,bht", "pro", "ser",
			"dhpg,hpg", "phe,trp", "gly,ala", "thr" };

	public BacterialNRPSPredictor()
	{
		encoder = new PrimalRauschEncoder();
		
		String type = ADomain.NRPS1_LARGE_CLUSTER;
		for (String label : large_cluster)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
		type = ADomain.NRPS1_SMALL_CLUSTER;
		for (String label : small_cluster)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
	}
}
