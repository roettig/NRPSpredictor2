package org.roettig.NRPSpredictor2.predictors;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.roettig.NRPSpredictor2.extraction.ADomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NearestNeighborPredictor implements Predictor
{
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(NearestNeighborPredictor.class);
	
	private static final String DEFAULT_ASCII_CODEPAGE = "CP1252";
	
	private List<String> sigstach        = new ArrayList<String>();
	private List<String> sigstach_fungal = new ArrayList<String>();
	private List<String> specs           = new ArrayList<String>();
	private List<String> specs_fungal    = new ArrayList<String>();
	private List<String> sig8a           = new ArrayList<String>();

	private boolean bacterialPrediction = true;
	
	public NearestNeighborPredictor()
	{
		try
		{
			initSigDB();
		}
		catch (IOException e)
		{
			logger.error("Unexpected Exception", e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public boolean isBacterialPrediction()
	{
		return bacterialPrediction;
	}

	public void setBacterialPrediction(boolean bacterialPrediction)
	{
		this.bacterialPrediction = bacterialPrediction;
	}

	private void initSigDB() 
	throws 
		IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader( new FileInputStream("data/labeled_sigs"), DEFAULT_ASCII_CODEPAGE));
		try
		{
			String line = "";
			while((line=in.readLine())!=null)
			{
				String toks[] = line.split("\\\t");
				specs.add(toks[0]);
				sig8a.add(toks[1]);
				sigstach.add(toks[2]);
			}
		}
		finally
		{
			if(null!=in)
				in.close();
		}
		
		in = new BufferedReader(new InputStreamReader( new FileInputStream("data/fungal_labeled_sigs"), DEFAULT_ASCII_CODEPAGE));
		
		try
		{
			String line = "";
			while((line=in.readLine())!=null)
			{
				String toks[] = line.split("\\\t");
				specs_fungal.add(toks[0]);
				sigstach_fungal.add(toks[2]);
			}
		}
		finally
		{
			if(null!=in)
				in.close();
		}
	}
	
	@Override
	public void predict(List<ADomain> domains)
	{
		detectStachNN(domains, bacterialPrediction);
	}

	public void detectStachNN(List<ADomain> adoms, boolean bacterial)
	{
		for(ADomain ad: adoms)
		{
			String[] hitspec = new String[1];
			double sim = nearestSignature(ad, hitspec, bacterial);
			if(bacterial)
				ad.addDetection(ADomain.NRPS2_STACH_NN, hitspec[0], sim);
			else
				ad.addDetection(ADomain.NRPS2_STACH_NN_FUNGAL, hitspec[0], sim);
		}
	}

	/**
	 * Computes the nearest signature according to sequence identity of Stachelhaus code.
	 * 
	 * @param ad query ADomain
	 * @param hit the best matching ADomain
	 * @param bacterial consider only bacterial signatures
	 * 
	 * @return sequence identity distance
	 */
	public double nearestSignature(ADomain ad, String[] hit, boolean bacterial)
	{
		String a_sig   = ad.sigstach;
		int maxMatches = 0;
		int idx = 0;
		
		List<String> sigs;
		
		if(bacterial)
		{
			// consider only bacterial signatures
			sigs = sigstach;
		}
		else
		{
			// consider only fungal signatures
			sigs = sigstach_fungal;
		}
		
		// compute sequence identity to all candidates
		for(String sig: sigs)
		{
			// current best number of matches (amino acids)
			int matches = 0;
			
			for(int i=0;i<sig.length();i++)
			{
				if(sig.charAt(i)==a_sig.charAt(i))
					matches++;
			}
			if(matches>maxMatches)
			{
				maxMatches = matches;
				hit[0] = specs.get(idx); 
			}
			idx++;
		}
		return (1.0*maxMatches)/(a_sig.length());
	}
}
