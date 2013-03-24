package org.roettig.NRPSpredictor2.ws;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;

public class NRPSPredictor2ServicePublisher
{
	public static void main(String[] args)
	{
		String svcurl = "http://localhost:9999/ws/nrpspredictor2service";
		System.out.println("starting NRPSpredictor2 web service at "+svcurl);
		ExecutorService executor = Executors.newCachedThreadPool();
		Endpoint endpoint = Endpoint.publish(svcurl, new NRPSPredictor2ServiceImpl());
		endpoint.setExecutor(executor);
		executor.shutdown();
	}
}