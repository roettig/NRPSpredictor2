package org.roettig.NRPSpredictor2.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://ws.NRPSpredictor2.roettig.org/")
public class Sequence
{

	@XmlEnum
	public enum Kingdom
	{
		@XmlEnumValue(value="bacterial") Bacterial,
		@XmlEnumValue(value="fungal") Fungal
	}
	

	@XmlEnum
	public enum SequenceType
	{
		@XmlEnumValue(value="FullSequence") FullSequence,
		@XmlEnumValue(value="Signature8A") Signature8A,
		@XmlEnumValue(value="SignatureStachelhaus") SignatureStachelhaus
	}
	
	private String       sequence;
	private SequenceType type;
	private String  id;
	private Kingdom kingdom;
	
	public Sequence() {}
	
	public Sequence(String id, String seq, Kingdom kingdom, SequenceType type )
	{
		this.id = id;
		this.sequence = seq;
		this.kingdom = kingdom;
		this.type = type;
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
		return type;
	}
	
	public void setSequenceType(SequenceType sequenceType)
	{
		this.type = sequenceType;
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
