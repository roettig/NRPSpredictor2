package org.roettig.NRPSpredictor2.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://ws.NRPSpredictor2.roettig.org/")
public class Sequence
{
	public enum Kingdom
	{
		Bacterial,
		Fungal
	}
	
	public enum SequenceType
	{
		FullSequence,
		Signature8A,
		SignatureStachelhaus
	}
	
	private String       sequence;
	private SequenceType isSignature;
	private String  id;
	private Kingdom kingdom;
	
	public Sequence() {}
	
	public Sequence(String id, String seq, Kingdom kingdom, SequenceType type )
	{
		this.id = id;
		this.sequence = seq;
		this.kingdom = kingdom;
		this.isSignature = type;
	}
	
	@XmlElement(name="seqString",required=true,namespace="http://ws.NRPSpredictor2.roettig.org/")
	public String getSequence()
	{
		return sequence;
	}
	
	public void setSequence(String sequence)
	{
		this.sequence = sequence;
	}
	
	@XmlElement(required=true,namespace="http://ws.NRPSpredictor2.roettig.org/")
	public SequenceType getSequenceType()
	{
		return isSignature;
	}
	
	public void setSequenceType(SequenceType sequenceType)
	{
		this.isSignature = sequenceType;
	}
	
	@XmlElement(required=true,namespace="http://ws.NRPSpredictor2.roettig.org/")
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	@XmlElement(required=true,namespace="http://ws.NRPSpredictor2.roettig.org/")
	public Kingdom getKingdom()
	{
		return kingdom;
	}
	
	public void setKingdom(Kingdom kingdom)
	{
		this.kingdom = kingdom;
	}
}
