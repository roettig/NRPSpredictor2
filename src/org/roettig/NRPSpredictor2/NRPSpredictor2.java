package org.roettig.NRPSpredictor2;

import gnu.getopt.Getopt;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
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
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

public class NRPSpredictor2
{	
	private static boolean useNRPS1input  = false;
	private static boolean bacterialMode  = true;
	private static String inputfile  = "";
	private static String outputfile = "";
	private static String modeldir = "/home/roettig/coops/nrps2/models";
	
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
						continue;
					}
					String toks[] = line.split(":");
					cur_adom.addDetection("lc", toks[0], Double.parseDouble(toks[1]));
				}
				while((line = br.readLine())!=null)
				{
					if(line.contains("Alis (>="))
						break;
					if(line.contains("[no predictions"))
					{
						continue;
					}
					String toks[] = line.split(":");
					cur_adom.addDetection("sc", toks[0], Double.parseDouble(toks[1]));
				}
				continue;
			}
			
		}
		adoms.add(cur_adom);
		br.close();
		
		//store(outputdir+"/preds",adoms);
		
	}
	
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
	
	public static void main(String[] argv) throws Exception
	{
		modeldir = System.getProperty("modeldir",modeldir);
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
		if(bacterialMode)
			bacterialPrediction();
		else
			fungalPrediction();
		
		// export annotated adomains
		XMLEncoder encoder =
	           new XMLEncoder(
	              new BufferedOutputStream(
	                new FileOutputStream(outputfile)));
	    encoder.writeObject(adoms);
	    encoder.close();
	}
	
	public static void fungalPrediction() throws MalformedURLException, ParseException
	{	
		
	}
	
	public static void bacterialPrediction() throws ParseException, IOException
	{	
		
		// check applicability domain
		checkAD("bacterial");
		
		// 3 class predictions
		String three_class[] = {"hydrophilic","hydrophobic-aliphatic","hydrophobic-aromatic"};
		for(String sc: three_class)
		{
			detect("3class", sc);
		}
		
		// large cluster predictions
		String large_cluster[] = {"phe,trp,phg,tyr,bht","ser,thr,dhpg,hpg","gly,ala,val,leu,ile,abu,iva","asp,asn,glu,gln,aad","cys","orn,lys,arg","pro,pip","dhb,sal"}; 
		for(String sc: large_cluster)
		{
			detect("lc", sc);
		}
		
		// small cluster predictions
		String small_cluster[] = {"aad","val,leu,ile,abu,iva","arg","asp,asn","cys","dhb,sal","glu,gln","orn,horn","tyr,bht","pro","ser","dhpg,hpg","phe,trp","gly,ala","thr"}; 
		for(String sc: small_cluster)
		{			
			detect("sc", sc);
		}
		
		// single aa predictions
		String single_cluster[] = {"aad","ala","arg","asn","asp","bht","cys","dhb","dhpg","gln","glu","gly","hpg","ile","iva","leu","lys","orn","phe","pip","pro","ser","thr","trp","tyr","val"}; 
		for(String sc: single_cluster)
		{
			detect("single", sc);
		}
				
		/*
		
	     */
	}
	
	public static void checkAD(String model) throws IOException
	{
		svm_model m = svm.svm_load_model(modeldir+String.format("/%s_1class.mdl",model));
		
		PrimalWoldEncoder enc = new PrimalWoldEncoder();
		
		for(ADomain ad: adoms)
		{
			double fv[] = enc.encode(ad.sig8a);
			svm_node x[] = makeSVMnode(fv,1);
			double yp = svm.svm_predict(m,x);
			if(yp>=0.0)
				ad.setOutlier(false);
			else
				ad.setOutlier(true);
		}
		
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
	

	public static svm_node[] makeSVMnode(double[] row, int idx)
	{
		int m        = row.length;
		svm_node[] x = new svm_node[m+1];

		x[0]         = new svm_node();
		x[0].index   = 0; 
		x[0].value   = idx;

		for(int j=1;j<=m;j++)
		{
			x[j]       = new svm_node();
			x[j].index = j; 
			x[j].value = row[j-1];
		}
		return x;
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
	
		Getopt g = new Getopt("NRPSpredictor2", argv, "ni:o:b");
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
			case 'b':
				bacterialMode = true; 
				break;
				//				
			case 'i':
				arg = g.getOptarg();
				inputfile = arg;
				break;
				//
			case 'o':
				arg = g.getOptarg();
				outputfile = arg;
				break;
				//
			default:
				break;				
			}
		}		
	}
	

}
