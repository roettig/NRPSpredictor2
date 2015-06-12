package org.roettig.NRPSpredictor2.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.roettig.NRPSpredictor2.extraction.ADomain;
import org.roettig.NRPSpredictor2.predictors.BacterialNRPSPredictor2;
import org.roettig.NRPSpredictor2.predictors.Detection;
import org.roettig.NRPSpredictor2.util.Helper;
import org.roettig.NRPSpredictor2.ws.Sequence.SequenceType;

import com.sun.istack.internal.logging.Logger;

@WebService(endpointInterface = "org.roettig.NRPSpredictor2.ws.NRPSPredictor2Service",serviceName="NRPSPredictor2Service")
public class NRPSPredictor2ServiceImpl implements NRPSPredictor2Service
{
	private static final Logger logger = Logger.getLogger(NRPSPredictor2ServiceImpl.class);
	
	@Resource 
	WebServiceContext context;
	
	public NRPSPredictorResponse predict(NRPSPredictorRequest request)
	{
		NRPSPredictorResponse response = new NRPSPredictorResponse();
		
		List<ADomain> adoms = new ArrayList<ADomain>();
		
		List<Sequence> seqs = request.getSequences();
		
		logger.info("predict: size = "+seqs.size());
		
		for(Sequence seq: seqs)
		{
			ADomain ad = new ADomain();
			
			if(SequenceType.FullSequence.equals(seq.getSequenceType()))
			{
				List<ADomain> adoms_ = Helper.extractADomainsFromFullSequence(seq.getId(),seq.getSequence());
				int idx = 1;
				for(ADomain ad_:adoms_)
				{
					ad_.setSid(String.format("%s_%d",ad_.getSid(),idx));
					adoms.add(ad_);
					idx++;
				}
			}
			else if(SequenceType.Signature8A.equals(seq.getSequenceType()))
			{
				ad.setSig8a(seq.getSequence());
				ad.setSid(seq.getId());
				adoms.add( ad );
			}
			else if(SequenceType.SignatureStachelhaus.equals(seq.getSequenceType()))
			{
				// ToDo
			}
		}
		
		BacterialNRPSPredictor2 pred = new BacterialNRPSPredictor2();
		pred.predict(adoms);
		
		for(ADomain ad:adoms)
		{
			for(Detection det: ad.getDetections())
			{
				ADomainPrediction adm = new ADomainPrediction(ad.getSid(),ad.sig8a,det.getLabel(),(float)det.getScore(),ad.outlier);
				adm.setStachelhausCode(ad.sigstach);
				response.addPrediction(adm);
				logger.info(adm.toString());
			}
			
		}
				
		return response;
	}

	public static void dumpMap(Map<?,?> map)
	{
		for(Object key: map.keySet())
		{
			System.out.println(key+" -> "+map.get(key));
		}
	}
}
