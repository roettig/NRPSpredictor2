package org.roettig.NRPSpredictor2.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.roettig.NRPSpredictor2.ADomain;
import org.roettig.NRPSpredictor2.Detection;
import org.roettig.NRPSpredictor2.predictors.BacterialNRPSPredictor2;

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
			ad.setSig8a(seq.getSequence());
			ad.setSid(seq.getId());
			adoms.add( ad );
		}
		
		BacterialNRPSPredictor2 pred = new BacterialNRPSPredictor2();
		pred.predict(adoms);
		
		for(ADomain ad:adoms)
		{
			Detection de = ad.getBestDetection(ADomain.NRPS2_SINGLE_CLUSTER);
			response.addPrediction(new ADomainPrediction(ad.sig8a,de.getLabel(),(float)de.getScore()));
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
