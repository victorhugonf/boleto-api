package io.github.victorhugonf.boletoapi.rest.useful;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public class RestClient {
	
	private ResteasyClient client;
	private String host;
	private Integer port;
	
	private RestClient(String host, Integer port){
		client = new ResteasyClientBuilder()
						.connectionPoolSize(99)
						.connectionCheckoutTimeout(100, TimeUnit.MILLISECONDS)
						.build();
		this.host = host;
		this.port = port;
	}
	
	public static RestClient create(String host, Integer port){
		return new RestClient(host, port);
	}

	private String url(String path){
		return String.format("http://%s:%s/%s", host, port, path);
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