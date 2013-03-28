package org.roettig.NRPSpredictor2.extraction;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.roettig.NRPSpredictor2.hmmer.HMMPfam;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainAlignment;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.DomainHit;
import org.roettig.NRPSpredictor2.hmmer.HMMPfamParser.QueryResult;
import org.roettig.NRPSpredictor2.resources.ResourceManager;
import org.roettig.NRPSpredictor2.util.Helper;


public class ADomSigExtractor
{
	private String a_topline;
	private String a_downline;
	
	private String lys_topline;
	private String lys_downline;
	
	
	private List<Integer>   indel_idx_topline = new ArrayList<Integer>();
	private List<Character> char_del_downline = new ArrayList<Character>();  
	
	public ADomSigExtractor()
	{
	}
	
	public void setADomain(DomainAlignment ali)
	{
		setADomainTopline(ali.target);
		setADomainDownline(ali.query);
	}
	
	public void setLDomain(DomainAlignment ali)
	{
		if(ali!=null)
		{
			setLysDomainTopline(ali.target);
			setLysDomainDownline(ali.query);
		}
		else
		{
			setLysDomainTopline("");
			setLysDomainDownline("");
		}
	}
	
	public void setADomainTopline(String s)
	{
		this.a_topline = s;
	}
	
	public void setADomainDownline(String s)
	{
		this.a_downline = s;
	}

	public void setLysDomainTopline(String s)
	{
		this.lys_topline = s;
	}
	
	public void setLysDomainDownline(String s)
	{
		this.lys_downline = s;
	}
	
	private void removeToplineIndels()
	{
		int N = a_topline.length();
		
		StringBuffer a_topline_tmp  = new StringBuffer();
		StringBuffer a_downline_tmp = new StringBuffer();
		
		for(int i=0;i<N;i++)
		{
			if(a_topline.charAt(i)=='.')
			{
				indel_idx_topline.add(i);
				char_del_downline.add(a_downline.charAt(i));
			}
			else
			{
				a_topline_tmp.append(a_topline.charAt(i));
				a_downline_tmp.append(a_downline.charAt(i));
			}
		}
		
		a_topline  = a_topline_tmp.toString();
		a_downline = a_downline_tmp.toString();
	}
	
	private String extract8ASignature() throws Exception
	{
		String s1 = extractCharacters( a_topline, a_downline, "KGVmveHrnvvnlvkwl", new int[]{12, 15, 16});
		
		String s2 = extractCharacters( a_topline, a_downline, "LqfssAysFDaSvweifgaLLnGgt", new int[]{3,8,9,10,11,12,13,14,17});
		
		String s3 = extractCharacters( a_topline, a_downline, "iTvlnltPsl", new int[]{4,5});
		
		String s4 = extractCharacters( a_topline, a_downline, "LrrvlvGGEaL", new int[]{4,5,6,7,8});
		
		String s5 = extractCharacters( a_topline, a_downline, "liNaYGPTEtTVcaTi", new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
		
		return s1+s2+s3+s4+s5;
	}
	
	private String extractStachelhausSignature() throws Exception
	{
		String s2 = extractCharacters( a_topline, a_downline, "LqfssAysFDaSvweifgaLLnGgt", new int[]{9,10,13});
		
		String s3 = extractCharacters( a_topline, a_downline, "iTvlnltPsl", new int[]{4});
		
		String s4 = extractCharacters( a_topline, a_downline, "LrrvlvGGEaL", new int[]{4,6});
		
		String s5 = extractCharacters( a_topline, a_downline, "liNaYGPTEtTVcaTi", new int[]{3,11,12});
		
		
		String s6 = "-";
		if(!lys_topline.equals(""))
				s6 = extractCharacters( lys_topline, lys_downline, "fvvLdalPLTpNGKlDRkALPaPd", new int[]{13});
		
		return s2+s3+s4+s5+s6;
	}
	
	private String extractCharacters(String target, String source, String anchor_pattern, int[] idxs) throws Exception
	{
		int start_idx = target.indexOf(anchor_pattern);
		String ret = "";
		
		if(start_idx<0)
			throw new Exception("could not find anchor pattern");
		
		for(Integer idx: idxs)
		{
			ret += source.charAt(start_idx+idx);
		}
		return ret;
	}
		
	public void run()
	{
		removeToplineIndels();
	}
	
	public String getStachelhausCode() throws Exception
	{
		return extractStachelhausSignature();
	}
	
	public String get8ASignature() throws Exception
	{
		
		return extract8ASignature();
	}
	
	public static void main(String[] args) throws Exception
	{
		ADomSigExtractor e = new ADomSigExtractor();
		e.setADomainTopline("KGVmveHrnvvnlvkwlneryflfgeeddllgesdrvLqfssAysFDaSvweifgaLLnGgtLVivpkefsetrlDpeaLaalieregiTvlnltPsllnllldaaeeatpdfapedlssLrrvlvGGEaLspslarrlrerfpdragvrliNaYGPTEtTVcaTi");
		e.setADomainDownline("KGVMVGQTAIVNRLLWMQN---HYPLTG-----EDVVAQKTP-CSFDVSVWEFFWPFIAGAKLVMAEPE---AHRDPLAMQQFFAEYGVTTTHFVPSMLAAFVASL---TPQTARQSCVTLKQVFCSGEALPADLCREWQQLTG--A--PLHNLYGPTEAAVDVSW");
        e.setLysDomainTopline("daaeLRahLaarLPdYMVPsaVkfvvLdalPLTpNGKlDRkALPaPdaaa");
        e.setLysDomainDownline("DTSALQAQLRETLPPHMVPVV--LLQLPQLPLSANGKLDRKALPLPELKA");
		e.run();
		System.out.println(e.get8ASignature());
		System.out.println(e.getStachelhausCode());
	}
	
}
