package org.roettig.NRPSpredictor2.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ADomainPrediction
{
	private boolean outlier;
	private String id;
	private String signature;
	private String stachelhausCode;
	private String label;
	private float  score;
	
	public ADomainPrediction() {}
	
	public ADomainPrediction(String id, String signature, String label, float score, boolean isOutlier)
	{
		this.id = id;
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
	
	@XmlElement
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
	
	@XmlElement
	public boolean isOutlier()
	{
		return outlier;
	}

	public void setOutlier(boolean outlier)
	{
		this.outlier = outlier;
	}

	@XmlElement
	public String getStachelhausCode()
	{
		return stachelhausCode;
	}

	public void setStachelhausCode(String stachelhausCode)
	{
		this.stachelhausCode = stachelhausCode;
	}

	public String toString()
	{
		return String.format("[ADomPred] sig: %s label: %s score: %.2f", signature,label, score);
	}
}
