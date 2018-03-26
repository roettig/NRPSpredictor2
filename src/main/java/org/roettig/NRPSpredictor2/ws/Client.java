package org.roettig.NRPSpredictor2.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.roettig.NRPSpredictor2.ws.Sequence.Kingdom;
import org.roettig.NRPSpredictor2.ws.Sequence.SequenceType;


public class Client
{

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException
	{
		URL url = new URL("http://localhost:9999/ws/nrpspredictor2service?wsdl");
		 
        //1st argument service URI, refer to wsdl document above
		//2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ws.NRPSpredictor2.roettig.org/", "NRPSPredictor2Service");
 
        Service service = Service.create(url, qname);
 
        NRPSPredictor2Service svc      = service.getPort(NRPSPredictor2Service.class);
        
        NRPSPredictorRequest req = new NRPSPredictorRequest();
        req.addSequence(new Sequence("1","VETSFDGFTFDGFVLFGGEIHVYGPTETTVFATF",Kingdom.Bacterial,SequenceType.Signature8A));
        req.addSequence(new Sequence("2","RWATFDLSVWELHFLCSGEHNLYGPTEAAIDVTA",Kingdom.Bacterial,SequenceType.Signature8A));
        
        NRPSPredictorResponse response = svc.predict(req);
        
        for(ADomainPrediction pred: response.getPredictions())
        {
        	System.out.println(pred);
        }
	}

}
