package io.github.victorhugonf.boletoapi.rest;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.victorhugonf.boletoapi.rest.utils.RestServer;

public class PingEndPointTest {
	
	private static PingEndPoint pingEndPoint = new PingEndPoint();
	private static RestServer server;
	
	@BeforeClass
	public static void iniciar(){
		server = RestServer.create(pingEndPoint);		
	}
	
	@AfterClass
	public static void finalizar(){
		server.stop();
	}
	
	@Test
	public void pingSucesso(){		
		Response response = server.get("ping");
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}

}