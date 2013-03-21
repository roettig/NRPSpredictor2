package org.roettig.NRPSpredictor2.predictors;

import org.roettig.NRPSpredictor2.ADomain;
import org.roettig.NRPSpredictor2.encoder.PrimalWoldEncoder;

public class BacterialNRPSPredictor2 extends PredictorBase
{
	private static String three_class[]    = {"hydrophilic","hydrophobic-aliphatic","hydrophobic-aromatic"};
	private static String single_cluster[] = {"aad","ala","arg","asn","asp","bht","cys","dhb","dhpg","gln","glu","gly","hpg","ile","iva","leu","lys","orn","phe","pip","pro","ser","thr","trp","tyr","val"}; 
		
	public BacterialNRPSPredictor2()
	{
		encoder = new PrimalWoldEncoder();
		
		String type = ADomain.NRPS2_THREE_CLUSTER;
		
		for (String label : three_class)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
		
		type = ADomain.NRPS2_SINGLE_CLUSTER;
		
		for (String label : single_cluster)
		{
			NRPSPredictorModel model = NRPSPredictorModel.loadModel(label, type);
			model.setLabel(label);
			model.setType(type);
			models.add(model);
		}
	}
}
