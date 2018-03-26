package org.roettig.NRPSpredictor2.ws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NRPSPredictorRequest
{
	private List<Sequence>  sequences = new ArrayList<Sequence>();
	
	public NRPSPredictorRequest() {}
	
	public NRPSPredictorRequest(List<Sequence> sequences)
	{
		this.sequences = sequences;
	}
	
	@XmlElement(name="sequence",namespace="http://ws.NRPSpredictor2.roettig.org/")
	public List<Sequence> getSequences()
	{
		return sequences;
	}

	public void setSequences(List<Sequence> sequences)
	{
		this.sequences = sequences;
	}

	public void addSequence(Sequence seq)
	{
		this.sequences.add(seq);
	}
}
