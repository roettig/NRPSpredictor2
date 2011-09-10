package org.roettig.NRPSpredictor2.hmmer;

import java.io.File;
import java.util.List;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.QueryResult;
import org.roettig.NRPSpredictor2.util.Helper;
import org.roettig.NRPSpredictor2.util.ToolRunner;

public class HMMPfam
{
	private static File executable;
	private static String tmpdir;

	static
	{
		// deploy excutable
		try
		{
			executable = File.createTempFile("HMMPFAM", "EXE");
			
			String os = System.getProperty("os.name");
			tmpdir    = System.getProperty("java.io.tmpdir");

			if(os.toLowerCase().contains("nux")||os.toLowerCase().contains("nix"))
			{
				executable = new File(tmpdir+File.separator+"hmmpfam");
				Helper.deployFile(HMMPfam.class.getResourceAsStream("hmmpfam"), executable.getAbsolutePath());
			}
			else if(os.toLowerCase().contains("mac"))
			{
				executable = new File(tmpdir+File.separator+"hmmpfam");
				Helper.deployFile(HMMPfam.class.getResourceAsStream("hmmpfam.macos64"), executable.getAbsolutePath());
			}
			else if(os.toLowerCase().contains("win"))
			{
				executable = new File(tmpdir+File.separator+"hmmpfam.exe");
				Helper.deployFile(HMMPfam.class.getResourceAsStream("hmmpfam.win32"), executable.getAbsolutePath());
			 	Helper.deployFile(HMMPfam.class.getResourceAsStream("cygwin1.dll"), tmpdir+File.separator+"cygwin1.dll");
			} 
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
		logfile = File.createTempFile("HMMPFAM", "RESULT");
		logfile.deleteOnExit();

		ToolRunner tr = new ToolRunner();
		tr.setJobDir(tmpdir);
		tr.run(executable.getAbsolutePath(), "-E", String.valueOf(evalue), "-l", logfile.getAbsolutePath(), hmmmodel.getAbsolutePath(), sequences.getAbsolutePath());
		if(tr.getReturnCode()!=0)
		{
			System.err.println(tr.getOutput());
			throw new Exception("hmmpfam terminated with non-zero exit code");
		}
	}
	
	public List<QueryResult> getResults() throws Exception
	{
		HMMPfamParser parser = new HMMPfamParser(logfile.getAbsolutePath());
		return parser.getResultsForQueries();
	}
}
