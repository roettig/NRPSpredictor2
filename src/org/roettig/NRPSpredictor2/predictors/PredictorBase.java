package org.roettig.NRPSpredictor2.predictors;

import java.util.ArrayList;
import java.util.List;

import org.roettig.NRPSpredictor2.ADomain;
import org.roettig.NRPSpredictor2.encoder.PrimalEncoder;
import org.roettig.NRPSpredictor2.svm.FeatureVector;
import org.roettig.NRPSpredictor2.svm.SVMlightModel;

public abstract class PredictorBase implements Predictor
{
	
	protected PrimalEncoder encoder;
	protected List<NRPSPredictorModel> models = new ArrayList<NRPSPredictorModel>();
	
	public void predict(List<ADomain> domains)
	{
		for(NRPSPredictorModel model : models)
			predictForModel(domains, model.getType(), model.getLabel(), model);
	}
	
	protected void predictForModel(List<ADomain> domains, String type, String label, SVMlightModel model)
	{
		for(ADomain adomain: domains)
		{
			double fv[] = encoder.encode(adomain.sig8a);
			double yp   = model.predict(FeatureVector.makeFVec(fv));
			if(yp>0.0)
			{
				adomain.addDetection(type, label, yp);
			}
		}		
	}
	
	public PrimalEncoder getEncoder()
	{
		return encoder;
	}

	public void setEncoder(PrimalEncoder encoder)
	{
		this.encoder = encoder;
	}
}
