package org.roettig.NRPSpredictor2.predictors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.roettig.NRPSpredictor2.svm.SVMlightModel;

public class NRPSPredictorModel extends SVMlightModel
{
	private String type;
	
	public NRPSPredictorModel(InputStream in) throws FileNotFoundException,
			IOException
	{
		super(in);
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	public static NRPSPredictorModel loadModel(String label, String type)
	{
		NRPSPredictorModel model = null;
		try
		{
			model = new NRPSPredictorModel(new FileInputStream(String.format("data/models/%s/[%s].mdl", type, label)));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return model;
	}
}
