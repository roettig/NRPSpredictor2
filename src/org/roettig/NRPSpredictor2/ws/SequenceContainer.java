package org.roettig.NRPSpredictor2.ws;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SequenceContainer
{
	private List<Sequence> sequences;
	
	public SequenceContainer() {}
	
	public SequenceContainer(List<Sequence> sequences)
	{
		this.sequences = sequences;
	}

	public List<Sequence> getSequences()
	{
		return sequences;
	}

	public void setSequences(List<Sequence> sequences)
	{
		this.sequences = sequences;
	}
	
}
