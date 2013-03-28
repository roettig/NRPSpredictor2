package org.roettig.NRPSpredictor2.predictors;

import java.util.List;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

import org.roettig.NRPSpredictor2.encoder.PrimalEncoder;
import org.roettig.NRPSpredictor2.encoder.PrimalWoldEncoder;
import org.roettig.NRPSpredictor2.extraction.ADomain;

public class ADChecker
{
	private svm_model     model   = null;
	private PrimalEncoder encoder = new PrimalWoldEncoder();
	
	public ADChecker()
	{
	}
	
	public svm_model getModel()
	{
		return model;
	}
	
	public void setModel(svm_model model)
	{
		this.model = model;
	}

	public PrimalEncoder getEncoder()
	{
		return encoder;
	}

	public void setEncoder(PrimalEncoder encoder)
	{
		this.encoder = encoder;
	}

	public void check(List<ADomain> adomains)
	{
		if(model==null)
			throw new RuntimeException("Model was not set.");
		
		for(ADomain ad: adomains)
		{
			double   fv[]  = encoder.encode(ad.sig8a);
			svm_node x[]   = makeSVMnode(fv,1);
			double   yp    = svm.svm_predict(model,x);
			if(yp>=0.0)
				ad.setOutlier(false);
			else
				ad.setOutlier(true);
		}
	}
	
	private static svm_node[] makeSVMnode(double[] row, int idx)
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
}
