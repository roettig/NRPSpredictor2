package org.roettig.NRPSpredictor2.hmmer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

public class HMMPfamParser
{
	private List<QueryResult> results = new ArrayList<QueryResult>();
	
	public HMMPfamParser(String filename)
	{
		try
		{
			InputStream xmlstream = new FileInputStream(filename);
			parse(xmlstream);
		}
		catch(FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException(e);
		}
		catch (SAXException e)
		{
			throw new RuntimeException(e);
		}
		catch (DocumentException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public List<QueryResult> getResultsForQueries()
	{
		return results;
	}
	
	@SuppressWarnings("unchecked")
	private void parse(InputStream xmlstream) throws ParserConfigurationException, SAXException, DocumentException
	{	
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser 		 parser  = factory.newSAXParser();

		SAXReader reader = new SAXReader(parser.getXMLReader());
		reader.setValidation(false);
		
		Document doc = reader.read(xmlstream);
		
		List<Node>   nodes  = doc.selectNodes("/hmmpfamresults/querysequence");
		
		for(Node node: nodes)
		{
			String query_id = node.valueOf("@id");
			
			QueryResult res = new QueryResult(query_id);

			// fetch domain hits
			List<Node> hitnodes = node.selectNodes("domainhits/domainhit");
			for(Node hitnode: hitnodes)
			{
				DomainHit hit = new DomainHit();
				hit.hmmname = hitnode.valueOf("@name");
				hit.domidx  = Integer.parseInt(hitnode.valueOf("@domidx"));
				hit.ndom    = Integer.parseInt(hitnode.valueOf("@ndom"));
				hit.score   = Double.parseDouble(hitnode.valueOf("@score"));
				hit.evalue  = Double.parseDouble(hitnode.valueOf("@evalue"));
				hit.hmmfrom = Integer.parseInt(hitnode.valueOf("@hmmfrom"));
				hit.hmmto   = Integer.parseInt(hitnode.valueOf("@hmmto"));
				hit.seqfrom = Integer.parseInt(hitnode.valueOf("@sqfrom"));
				hit.seqto   = Integer.parseInt(hitnode.valueOf("@sqto"));
				
				res.addHit(hit);
			}
			
			
			// fetch domain alignment
			List<Node> alinodes = node.selectNodes("alignments/alignment");
			
			for(Node alinode: alinodes)
			{
				DomainAlignment ali = new DomainAlignment();
				
				ali.score   =  Double.parseDouble(alinode.valueOf("@score"));
				ali.evalue  =  Double.parseDouble(alinode.valueOf("@evalue"));
				ali.hmmname =  alinode.valueOf("@name");
				ali.target  =  alinode.selectSingleNode("modelline").getText();
				ali.match   =  alinode.selectSingleNode("matchline").getText();
				ali.query   =  alinode.selectSingleNode("queryline").getText();
				res.addAlignment(ali);
			}
			
			results.add(res);
		}
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		HMMPfamParser parser = new HMMPfamParser("/tmp/in.fa.log");
		List<QueryResult> res = parser.getResultsForQueries();
		for(QueryResult q: res)
		{
			System.out.println(q.getQueryId());
			for(DomainAlignment ali: q.getAlignments())
			{
				System.out.println("\t"+ali.hmmname+" "+ali.evalue);
			}
		}
	}

	public static class DomainHit
	{
		public double score;
		public double evalue;
		public String hmmname;
		public int    domidx;
		public int    ndom;
		public int    seqfrom;
		public int    seqto;
		public int    hmmfrom;
		public int    hmmto;
		
		public String toString()
		{
			return String.format("[domain hit: %s %d/%d] evalue=%e score=%e seq:[%d:%d] hmm:[%d:%d]", hmmname, domidx,ndom,evalue, score, seqfrom,seqto,hmmfrom,hmmto);
		}
	}
	
	public static class DomainAlignment
	{
		public String query;
		public String target;
		public String match;
		public double score;
		public double evalue;
		public String hmmname;
		
		public String toString()
		{
			return String.format("%s\n%s\n%s\n",target,match,query);
		}
	}
	
	public static class QueryResult
	{
		private List<DomainAlignment> alis = new ArrayList<DomainAlignment>();
		private List<DomainHit>       hits = new ArrayList<DomainHit>();
		private String query_id;
		
		public QueryResult(String query_id)
		{
			this.query_id = query_id;
		}
		
		public void addAlignment(DomainAlignment ali)
		{
			alis.add(ali);
		}
		
		public void addHit(DomainHit hit)
		{
			hits.add(hit);
		}
		
		public List<DomainAlignment> getAlignments()
		{
			return this.alis;
		}
		
		public List<DomainHit> getHits()
		{
			return this.hits;
		}
		
		public String getQueryId()
		{
			return this.query_id;
		}
	}
}
