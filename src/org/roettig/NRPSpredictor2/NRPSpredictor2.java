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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import libsvm.svm;
import libsvm.svm_model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.roettig.NRPSpredictor2.extraction.ADomSigExtractor;
import org.roettig.NRPSpredictor2.extraction.ADomain;
import org.roettig.NRPSpredictor2.hmmer.HMMPfam;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainAlignment;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainHit;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.QueryResult;
import org.roettig.NRPSpredictor2.predictors.ADChecker;
import org.roettig.NRPSpredictor2.predictors.BacterialNRPSPredictor;
import org.roettig.NRPSpredictor2.predictors.BacterialNRPSPredictor2;
import org.roettig.NRPSpredictor2.predictors.Detection;
import org.roettig.NRPSpredictor2.predictors.FungalNRPSPredictor2;
import org.roettig.NRPSpredictor2.resources.ResourceManager;
import org.roettig.NRPSpredictor2.util.Helper;


public class NRPSpredictor2
{	
	/**
	 * The logger.
	 */
	private static final Logger logger = LogManager.getLogger(NRPSpredictor2.class);
	
	public static void main(String[] argv) throws Exception
	{
		
		Locale locale = new Locale("en", "UK");
		Locale.setDefault(locale);

		try
		{
			datadir = System.getProperty("datadir",datadir);
			evalue  = Double.parseDouble(System.getProperty("evalue","0.00001"));
			
			if(argv.length==0)
			{
				banner();
				logger.info("");
				logger.info("Usage: NRPSPredictor2 -i <inputfile> -r <reportfile> -s [0|1 use signatures?]\n");
				logger.info("");
				logger.info("The inputfile can either be a flatfile with signatures or multi-fastafile with full sequences.");
				logger.info("An exemplary signature file can be found in the examples directory");
				System.exit(0);
			}
			

			parseCommandline(argv);

			banner();

			if(!(new File(inputfile).exists()))
			{
				logger.error("Error: Input file "+inputfile+" does not exist");
				System.exit(1);
			}
			
			if(extractsigs)
			{
				logger.info("## extracting signatures from fasta file");
				extractSigs(inputfile);
			}
			else
			{
				logger.info("## using signature input");
				parseSigs(inputfile);
			}
			

			logger.info("## start predicting on "+adoms.size()+" signatures");

			// we now have a list of adomain objects
			if(bacterialMode)
			{
				logger.info("## bacterial mode");
				bacterialPrediction();
			}
			else
			{
				logger.info("## fungal mode");
				fungalPrediction();
			}

			// export annotated adomains

			if(outputfile!=null)
				store(outputfile,adoms);
			if(reportfile!=null)
				report(reportfile,adoms);
		}
		catch(Throwable e)
		{
			logger.error(e.getMessage(),e);
			System.exit(1);
		}
	}
	
	private static void debug(String message)
	{
		if(debug)
			logger.debug(message);
	}
	
	private static double evalue = 0.00001;
	
	private static void extractSigs(String infile) 
	throws 
		Exception
	{
		HMMPfam hmmpfam = new HMMPfam();
		File model = Helper.deployFile(ResourceManager.class.getResourceAsStream("aa-activating.aroundLys.hmm"));
		hmmpfam.run( evalue, model, new File(infile));
		
		List<QueryResult> res = hmmpfam.getResults();
		
		ADomain cur_adom = null;
		
		for(QueryResult qr : res)
		{
			List<DomainHit>       hits = qr.getHits();
			List<DomainAlignment> alis = qr.getAlignments();
			
			int N = hits.size();
			
			List<DomainHit>       a_hits = new ArrayList<DomainHit>();
			List<DomainAlignment> a_alis = new ArrayList<DomainAlignment>();
			
			List<DomainHit>       l_hits = new ArrayList<DomainHit>();
			List<DomainAlignment> l_alis = new ArrayList<DomainAlignment>();
			
			// find matching ADOM-LysDOM pairs
			for(int i=0;i<N;i++)
			{
				DomainHit       hit = hits.get(i);
				DomainAlignment ali = alis.get(i);
				
				if(hit.hmmname.equals("aa-activating-core.198-334"))
				{
					a_alis.add(ali);
					a_hits.add(hit);
				}
				if(hit.hmmname.equals("aroundLys517"))
				{
					l_alis.add(ali);
					l_hits.add(hit);
				}
			}
			
			int A = a_hits.size();
			int L = l_hits.size();
			
			Map<Integer,Integer> aIdx2lIdx = new HashMap<Integer,Integer>();
			
			int J = 0;
			for(int i=0;i<A;i++)
			{
				DomainHit       adom_hit = a_hits.get(i);
				
				int Aend = adom_hit.seqto;
				for(int j=J;j<L;j++)
				{
					DomainHit       lys_hit = l_hits.get(j);
					
					int Lstart = lys_hit.seqfrom;
					if( (Aend+200)>Lstart && (Aend<Lstart)) 
					{
						debug("matching ADomain hit "+i+" with LysDomain hit "+j);
						aIdx2lIdx.put(i, j);
						J++;
						break;
					}
				}
			}
			for(int i=0;i<A;i++)
			{
				DomainHit       adom_hit = a_hits.get(i);
				DomainAlignment adom_ali = a_alis.get(i);
				
				
				
				DomainAlignment ldom_ali = null;
				Integer mIdx = aIdx2lIdx.get(i);
				if(mIdx!=null)
				{
					ldom_ali = l_alis.get(aIdx2lIdx.get(i));
				}
				
				ADomSigExtractor e = new ADomSigExtractor();
				e.setADomain(adom_ali);
				e.setLDomain(ldom_ali);
				e.extract();

				cur_adom = new ADomain();
				
				try
				{
					cur_adom.sig8a    = e.get8ASignature();
					cur_adom.sigstach = e.getStachelhausCode();
				}
				catch(Exception ex)
				{
					continue;
				}
				
				
				debug(cur_adom.sig8a+" - "+cur_adom.sigstach);
				
				cur_adom.sid       = qr.getQueryId();
				cur_adom.startPos  = adom_hit.seqfrom;
				cur_adom.endPos    = adom_hit.seqto;
				cur_adom.pfamscore = adom_hit.score;
				
				adoms.add(cur_adom);
			
			}
		}
	}

	public static void banner()
	{
		logger.info("\n");
		logger.info(" ##        Welcome to NRPSpredictor2 by        ##");
		logger.info(" ##    Marc Roettig, Marnix Medema, Kai Blin   ##");
		logger.info(" ##     based on work by Christian Rausch      ##");
		if(debug)
			logger.info(" ##                DEBUG MODE                  ##");
		logger.info(" ##                                            ##\n");
		logger.info(" please cite: http://dx.doi.org/10.1093/nar/gki885");
		logger.info(" please cite: http://dx.doi.org/10.1093/nar/gkr323\n\n");			
	}
	
	private static boolean extractsigs    = false;
	private static boolean bacterialMode  = true;
	private static boolean debug = false;
	private static String inputfile;
	private static String outputfile;
	private static String reportfile;
	private static String datadir ="data";
	
	private static List<ADomain> adoms = new ArrayList<ADomain>();
	
	public static void parseSigs(String filename) throws Exception
	{
		if(!checkSignatureFormat(new File(filename)))
			crash("invalid signature file supplied");
		
		BufferedReader br = null;
		
		try
		{
			br = new BufferedReader(new FileReader(filename));
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
		finally
		{
			if(null!=br)
				br.close();
		}
	}
	
	public static boolean checkSignatureFormat(File infile) 
	throws 
		IOException
	{
		BufferedReader br = null;
		
		try
		{
			br= new BufferedReader(new FileReader(infile));
		
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
		}
		finally
		{
			if(null!=br)
				br.close();
		}
		return true;
	}
	
	private static void crash(String msg)
	{
		logger.error(msg);
		System.exit(1);
	}

	public static String fixLabel(String lab)
	{
		
		// large cluster predictions
		//String large_cluster[] = {"phe,trp,phg,tyr,bht","ser,thr,dhpg,hpg","gly,ala,val,leu,ile,abu,iva","asp,asn,glu,gln,aad","cys","orn,lys,arg","pro,pip","dhb,sal"}; 
	
		// small cluster predictions
		//String small_cluster[] = {"aad","val,leu,ile,abu,iva","arg","asp,asn","cys","dhb,sal","glu,gln","orn,horn","tyr,bht","pro","ser","dhpg,hpg","phe,trp","gly,ala","thr"};	 

		
		String ret = "N/A";
		if(lab.equals("phe=trp=phg=tyr=bht"))
			ret = "phe,trp,phg,tyr,bht";
		if(lab.equals("ser=thr=ser-thr=dht=dhpg=dpg=hpg"))
			ret = "ser,thr,dhpg,hpg";
		if(lab.equals("gly=ala=val=leu=ile=abu=iva"))
			ret = "gly,ala,val,leu,ile,abu,iva";
		if(lab.equals("asp=asn=glu=gln=aad"))
			ret = "asp,asn,glu,gln,aad";
		if(lab.equals("cys"))
			ret = "cys";
		if(lab.equals("orn=lys=arg"))
			ret = "orn,lys,arg";
		if(lab.equals("pro=pip"))
			ret = "pro,pip";
		if(lab.equals("dhb=sal"))
			ret = "dhb,sal";
		
		if(lab.equals("aaf"))
			ret = "aad";
		if(lab.equals("val=leu=ile=abu=iva"))
			ret = "val,leu,ile,abu,iva";
		if(lab.equals("arg"))
			ret = "arg";		
		if(lab.equals("asp=asn"))
			ret = "asp,asn";
		if(lab.equals("cys"))
			ret = "cys";
		if(lab.equals("dhb=sal"))
			ret = "dhb,sal";
		if(lab.equals("glu=gln"))
			ret = "glu,gln";
		if(lab.equals("orn"))
			ret = "orn,horn";		
		if(lab.equals("tyr=bht"))
			ret = "tyr,bht";		
		if(lab.equals("pro"))
			ret = "pro";		
		if(lab.equals("ser"))
			ret = "ser";		
		if(lab.equals("dhpg=dpg=hpg"))
			ret = "dhpg,hpg";		
		if(lab.equals("phe=trp"))
			ret = "phe,trp";
		if(lab.equals("gly=ala"))
			ret = "gly,ala";		
		if(lab.equals("thr=dht"))
			ret = "thr,dht";		
		return ret;
	}
	
	public static void parseNRPS1(String filename) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		ADomain cur_adom = null;
		while ((line = br.readLine()) != null)   
		{
			if(line.startsWith("[no hits above thresholds]"))
			{
				cur_adom = null;
			}
			if(line.startsWith("Score: "))
			{
				String scoreline = line.substring(6).trim();
				double score = 0;
				try
				{
					score = Double.parseDouble(scoreline);
				}
				catch(Exception e)
				{
					
				}
				cur_adom.pfamscore = score;
			}
			if(line.startsWith("Loc: "))
			{
				String locsline = line.substring(4).trim();
				String[] locs = locsline.split("_");
				
				int start = -1;
				int end   = -1;
				try
				{
					start = Integer.parseInt(locs[0]);
					end   = Integer.parseInt(locs[1]);
				}
				catch(Exception e)
				{
					
				}
				cur_adom.startPos = start;
				cur_adom.endPos   = end;
			}
			if(line.startsWith("Modul: "))
			{
				if(cur_adom!=null)
					adoms.add(cur_adom);
				cur_adom = new ADomain();

				String sid = line.substring(7).trim();
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
					String label = fixLabel(toks[0]);
					if(precsNRPS1.containsKey(label))
						cur_adom.addDetection(ADomain.NRPS1_LARGE_CLUSTER, toks[0], Double.parseDouble(toks[1]),precsNRPS1.get(label));
					else
						cur_adom.addDetection(ADomain.NRPS1_LARGE_CLUSTER, toks[0], Double.parseDouble(toks[1]),0.0);
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
					String label = fixLabel(toks[0]);
					if(precsNRPS1.containsKey(label))
						cur_adom.addDetection(ADomain.NRPS1_SMALL_CLUSTER, toks[0], Double.parseDouble(toks[1]),precsNRPS1.get(label));
					else
						cur_adom.addDetection(ADomain.NRPS1_SMALL_CLUSTER, toks[0], Double.parseDouble(toks[1]),0.0);
					//cur_adom.addDetection(ADomain.NRPS1_SMALL_CLUSTER, toks[0], Double.parseDouble(toks[1]));
				}
				continue;
			}
			
		}
		
		if(cur_adom!=null)
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
	
	@SuppressWarnings("unchecked")
	public static List<ADomain> load(String filename) throws Exception
	{
		XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)));
        List<ADomain> o = (List<ADomain>) decoder.readObject();
        decoder.close();
        return o;
	}
		
	private static void report(String outputfile, List<ADomain> adoms) throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter( new File(reportfile) );
		out.println(String.format("#%s<tab>%s<tab>%s<tab>%s<tab>%s<tab>%s<tab>%s<tab>%s<tab>%s<tab>%s<tab>%s<tab>%s<tab>%s","sequence-id","8A-signature","stachelhaus-code","3class-pred","large-class-pred","small-class-pred","single-class-pred","nearest stachelhaus code","NRPS1pred-large-class-pred","NRPS2pred-large-class-pred","outside applicability domain?","coords","pfam-score"));
		for(ADomain adom: adoms)
		{
			// id <tab> 8a_sig <tab> stachelhaus_sig <tab> three_class_prediction_nrps2 <tab> large_cluster_prediction_nrps2 <tab> small_cluster_prediction_nrps2 <tab> single_aa_prediction_nrps2 <tab> nearest_stachelhaus_specificity <tab> large_cluster_prediction_nrps1 <tab> small_cluster_prediction_nrps1 <tab> isAdomainUnusual? <tab> coords <pfam-score>
			
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
			
			out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%d\t%s\t%e",adom.sid,adom.sig8a,adom.sigstach, pred_THREE_CLASS, pred_LARGE_CLASS, pred_SMALL_CLASS, pred_SINGLE_CLASS, pred_STACH_NN, pred_LARGE_CLASS1, pred_SMALL_CLASS1 ,(adom.isOutlier()?1:0),String.format("%d:%d",adom.startPos,adom.endPos),adom.pfamscore));
		}
		out.close();
	}
	
	public static void fungalPrediction() throws ParseException, IOException
	{	
		FungalNRPSPredictor2 fungalPred2 = new FungalNRPSPredictor2();
		fungalPred2.predict(adoms);
	}
	
	public static void bacterialPrediction() throws ParseException, IOException
	{	
		BacterialNRPSPredictor bactPred1 = new BacterialNRPSPredictor();
		bactPred1.predict(adoms);
		BacterialNRPSPredictor2 bactPred2 = new BacterialNRPSPredictor2();
		bactPred2.predict(adoms);
	}
	
	public static void checkAD(String model) throws IOException
	{	
		ADChecker adc = new ADChecker();
		svm_model mdl = svm.svm_load_model(String.format("data/models/%s_1class.mdl","bacterial"));
		adc.setModel(mdl);
		adc.check(adoms);
	}
	
	public static Map<String,Double> precs      = new HashMap<String,Double>();
	public static Map<String,Double> precsNRPS1 = new HashMap<String,Double>();
	
	public static void fillPrecs()
	{
		Properties props = new Properties();
		try 
		{
			props.load(ResourceManager.class.getResourceAsStream("NRPSpredictor2-model-qualities.properties"));
		} 
		catch (IOException e) 
		{
			throw new RuntimeException(e.getMessage(),e);
		}
		
		for(Object key: props.keySet())
		{
			precs.put(key.toString(), Double.parseDouble(props.get(key).toString()) );
		}
		
		props = new Properties();
		try 
		{
			props.load(ResourceManager.class.getResourceAsStream("NRPSpredictor1-model-qualities.properties"));
		} 
		catch (IOException e) 
		{
			throw new RuntimeException(e.getMessage(),e);
		}
		
		for(Object key: props.keySet())
		{
			precsNRPS1.put(key.toString(), Double.parseDouble(props.get(key).toString()) );
		}
	}
	
	public static void parseCommandline(String[] argv)
	{
		fillPrecs();
		
		Getopt g = new Getopt("NRPSpredictor2", argv, "i:o:b:s:r:d");
		//
		int c;
		String arg;
		while ((c = g.getopt()) != -1)
		{
			switch (c)
			{
				case 's':
					arg = g.getOptarg();
					if (arg.equals("1"))
						extractsigs = false;
					else
						extractsigs = true;
					break;
				//
				case 'b':
					arg = g.getOptarg();
					if (arg.equals("1"))
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
				case 'd':
					debug = true;
					debug("DEBUGMODE ON");
					break;
				default:
					break;
			}
		}		
	}
}
