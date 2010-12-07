package org.roettig.NRPSpredictor2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NRPSpredictor2
{	
	private static List<ADomain> adoms = new ArrayList<ADomain>();
	
	public static void parse(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		ADomain cur_adom = null;
		while ((line = br.readLine()) != null)   
		{
			if(line.startsWith("Modul: "))
			{
				if(cur_adom!=null)
					adoms.add(cur_adom);
				cur_adom = new ADomain();
				String toks[] = line.split(":");
				String sid = toks[1].trim();
				cur_adom.sid = sid;
				continue;
			}
			if(line.contains("10 amino acid code"))
			{
				String toks[] = line.split(":");
				String sig = toks[1].trim();
				cur_adom.sigstach = sig;
				continue;
			}
			if(line.contains("8A-Code"))
			{
				String toks[] = line.split(":");
				String sig = toks[1].trim();
				cur_adom.sig8a = sig;
				continue;
			}
		}
		adoms.add(cur_adom);
		br.close();
		for(ADomain ad: adoms)
		{
			System.out.println(ad.sid+"|"+ad.sigstach+"|"+ad.sig8a+"|lc|sc");
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		parse(args[0]);
		//parse("/tmp/nrps/jobs/64445cbd9357717a17f497a083b22698/log");
		/*
		List<ADomain> adoms = new ArrayList<ADomain>(); 
		ADomain cur_adom = new ADomain();
		cur_adom.sig8a="hallo";
		adoms.add(cur_adom);
		cur_adom = new ADomain();
		cur_adom.sig8a="hallo2";
		//System.out.println(cur_adom.hashCode());
		//System.out.println(adoms.get(0).hashCode());
		System.out.println(cur_adom.sig8a);
		System.out.println(adoms.get(0).sig8a);
		*/
	}

}
