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

@WebService(endpointInterface = "org.roettig.NRPSpredictor2.ws.NRPSPredictor2Service",serviceName="NRPSPredictor2Service")
public class NRPSPredictor2ServiceImpl implements NRPSPredictor2Service
{
	@Resource 
	WebServiceContext context;
	
	public NRPSPredictorResponse predict(NRPSPredictorRequest request)
	{
		NRPSPredictorResponse response = new NRPSPredictorResponse();
		
		List<ADomain> adoms = new ArrayList<ADomain>();
		
		List<Sequence> seqs = request.getSequences();
		
		System.out.println("predict: size = "+seqs.size());
		
		for(Sequence seq: seqs)
		{
			ADomain ad = new ADomain();
			
			if(SequenceType.FullSequence==seq.getSequenceType())
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
			else if(SequenceType.Signature8A==seq.getSequenceType())
			{
				ad.setSig8a(seq.getSequence());
				ad.setSid(seq.getId());
				adoms.add( ad );
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
