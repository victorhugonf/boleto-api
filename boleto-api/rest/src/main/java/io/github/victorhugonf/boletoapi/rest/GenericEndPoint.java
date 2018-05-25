package io.github.victorhugonf.boletoapi.rest;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import io.github.victorhugonf.boletoapi.ejb.entity.EntityIdentifiable;
import io.github.victorhugonf.boletoapi.ejb.service.Service;

@Consumes(value = MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
public abstract class GenericEndPoint<E extends EntityIdentifiable, S extends Service<E>> {

	protected abstract S service();
	protected abstract Class<E> getClazz();
	
	private Response response(ResponseBuilder response){
		return response.build();
	}
	
	private Response responseStatus(Status status){
		return response(Response.status(status));
	}

	protected Response responseOk() {
		return response(Response.ok());
	}

	protected Response responseOk(Object object) {
		return response(Response.ok(object));
	}
	
	protected Response responseCreated(EntityIdentifiable object, UriInfo uriInfo) {
		return object == null
				? responseNoContent()
				: responseCreated(object.getId().toString(), uriInfo);
	}
	
	private Response responseCreated(String path, UriInfo uriInfo) {
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(path);
		return response(Response.created(builder.build()));
	}

	protected Response responseNoContent() {
		return response(Response.noContent());
	}

	protected Response responseBadRequest(){
		return responseStatus(Status.BAD_REQUEST);
	}
	
	protected Response responseNotFound() {
		return responseStatus(Status.NOT_FOUND);
	}
	
	protected Response responseMethodNotAllowed(){
		return responseStatus(Status.METHOD_NOT_ALLOWED);
	}

	@GET
	public Response getAll() throws Exception{
		List<E> list = service().getAll();

		return (list == null || list.isEmpty())
				? responseNoContent()
				: responseOk(list);
	}

	@GET
	@Path("{id}")
	public Response get(@PathParam("id") UUID id) throws Exception{
		E object = service().getById(id);

		return object == null
				? responseNotFound()
				: responseOk(object);
	}

}