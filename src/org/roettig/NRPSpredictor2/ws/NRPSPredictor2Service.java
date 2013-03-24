package org.roettig.NRPSpredictor2.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlElement;

@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface NRPSPredictor2Service
{
	@WebMethod
	NRPSPredictorResponse predict(@XmlElement(name="request",required=true,namespace="http://ws.NRPSpredictor2.roettig.org/") NRPSPredictorRequest request);
}