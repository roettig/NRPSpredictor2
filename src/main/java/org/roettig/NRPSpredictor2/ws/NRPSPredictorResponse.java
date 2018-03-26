package org.roettig.NRPSpredictor2.ws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NRPSPredictorResponse
{
	private List<ADomainPrediction> predictions = new ArrayList<ADomainPrediction>();
	
	public NRPSPredictorResponse() {}
	
	@XmlElement
	public List<ADomainPrediction> getPredictions()
	{
		return predictions;
	}

	public void setPredictions(List<ADomainPrediction> predictions)
	{
		this.predictions = predictions;
	}
	
	public void addPrediction(ADomainPrediction pred)
	{
		predictions.add(pred);
	}
}