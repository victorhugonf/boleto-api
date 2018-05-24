package io.github.victorhugonf.boletoapi.rest.utils;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;

public class RestServer {
	
	private static final String HOST = "localhost";
	private static final Integer PORT = 5555;
	
	private ResteasyClient client;
	private TJWSEmbeddedJaxrsServer server;
	
	private RestServer(Object resource){
		server = new TJWSEmbeddedJaxrsServer();
		server.setBindAddress(HOST);
		server.setPort(PORT);
		server.getDeployment().getResources().add(resource);
		server.start();
		
		client = new ResteasyClientBuilder()
						.connectionPoolSize(99)
						.connectionCheckoutTimeout(100, TimeUnit.MILLISECONDS)
						.build();
	}
	
	public static RestServer create(Object resource){
		return new RestServer(resource);
	}

	public void stop() {
		server.stop();		
	}
	
	private String url(String path){
		return String.format("http://%s:%s/%s", HOST, PORT, path);
	}

	private Builder request(String path) {
		return client.target(url(path)).request();
	}
	
	public Response get(String path){
		return request(path).buildGet().invoke();
	}
	
	public Response post(String path, Object object){
		return request(path).buildPost(Entity.json(object)).invoke();
	}
	
	public Response put(String path, Object object){
		return request(path).buildPut(Entity.json(object)).invoke();
	}
	
	public Response delete(String path){
		return request(path).buildDelete().invoke();
	}

}