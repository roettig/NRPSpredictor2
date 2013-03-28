package org.roettig.NRPSpredictor2.predictors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.roettig.NRPSpredictor2.extraction.ADomain;

public class NearestNeighborPredictor implements Predictor
{
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
			e.printStackTrace();
			throw new RuntimeException(e);
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

	private void initSigDB() throws IOException
	{
		BufferedReader in = new BufferedReader(new FileReader("data/labeled_sigs"));
		String line = "";
		while((line=in.readLine())!=null)
		{
			String toks[] = line.split("\\\t");
			specs.add(toks[0]);
			sig8a.add(toks[1]);
			sigstach.add(toks[2]);
		}
		in.close();
		
		in = new BufferedReader(new FileReader("data/fungal_labeled_sigs"));
		line = "";
		while((line=in.readLine())!=null)
		{
			String toks[] = line.split("\\\t");
			specs_fungal.add(toks[0]);
			sigstach_fungal.add(toks[2]);
		}
		in.close();
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
	
	public double nearestSignature(ADomain ad, String[] hit, boolean bacterial)
	{
		String a_sig   = ad.sigstach;
		int maxMatches = 0;
		int idx = 0;
		
		List<String> sigs;
		if(bacterial)
			sigs = sigstach;
		else
			sigs = sigstach_fungal;
		
		for(String sig: sigs)
		{
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
