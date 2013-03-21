package org.roettig.NRPSpredictor2.predictors;

import org.roettig.NRPSpredictor2.ADomain;
import org.roettig.NRPSpredictor2.encoder.PrimalWoldEncoder;


public class FungalNRPSPredictor2 extends PredictorBase
{
	private static String three_class[]    = {"hydrophilic","hydrophobic-aliphatic","hydrophobic-aromatic"};
	
	public FungalNRPSPredictor2()
	{
		encoder = new PrimalWoldEncoder();
		
		String type = ADomain.NRPS2_THREE_CLUSTER_FUNGAL;
		
		for (String label : three_class)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
	}
}
