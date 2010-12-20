package org.roettig.NRPSpredictor2;

import gnu.getopt.Getopt;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
	
	public static void main(String[] argv) throws Exception
	{
		datadir = System.getProperty("datadir",datadir);
		
		if(argv.length==0)
		{
			System.out.println("Usage: NRPSPredictor2 -i <inputfile> -r <reportfile> -s [0|1 use signatures?]\n");
			System.exit(1);
		}
		
		initSigDB();
		
		parseCommandline(argv);
		
		banner();
		
		if(useNRPS1input)
		{
			System.out.println("## using NRPS1 input");
			parseNRPS1(inputfile);
		}
		else
		{
			System.out.println("## using signature input");
			parseSigs(inputfile);
		}
		
		System.out.println("## start predicting on "+adoms.size()+" signatues");
		
		// we now have a list of adomain objects
		if(bacterialMode)
			bacterialPrediction();
		else
			fungalPrediction();
		
		// export annotated adomains
		
		if(outputfile!=null)
			store(outputfile,adoms);
		if(reportfile!=null)
			report(reportfile,adoms);
	}
	
	public static void banner()
	{
		System.out.println("\n");
		System.out.println(" ##        Welcome to NRPSpredictor2 by       ##");
		System.out.println(" ##    Marc Röttig, Marnix Medema, Kai Blin   ##");
		System.out.println(" ##     based on work by Christian Rausch     ##\n");
		System.out.println(" please cite: http://dx.doi.org/10.1093/nar/gki885");
		System.out.println(" please cite: <hopefully NARW paper here>\n\n");
	}
	
	private static boolean useNRPS1input  = false;
	private static boolean bacterialMode  = true;
	private static String inputfile;
	private static String outputfile;
	private static String reportfile;
	private static String datadir = "/home/roettig/coops/nrps2/models";
	
	private static List<ADomain> adoms = new ArrayList<ADomain>();
	
	public static void parseSigs(String filename) throws Exception
	{
		if(!checkSignatureFormat(new File(filename)))
			crash("invalid signature file supplied");
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		ADomain cur_adom = null;
		while ((line = br.readLine()) != null)   
		{
			line = line.trim();
			if(line.equals(""))
				continue;
			String toks[] = line.split("\\\t");
			cur_adom = new ADomain();
			cur_adom.setSig8a(toks[0]);
			if(toks.length>1)
				cur_adom.sid = toks[1];
			adoms.add(cur_adom);
		}
	}
	
	public static boolean checkSignatureFormat(File infile) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = "";
		while((line=br.readLine())!=null)
		{
			line = line.trim();
			if(line.equals(""))
				continue;
			String toks[] = line.split("\\t");
			if(toks.length!=2)
			{
				return false;
			}
			String sig = toks[0].toUpperCase();
			if(sig.length()!=34)
			{
				return false;
			}
			if(!sig.matches("[A,C,D,E,F,G,H,I,K,L,M,N,P,Q,R,S,T,V,W,X,Y,-]+"))
				return false;
		}
		br.close();
		return true;
	}
	
	private static void crash(String msg)
	{
		System.err.println(msg);
		System.exit(1);
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
	}
	
	
	
	private static List<String> specs     = new ArrayList<String>();
	private static List<String> sig8a     = new ArrayList<String>();
	private static List<String> sigstach = new ArrayList<String>();
	
	private static void initSigDB() throws IOException
	{
		BufferedReader in = new BufferedReader(new FileReader(datadir+"/labeled_sigs"));
		String line = "";
		while((line=in.readLine())!=null)
		{
			String toks[] = line.split("\\\t");
			specs.add(toks[0]);
			sig8a.add(toks[1]);
			sigstach.add(toks[2]);
		}
		in.close();
	}
	
	

	private static void report(String outputfile, List<ADomain> adoms) throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter( new File(reportfile) );
		for(ADomain adom: adoms)
		{
			// id <tab> 8a_sig <tab> stachelhaus_sig <tab> three_class_prediction_nrps2 <tab> large_cluster_prediction_nrps2 <tab> small_cluster_prediction_nrps2 <tab> single_aa_prediction_nrps2 <tab> nearest_stachelhaus_specificity <tab> large_cluster_prediction_nrps1 <tab> small_cluster_prediction_nrps1 <tab> isAdomainUnusual?
			
			Detection best_THREE_CLASS = adom.getBestDetection(ADomain.NRPS2_THREE_CLUSTER);
			String pred_THREE_CLASS = (best_THREE_CLASS!=null?best_THREE_CLASS.getLabel():"N/A");
			
			Detection best_LARGE_CLASS = adom.getBestDetection(ADomain.NRPS2_LARGE_CLUSTER);
			String pred_LARGE_CLASS = (best_LARGE_CLASS!=null?best_LARGE_CLASS.getLabel():"N/A");
			
			Detection best_SMALL_CLASS = adom.getBestDetection(ADomain.NRPS2_SMALL_CLUSTER);
			String pred_SMALL_CLASS = (best_SMALL_CLASS!=null?best_SMALL_CLASS.getLabel():"N/A");
			
			Detection best_SINGLE_CLASS = adom.getBestDetection(ADomain.NRPS2_SINGLE_CLUSTER);
			String pred_SINGLE_CLASS = (best_SINGLE_CLASS!=null?best_SINGLE_CLASS.getLabel():"N/A");
			
			Detection best_STACH_NN = adom.getBestDetection(ADomain.NRPS2_STACH_NN);
			String pred_STACH_NN = (best_STACH_NN!=null?best_STACH_NN.getLabel():"N/A");
			
			Detection best_LARGE_CLASS1 = adom.getBestDetection(ADomain.NRPS1_LARGE_CLUSTER);
			String pred_LARGE_CLASS1 = (best_LARGE_CLASS1!=null?best_LARGE_CLASS1.getLabel():"N/A");
			
			Detection best_SMALL_CLASS1 = adom.getBestDetection(ADomain.NRPS1_SMALL_CLUSTER);
			String pred_SMALL_CLASS1 = (best_SMALL_CLASS1!=null?best_SMALL_CLASS1.getLabel():"N/A");
			
			out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%d",adom.sid,adom.sig8a,adom.sigstach, pred_THREE_CLASS, pred_LARGE_CLASS, pred_SMALL_CLASS, pred_SINGLE_CLASS, pred_STACH_NN, pred_LARGE_CLASS1, pred_SMALL_CLASS1 ,(adom.isOutlier()?1:0)));
		}
		out.close();
	}

	public static void fungalPrediction() throws MalformedURLException, ParseException
	{	
		
	}
	
	public static void bacterialPrediction() throws ParseException, IOException
	{	
		PrimalEncoder encR = new PrimalRauschEncoder();
		
		// check applicability domain
		checkAD("bacterial");
		
		//////////
		// NRPS1
		/////////
		
		// large cluster predictions
		String large_cluster[] = {"phe,trp,phg,tyr,bht","ser,thr,dhpg,hpg","gly,ala,val,leu,ile,abu,iva","asp,asn,glu,gln,aad","cys","orn,lys,arg","pro,pip","dhb,sal"}; 
		
		for(String sc: large_cluster)
		{
			detect(ADomain.NRPS1_LARGE_CLUSTER, sc, encR);
		}
		
		// small cluster predictions
		String small_cluster[] = {"aad","val,leu,ile,abu,iva","arg","asp,asn","cys","dhb,sal","glu,gln","orn,horn","tyr,bht","pro","ser","dhpg,hpg","phe,trp","gly,ala","thr"}; 
		for(String sc: small_cluster)
		{			
			detect(ADomain.NRPS1_SMALL_CLUSTER, sc, encR);
		}
		
		
		//////////
		// NRPS2 
		/////////
		
		PrimalEncoder encW = new PrimalWoldEncoder();
		
		// 3 class predictions
		String three_class[] = {"hydrophilic","hydrophobic-aliphatic","hydrophobic-aromatic"};
		for(String sc: three_class)
		{
			detect(ADomain.NRPS2_THREE_CLUSTER, sc, encW);
		}
		
		// large cluster predictions
		//String large_cluster[] = {"phe,trp,phg,tyr,bht","ser,thr,dhpg,hpg","gly,ala,val,leu,ile,abu,iva","asp,asn,glu,gln,aad","cys","orn,lys,arg","pro,pip","dhb,sal"}; 
		
		for(String sc: large_cluster)
		{
			detect(ADomain.NRPS2_LARGE_CLUSTER, sc, encW);
		}
		
		// small cluster predictions
		//String small_cluster[] = {"aad","val,leu,ile,abu,iva","arg","asp,asn","cys","dhb,sal","glu,gln","orn,horn","tyr,bht","pro","ser","dhpg,hpg","phe,trp","gly,ala","thr"}; 
		for(String sc: small_cluster)
		{			
			detect(ADomain.NRPS2_SMALL_CLUSTER, sc, encW);
		}
		
		// single aa predictions
		String single_cluster[] = {"aad","ala","arg","asn","asp","bht","cys","dhb","dhpg","gln","glu","gly","hpg","ile","iva","leu","lys","orn","phe","pip","pro","ser","thr","trp","tyr","val"}; 
		for(String sc: single_cluster)
		{
			detect(ADomain.NRPS2_SINGLE_CLUSTER, sc, encW);
		}
		
		detectStachNN();
	}
	
	public static void checkAD(String model) throws IOException
	{
		svm_model m = svm.svm_load_model(datadir+String.format("/models/%s_1class.mdl",model));
		
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
	
	public static void detect(String type, String label, PrimalEncoder enc) throws MalformedURLException, ParseException
	{
		SVMLightModel m = SVMLightModel.readSVMLightModelFromURL(new URL(String.format("file:///%s/models/%s/[%s].mdl",datadir,type,label)));
		
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
	
	public static void detectStachNN()
	{
		for(ADomain ad: adoms)
		{
			String[] hitspec = new String[1];
			double sim = nearestSignature(ad, hitspec);
			ad.addDetection(ADomain.NRPS2_STACH_NN, hitspec[0], sim);
		}
	}
	
	public static double nearestSignature(ADomain ad, String[] hit)
	{
		String a_sig   = ad.sigstach;
		int maxMatches = 0;
		int idx = 0;
		
		for(String sig: sigstach)
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
		
		Getopt g = new Getopt("NRPSpredictor2", argv, "i:o:b:s:r:");
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
			case 'r':
				arg = g.getOptarg();
				reportfile = arg;
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
