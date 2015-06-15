package org.roettig.NRPSpredictor2.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.roettig.NRPSpredictor2.extraction.ADomSigExtractor;
import org.roettig.NRPSpredictor2.extraction.ADomain;
import org.roettig.NRPSpredictor2.hmmer.HMMPfam;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainAlignment;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainHit;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.QueryResult;
import org.roettig.NRPSpredictor2.resources.ResourceManager;


public class Helper
{
    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(Helper.class);
    
    
    /**
     * Copys (deploys) a file from Java resource to file system.
     *  
     *  
     * @param in
     * @param filename
     * 
     * @throws IOException
     */
	public static void deployFile(InputStream in, String filename) 
	throws 
	    IOException
	{
		FileOutputStream fout = null; 
		
		byte[] buffer = new byte[2048];
		int size;

		try
		{
		    fout = new FileOutputStream(filename);
		    
		    while ((size = in.read(buffer, 0, 2048)) != -1)
	        {
	            fout.write(buffer, 0, size);
	        }    
		}
		finally
		{
		    if(null!=fout)
		    {
		        fout.flush();
	            fout.close();
		    }
		    if(null!=in)
		        in.close();    
		}
	}
	
	/**
     * Copys (deploys) a file from Java resource to file system (as temp file).
     *  
     * @param in
     * 
     * @throws IOException
     */
	
	public static File deployFile(InputStream in)
	{
		File tmp = null;
		try
		{
			tmp = File.createTempFile("TEMP", "TMP");
			deployFile(in,tmp.getAbsolutePath());
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
				
		return tmp;
	}
	
	public static File dumpStringToFile(String sid, String data)
	{
		File tmp = null;
		try
		{
			tmp = File.createTempFile("TEMP", "TMP");
			FileOutputStream out = new FileOutputStream(tmp.getAbsolutePath());
			out.write(String.format(">%s\n",sid).getBytes());
			out.write(data.getBytes());
			out.close();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		return tmp;
	}
	
	public static List<ADomain> extractADomainsFromFullSequence(String sid, String inputData)
	{
		List<ADomain> adoms = new ArrayList<ADomain>();
		
		double evalue = 0.00001;
		HMMPfam hmmpfam = new HMMPfam();
		File model = Helper.deployFile(ResourceManager.class.getResourceAsStream("aa-activating.aroundLys.hmm"));
		File seqFile = dumpStringToFile(sid, inputData);
		hmmpfam.run(evalue, model, seqFile);

		List<QueryResult> res = hmmpfam.getResults();

		ADomain cur_adom = null;

		for (QueryResult qr : res)
		{
			List<DomainHit> hits = qr.getHits();
			List<DomainAlignment> alis = qr.getAlignments();

			int N = hits.size();

			List<DomainHit> a_hits = new ArrayList<DomainHit>();
			List<DomainAlignment> a_alis = new ArrayList<DomainAlignment>();

			List<DomainHit> l_hits = new ArrayList<DomainHit>();
			List<DomainAlignment> l_alis = new ArrayList<DomainAlignment>();

			// find matching ADOM-LysDOM pairs
			for (int i = 0; i < N; i++)
			{
				DomainHit hit = hits.get(i);
				DomainAlignment ali = alis.get(i);

				if (hit.hmmname.equals("aa-activating-core.198-334"))
				{
					a_alis.add(ali);
					a_hits.add(hit);
				}
				if (hit.hmmname.equals("aroundLys517"))
				{
					l_alis.add(ali);
					l_hits.add(hit);
				}
			}

			int A = a_hits.size();
			int L = l_hits.size();

			Map<Integer, Integer> aIdx2lIdx = new HashMap<Integer, Integer>();

			int J = 0;
			for (int i = 0; i < A; i++)
			{
				DomainHit adom_hit = a_hits.get(i);

				int Aend = adom_hit.seqto;
				for (int j = J; j < L; j++)
				{
					DomainHit lys_hit = l_hits.get(j);

					int Lstart = lys_hit.seqfrom;
					if ((Aend + 200) > Lstart && (Aend < Lstart))
					{
						logger.info("matching ADomain hit " + i
								+ " with LysDomain hit " + j);
						aIdx2lIdx.put(i, j);
						J++;
						break;
					}
				}
			}
			for (int i = 0; i < A; i++)
			{
				DomainHit adom_hit = a_hits.get(i);
				DomainAlignment adom_ali = a_alis.get(i);

				DomainAlignment ldom_ali = null;
				Integer mIdx = aIdx2lIdx.get(i);
				if (mIdx != null)
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
					cur_adom.sig8a = e.get8ASignature();
					cur_adom.sigstach = e.getStachelhausCode();
				}
				catch (Exception ex)
				{
					continue;
				}

				logger.info(cur_adom.sig8a + " - " + cur_adom.sigstach);

				cur_adom.sid = qr.getQueryId();
				cur_adom.startPos = adom_hit.seqfrom;
				cur_adom.endPos = adom_hit.seqto;
				cur_adom.pfamscore = adom_hit.score;

				adoms.add(cur_adom);
			}
		}
		
		return adoms;
	}
}
