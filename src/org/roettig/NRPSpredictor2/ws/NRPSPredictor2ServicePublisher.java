package org.roettig.NRPSpredictor2.ws;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;

public class NRPSPredictor2ServicePublisher
{
	public static void main(String[] args)
	{
		if(args.length!=1)
			System.err.println("Please supply an IP address to bind to.");
		System.out.println("host:"+args[0]);
		String svcurl = String.format("http://%s:9999/ws/nrpspredictor2service",args[0]);
		System.out.println("starting NRPSpredictor2 web service at "+svcurl);
		ExecutorService executor = Executors.newCachedThreadPool();
		Endpoint endpoint = Endpoint.publish(svcurl, new NRPSPredictor2ServiceImpl());
		endpoint.setExecutor(executor);
		executor.shutdown();
	}
}