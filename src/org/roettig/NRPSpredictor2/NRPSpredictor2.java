package org.roettig.NRPSpredictor2;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jnisvmlight.FeatureVector;
import jnisvmlight.SVMLightModel;

public class NRPSpredictor2
{	
	private static List<ADomain> adoms = new ArrayList<ADomain>();
	
	public static void parseSigs(String filename) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		ADomain cur_adom = null;
		while ((line = br.readLine()) != null)   
		{
			line = line.trim();
			String toks[] = line.split("\\\t");
			cur_adom = new ADomain();
			cur_adom.sig8a = toks[0];
			if(toks.length>1)
				cur_adom.sid = toks[1];
			adoms.add(cur_adom);
		}
	}
	
	public static void parseNRPS1(String filename) throws Exception
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
			if(line.contains("large clusters"))
			{
				while((line = br.readLine())!=null)
				{
					if(line.contains("small clusters"))
						break;
					if(line.contains("[no predictions"))
					{
						cur_adom.largecluster_predictions.add("nop");
						cur_adom.largecluster_scores.add(0.0);
						continue;
					}
					String toks[] = line.split(":");
					cur_adom.largecluster_predictions.add(toks[0]);
					cur_adom.largecluster_scores.add(Double.parseDouble(toks[1]));
				}
				while((line = br.readLine())!=null)
				{
					if(line.contains("Alis (>="))
						break;
					if(line.contains("[no predictions"))
					{
						cur_adom.smallcluster_predictions.add("no prediction possible");
						cur_adom.smallcluster_scores.add(0.0);
						continue;
					}
					String toks[] = line.split(":");
					cur_adom.smallcluster_predictions.add(toks[0]);
					cur_adom.smallcluster_scores.add(Double.parseDouble(toks[1]));
				}
				continue;
			}
			
		}
		adoms.add(cur_adom);
		br.close();
		for(ADomain ad: adoms)
		{
			//System.out.println(ad.sid+"|"+ad.sigstach+"|"+ad.sig8a+"|"+ad.largecluster_predictions.get(0)+"|"+ad.smallcluster_predictions.get(0));
			System.out.println(ad);			
		}
		
		store(outputdir+"/preds",adoms);
		
	}
	
	private static String outputdir = "/tmp";
	
	public static void store(String filename, List<ADomain> data) throws IOException
	{
		FileOutputStream f_out = new FileOutputStream(filename);
		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
		// Write object out to disk
		obj_out.writeObject( data );
		f_out.close();
	}
	
	public static List<ADomain> load(String filename) throws Exception
	{
		// restore object from file ...
		// Read from disk using FileInputStream
		FileInputStream f_in = new FileInputStream(filename);

		// Read object using ObjectInputStream
		ObjectInputStream obj_in = new ObjectInputStream (f_in);
		List<ADomain> ret = (List<ADomain>) obj_in.readObject();
		f_in.close();
		return ret;
	}
	
	
	private static boolean useNRPS1input = false;
	private static String inputfile = "";
	
	private static String modeldir = "/tmp/";
	
	public static void main(String[] argv) throws Exception
	{
		
		parseCommandline(argv);
		if(useNRPS1input)
		{
			parseNRPS1(inputfile);
		}
		else
		{
			parseSigs(inputfile);
		}
		// we now have a list of adomain objects
		
		
		
		// 3 class predictions
		String three_class[] = {"hydrophilic","hydrophobic-aliphatic","hydrophobic-aromatic"};
		for(String sc: three_class)
		{
			System.out.println("#"+sc);
			detect("3class", sc);
		}
		
		for(ADomain ad: adoms)
		{
			System.out.println(ad.sid);
			for(ADomain.Detection det : ad.getDetections() )
			{
				System.out.println(det);
			}
		}
		
		// large cluster predictions
		String large_cluster[] = {"polar, uncharged, thiol","cyclic aliphatic chain with polar NH2","long positively charged chain","aliphatic with ending H-donor","aromatic","apolar, aliphatic","hydroxy benzoic acid derivatives ","aliphatic or phenyl-group with OH"};
	}
	
	public static void detect(String type, String label) throws MalformedURLException, ParseException
	{
		SVMLightModel m = SVMLightModel.readSVMLightModelFromURL(new URL(String.format("file:///%s/%s/[%s].mdl",modeldir,type,label)));
		PrimalWoldEncoder enc = new PrimalWoldEncoder();
		for(ADomain ad: adoms)
		{
			double fv[] = enc.encode(ad.sig8a);
			double yp = m.classify(makeFVec(fv));
			if(yp>0.0)
			{
				ad.addDetection(type, label, yp);
			}
		}
	}
	
	public static FeatureVector makeFVec(double fts[])
	{
		int D = fts.length;
		int[]    dims = new int[D];
		double[] vals = new double[D];
		for(int j=0;j<D;j++)
		{
			vals[j] = fts[j];
			dims[j] = j+1;
		}

		FeatureVector ret = null;
		ret = new FeatureVector(dims, vals);
		return ret;
	}
	
	public static void parseCommandline(String[] argv)
	{
	
		Getopt g = new Getopt("NRPSpredictor2", argv, "ni:o:");
		//
		int c;
		String arg;
		while ((c = g.getopt()) != -1)
		{
			switch(c)
			{
			case 'n':
				useNRPS1input = true; 
				break;
				//
			case 'i':
				arg = g.getOptarg();
				inputfile = arg;
				break;
				//
			case 'o':
				arg = g.getOptarg();
				outputdir = arg;
				break;
				//
			case '?':
				break; // getopt() already printed an error
				//
			default:
				System.out.print("getopt() returned " + c + "\n");
			}
		}		
	}
	

}
