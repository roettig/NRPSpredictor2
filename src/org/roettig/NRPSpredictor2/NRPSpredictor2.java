package org.roettig.NRPSpredictor2;

import gnu.getopt.Getopt;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			cur_adom.setSig8a(toks[0]);
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
					cur_adom.addDetection("nrps1_lc", toks[0], Double.parseDouble(toks[1]));
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
					cur_adom.addDetection("nrps1_sc", toks[0], Double.parseDouble(toks[1]));
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
		/*
		FileOutputStream f_out = new FileOutputStream(filename);
		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
		// Write object out to disk
		obj_out.writeObject( data );
		f_out.close();
		*/
		XMLEncoder encoder =
	           new XMLEncoder(
	              new BufferedOutputStream(
	                new FileOutputStream(outputfile)));
	    encoder.writeObject(data);
	    encoder.close();
	}
	
	public static List<ADomain> load(String filename) throws Exception
	{
		XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)));
        List<ADomain> o = (List<ADomain>) decoder.readObject();
        decoder.close();
        return o;
		/*
		// restore object from file ...
		// Read from disk using FileInputStream
		FileInputStream f_in = new FileInputStream(filename);

		// Read object using ObjectInputStream
		ObjectInputStream obj_in = new ObjectInputStream (f_in);
		List<ADomain> ret = (List<ADomain>) obj_in.readObject();
		f_in.close();
		return ret;
		*/
	}
	
	public static void main(String[] argv) throws Exception
	{
		modeldir = System.getProperty("modeldir",modeldir);
		parseCommandline(argv);
		
		if(useNRPS1input)
		{
			System.out.println("using NRPS1 input");
			parseNRPS1(inputfile);
		}
		else
		{
			System.out.println("using signature input");
			parseSigs(inputfile);
		}
		// we now have a list of adomain objects
		if(bacterialMode)
			bacterialPrediction();
		else
			fungalPrediction();
		
		// export annotated adomains
		store(outputfile,adoms);
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
	
	public static Map<String,Double> precs = new HashMap<String,Double>();
	
	public static void fillPrecs()
	{
		String three_class[] = {"hydrophilic","hydrophobic-aliphatic","hydrophobic-aromatic"};
		String large_cluster[] = {"phe,trp,phg,tyr,bht","ser,thr,dhpg,hpg","gly,ala,val,leu,ile,abu,iva","asp,asn,glu,gln,aad","cys","orn,lys,arg","pro,pip","dhb,sal"};
		String small_cluster[] = {"aad","val,leu,ile,abu,iva","arg","asp,asn","cys","dhb,sal","glu,gln","orn,horn","tyr,bht","pro","ser","dhpg,hpg","phe,trp","gly,ala","thr"};
		String single_cluster[] = {"aad","ala","arg","asn","asp","bht","cys","dhb","dhpg","gln","glu","gly","hpg","ile","iva","leu","lys","orn","phe","pip","pro","ser","thr","trp","tyr","val"};
		precs.put("hydrophilic",0.963);
		precs.put("hydrophobic-aliphatic",0.954);
		precs.put("hydrophobic-aromatic",0.973);
		precs.put("phe,trp,phg,tyr,bht", 0.978);
		precs.put("ser,thr,dhpg,hpg", 0.970 );
		precs.put("gly,ala,val,leu,ile,abu,iva",0.956 );
		precs.put("asp,asn,glu,gln,aad", 0.956);
		precs.put("cys", 0.996);
		precs.put("orn,lys,arg", 0.984);
		precs.put("pro,pip", 0.994);
		precs.put("dhb,sal", 1.00);
		precs.put("aad",1.00 );
		precs.put("val,leu,ile,abu,iva", 0.979);
		precs.put("arg",0.994 );
		precs.put("asp,asn", 0.996);
		precs.put("cys", 0.996);
		precs.put("dhb,sal", 1.000);
		precs.put("glu,gln", 0.967);
		precs.put("orn,horn",0.994 );
		precs.put("tyr,bht", 0.958);
		precs.put("pro", 0.996);
		precs.put("ser", 0.998);
		precs.put("dhpg,hpg", 1.000);
		precs.put("phe,trp", 0.894);
		precs.put("gly,ala", 0.973);
		precs.put("thr", 1.000);
		precs.put("ala",0.968);
		precs.put("asn",1.000);
		precs.put("asp",0.898);
		precs.put("bht",0.828);
		precs.put("dhb",0.962);
		precs.put("dhpg",0.996);
		precs.put("gln",0.870);
		precs.put("glu",0.991);
		precs.put("gly",0.998);
		precs.put("hpg",0.969);
		precs.put("ile",0.871);
		precs.put("iva",0.778);
		precs.put("leu",0.957);
		precs.put("lys",0.996);
		precs.put("phe",0.852);
		precs.put("trp",0.496);
		precs.put("tyr",0.721);
		precs.put("val",0.826);
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
				if(precs.containsKey(label))
					ad.addDetection(type, label, yp, precs.get(label));
				else
					ad.addDetection(type, label, yp, 0.0);
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
		fillPrecs();
		Getopt g = new Getopt("NRPSpredictor2", argv, "i:o:b:s:");
		//
		int c;
		String arg;
		while ((c = g.getopt()) != -1)
		{
			switch(c)
			{
			case 's':
				arg = g.getOptarg();
				if(arg.equals("1"))
					useNRPS1input = false;
				else
					useNRPS1input = true;
				break;
				//
			case 'b':
				arg = g.getOptarg();
				if(arg.equals("1"))
					bacterialMode = true;
				else
					bacterialMode = false;
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
