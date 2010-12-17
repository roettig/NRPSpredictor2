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
	
	public String sid;
	public String sig8a;
	public String sigstach;
	public String specSmall;
	public String specLarge;
	public boolean outlier;
	
	private List<Detection> detections = new ArrayList<Detection>();
	
	public boolean hasDetections(String pattern)
	{
		for(Detection det: detections)
		{
			if(det.getType().contains(pattern))
				return true;
		}
		return false;
	}
	
	public List<Detection> getDetections()
	{
		return detections;
	}

	public void setDetections(List<Detection> detections)
	{
		this.detections = detections;
	}

	public void addDetection(String type, String label, double score)
	{
		detections.add( new Detection(type,label,score) );
	}
	
	public void addDetection(String type, String label, double score, double prec)
	{
		detections.add( new Detection(type,label,score, prec) );
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
		if(sigstach==null)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(sig8a.charAt(5));
			sb.append(sig8a.charAt(6));
			sb.append(sig8a.charAt(9));
			sb.append(sig8a.charAt(12));
			sb.append(sig8a.charAt(14));
			sb.append(sig8a.charAt(16));
			sb.append(sig8a.charAt(21));
			sb.append(sig8a.charAt(29));
			sb.append(sig8a.charAt(30));
			sb.append('K');
			sigstach = sb.toString();
		}
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

	public boolean isOutlier()
	{
		return outlier;
	}

	public void setOutlier(boolean outlier)
	{
		this.outlier = outlier;
	}
	
	


}