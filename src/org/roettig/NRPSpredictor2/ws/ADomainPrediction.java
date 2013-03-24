package org.roettig.NRPSpredictor2.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ADomainPrediction
{
	private String signature;
	private String label;
	private float  score;
	
	public ADomainPrediction() {}
	
	public ADomainPrediction(String signature, String label, float score)
	{
		this.signature = signature;
		this.label = label;
		this.score = score;
	}
	
	@XmlElement
	public String getSignature()
	{
		return signature;
	}
	
	public void setSignature(String signature)
	{
		this.signature = signature;
	}
	
	@XmlElement
	public float getScore()
	{
		return score;
	}
	
	public void setScore(float score)
	{
		this.score = score;
	}
	
	@XmlElement
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String toString()
	{
		return String.format("[ADomPred] sig: %s label: %s score: %.2f", signature,label, score);
	}
}
