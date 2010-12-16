package org.roettig.NRPSpredictor2;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.roettig.utilities.StringHelper;

public class ADomain implements Serializable
{
	private static final long	serialVersionUID	= -8665277739063399904L;
	
	public class Detection
	{
		public Detection(String _type, String _label, double _score)
		{
			type  = _type;
			label = _label;
			score = _score;
		}
		public String type;
		public String label;
		public double score;
		
		public String toString()
		{
			return String.format(Locale.ENGLISH,"[%s / %s] score: %.3f", type, label, score );
		}
	}
	
	public String sid;
	public String sig8a;
	public String sigstach;
	public String specSmall;
	public String specLarge;
	public List<String> knn_specs = new ArrayList<String>();
	public List<String> threeclass_predictions = new ArrayList<String>();
	public List<Double> threeclass_scores = new ArrayList<Double>();
	public List<String> largecluster_predictions = new ArrayList<String>();
	public List<Double> largecluster_scores = new ArrayList<Double>();
	public List<String> smallcluster_predictions = new ArrayList<String>();
	public List<Double> smallcluster_scores = new ArrayList<Double>();
	public List<String> singlecluster_predictions = new ArrayList<String>();
	public List<Double> singlecluster_scores = new ArrayList<Double>();
	public List<Detection> detections = new ArrayList<Detection>();
	
	
	public List<Detection> getDetections()
	{
		return detections;
	}
	
	public void addDetection(String type, String label, double score)
	{
		if(type.equals("3class"))
			addThreeClassDetection(label,score);
		if(type.equals("lc"))
			addLargeClusterDetection(label,score);
		if(type.equals("sc"))
			addSmallClusterDetection(label,score);
		if(type.equals("single"))
			addSingleClusterDetection(label,score);
		detections.add( new Detection(type,label,score) );
	}
	
	public void addThreeClassDetection(String label, double score)
	{
		threeclass_predictions.add(label);
		threeclass_scores.add(score);
	}
	
	public void addLargeClusterDetection(String label, double score)
	{
		largecluster_predictions.add(label);
		largecluster_scores.add(score);
	}
	
	public void addSmallClusterDetection(String label, double score)
	{
		smallcluster_predictions.add(label);
		smallcluster_scores.add(score);
	}
	
	public void addSingleClusterDetection(String label, double score)
	{
		singlecluster_predictions.add(label);
		singlecluster_scores.add(score);
	}
	
	public List<String> getSinglecluster_predictions()
	{
		return singlecluster_predictions;
	}

	public void setSinglecluster_predictions(List<String> singlecluster_predictions)
	{
		this.singlecluster_predictions = singlecluster_predictions;
	}

	public List<Double> getSinglecluster_scores()
	{
		return singlecluster_scores;
	}

	public void setSinglecluster_scores(List<Double> singlecluster_scores)
	{
		this.singlecluster_scores = singlecluster_scores;
	}

	public List<String> getThreeclass_predictions()
	{
		return threeclass_predictions;
	}

	public void setThreeclass_predictions(List<String> threeclass_predictions)
	{
		this.threeclass_predictions = threeclass_predictions;
	}

	public List<Double> getThreeclass_scores()
	{
		return threeclass_scores;
	}

	public void setThreeclass_scores(List<Double> threeclass_scores)
	{
		this.threeclass_scores = threeclass_scores;
	}

	public List<String> getKnn_specs()
	{
		return knn_specs;
	}

	public void setKnn_specs(List<String> knn_specs)
	{
		this.knn_specs = knn_specs;
	}

	public List<Double> getLargecluster_scores()
	{
		return largecluster_scores;
	}

	public void setLargecluster_scores(List<Double> largecluster_scores)
	{
		this.largecluster_scores = largecluster_scores;
	}

	public List<String> getSmallcluster_predictions()
	{
		return smallcluster_predictions;
	}

	public void setSmallcluster_predictions(List<String> smallcluster_predictions)
	{
		this.smallcluster_predictions = smallcluster_predictions;
	}

	public List<Double> getSmallcluster_scores()
	{
		return smallcluster_scores;
	}

	public void setSmallcluster_scores(List<Double> smallcluster_scores)
	{
		this.smallcluster_scores = smallcluster_scores;
	}

	public List<String> getLargecluster_predictions()
	{
		return largecluster_predictions;
	}

	public void setLargecluster_predictions(List<String> largecluster_predictions)
	{
		this.largecluster_predictions = largecluster_predictions;
	}

	public String getSid()
	{
		return sid;
	}

	public void setSid(String sid)
	{
		this.sid = sid;
	}

	public String getSig8a()
	{
		return sig8a;
	}

	public void setSig8a(String sig8a)
	{
		this.sig8a = sig8a;
	}

	public String getSigstach()
	{
		return sigstach;
	}

	public void setSigstach(String sigstach)
	{
		this.sigstach = sigstach;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(sid+"|");
		sb.append(sigstach+"|");
		sb.append(sig8a+"|");
		sb.append(StringHelper.implode(largecluster_predictions));
		sb.append("|");
		sb.append(StringHelper.implode(largecluster_scores));
		sb.append("|");
		sb.append(StringHelper.implode(smallcluster_predictions));
		sb.append("|");
		sb.append(StringHelper.implode(smallcluster_scores));
		return sb.toString();
	}
	
	public void write(String filename) throws Exception
	{
        XMLEncoder encoder =
           new XMLEncoder(
              new BufferedOutputStream(
                new FileOutputStream(filename)));
        encoder.writeObject(this);
        encoder.close();
    }
	
	public static ADomain read(String filename) throws Exception 
	{
        XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)));
        ADomain o = (ADomain)decoder.readObject();
        decoder.close();
        return o;
    }


}