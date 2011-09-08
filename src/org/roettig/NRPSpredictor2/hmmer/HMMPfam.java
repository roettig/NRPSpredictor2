package org.roettig.NRPSpredictor2.hmmer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainAlignment;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainHit;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.QueryResult;
import org.roettig.NRPSpredictor2.util.ToolRunner;

public class HMMPfam
{
	private static File executable;
	static
	{
		// deploy excutable
		try
		{
			executable = File.createTempFile("HMMPFAM", "EXE");
			FileOutputStream fout = new FileOutputStream(executable);
			byte[] buffer = new byte[2048];
			int size;
			
			InputStream zin = null;
			
			String os = System.getProperty("os.name");
			
			if(os.toLowerCase().contains("nux")||os.toLowerCase().contains("nix"))
			{
				zin = HMMPfam.class.getResourceAsStream("hmmpfam");
			}
			if(os.toLowerCase().contains("mac"))
			{
				zin = HMMPfam.class.getResourceAsStream("hmmpfam.macos64");
			}
			
			if(zin==null)
				throw new Exception("could not deploy hmmpfam binary for your platform :"+os);
			
			while ((size = zin.read(buffer, 0, 2048)) != -1)
			{
				fout.write(buffer, 0, size);
			}
			fout.flush();
			fout.close();
			zin.close();
			executable.setExecutable(true);
			executable.deleteOnExit();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public HMMPfam()
	{
		
	}
	
	private File logfile;
	
	public void run(double evalue, File hmmmodel, File sequences) throws Exception
	{
		logfile = File.createTempFile("HMMPFAM", "");
		logfile.deleteOnExit();

		ToolRunner tr = new ToolRunner();
		tr.run(executable.getAbsolutePath(), "-E", String.valueOf(evalue), "-l", logfile.getAbsolutePath(), hmmmodel.getAbsolutePath(), sequences.getAbsolutePath());
		
		if(tr.getReturnCode()!=0)
			throw new Exception("hmmpfam terminated with non-zero exit code");
	}
	
	public List<QueryResult> getResults() throws Exception
	{
		HMMPfamParser parser = new HMMPfamParser(logfile.getAbsolutePath());
		return parser.getResultsForQueries();
	}
	
	public static void main(String[] args) throws Exception
	{
		HMMPfam pfam = new HMMPfam();
		pfam.run( 0.001, new File("/tmp/aa-activating.aroundLys.hmm"), new File("/tmp/in.fa"));
		List<QueryResult> res = pfam.getResults();
		for(QueryResult q: res)
		{
			System.out.println(q.getQueryId());
			for(DomainHit hit: q.getHits())
			{
				System.out.println(hit.toString());
			}
			for(DomainAlignment ali: q.getAlignments())
			{
				System.out.println(ali.toString());
			}
		}
	}

}
