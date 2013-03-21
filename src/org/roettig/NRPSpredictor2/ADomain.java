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
	public String spec;
	public int startPos;
	public int endPos;
	public double pfamscore;
	public boolean outlier;
	
	public static String NRPS2_THREE_CLUSTER         = "NRPS2_THREE_CLUSTER";
	public static String NRPS2_THREE_CLUSTER_FUNGAL  = "NRPS2_THREE_CLUSTER_FUNGAL";
	public static String NRPS2_LARGE_CLUSTER         = "NRPS2_LARGE_CLUSTER";
	public static String NRPS2_SMALL_CLUSTER         = "NRPS2_SMALL_CLUSTER";
	public static String NRPS2_SINGLE_CLUSTER        = "NRPS2_SINGLE_CLUSTER";
	public static String NRPS2_STACH_NN              = "NRPS2_STACH_NN";
	public static String NRPS2_STACH_NN_FUNGAL       = "NRPS2_STACH_NN_FUNGAL";
	public static String NRPS1_LARGE_CLUSTER         = "NRPS1_LARGE_CLUSTER";
	public static String NRPS1_SMALL_CLUSTER         = "NRPS1_SMALL_CLUSTER";
	
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
	
	public Detection getBestDetection(String type)
	{
		double maxScore = Double.NEGATIVE_INFINITY;
		Detection best  = null;
		for(Detection det: detections)
		{
			if(det.getType().equals(type))
			{
				if(det.getScore()>maxScore)
				{
					maxScore = det.getScore();
					best     = det;
				}
			}
		}
		return best;
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
	
	/**
	 * Gets the sequence id.
	 * @return
	 */
	public String getSid()
	{
		return sid;
	}

	/**
	 * Sets the sequence id.
	 * @param sid
	 */
	public void setSid(String sid)
	{
		this.sid = sid;
	}

	/**
	 * Gets the 8 angstrom signature.
	 * @return
	 */
	public String getSig8a()
	{
		return sig8a;
	}

	/**
	 * Sets the 8 angstrom signature.
	 * 
	 * @param sig8a
	 */
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

	/**
	 * Gets the Stachelhaus signature code.
	 * @return
	 */
	public String getSigstach()
	{
		return sigstach;
	}

	/**
	 * Sets the Stachelhaus signature code.
	 * @param sigstach
	 */
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
	
	/**
	 * Reads an ADomain from file.
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static ADomain read(String filename) throws Exception 
	{
        XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)));
        ADomain o = (ADomain)decoder.readObject();
        decoder.close();
        return o;
    }

	/**
	 * Gets whether the ADomain is deemed to be an outlier.
	 * @return
	 */
	public boolean isOutlier()
	{
		return outlier;
	}

	/**
	 * Sets whether the ADomain is deemed to be an outlier.
	 * @param outlier
	 */
	public void setOutlier(boolean outlier)
	{
		this.outlier = outlier;
	}
	
	/**
	 * Gets the end position of the extracted ADomain.
	 * @return
	 */
	public int getStartPos()
	{
		return startPos;
	}

	/**
	 * Sets the start position of the extracted ADomain.
	 * @param startPos
	 */
	public void setStartPos(int startPos)
	{
		this.startPos = startPos;
	}

	/**
	 * Gets the end position of the extracted ADomain.
	 * @return
	 */
	public int getEndPos()
	{
		return endPos;
	}

	/**
	 * Sets the end position of the extracted ADomain.
	 * @param endPos
	 */
	public void setEndPos(int endPos)
	{
		this.endPos = endPos;
	}

	/**
	 * Sets the score of the PFAM HMMER ADomain extraction step.
	 * @return score
	 */
	public double getPfamscore()
	{
		return pfamscore;
	}

	/**
	 * Sets the score of the PFAM HMMER ADomain extraction step.
	 * @param pfamscore
	 */
	public void setPfamscore(double pfamscore)
	{
		this.pfamscore = pfamscore;
	}
}