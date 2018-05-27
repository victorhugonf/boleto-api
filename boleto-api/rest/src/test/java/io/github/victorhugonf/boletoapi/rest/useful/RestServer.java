package io.github.victorhugonf.boletoapi.rest.useful;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;

public class RestServer {
	
	private static final String HOST = "localhost";
	private static final Integer PORT = 5555;
	
	private RestClient client;
	private TJWSEmbeddedJaxrsServer server;
	
	private RestServer(Object resource){
		server = new TJWSEmbeddedJaxrsServer();
		server.setBindAddress(HOST);
		server.setPort(PORT);
		server.getDeployment().getResources().add(resource);
		server.start();
		
		client = RestClient.create(HOST, PORT);
	}
	
	public static RestServer create(Object resource){
		return new RestServer(resource);
	}

	public void stop() {
		server.stop();		
	}
	
	public Response get(String path){
		return client.get(path);
	}
	
	public Response post(String path, Object object){
		return client.post(path, object);
	}
	
	public Response put(String path, Object object){
		return client.put(path, object);
	}
	
	public Response delete(String path){
		return client.delete(path);
	}

}