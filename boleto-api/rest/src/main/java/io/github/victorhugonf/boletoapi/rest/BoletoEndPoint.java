package io.github.victorhugonf.boletoapi.rest;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.github.victorhugonf.boletoapi.dto.StatusDto;
import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.service.BoletoService;

@Path("bankslips")
public class BoletoEndPoint extends GenericEndPoint<Boleto, BoletoService> {

	@Inject
	private BoletoService boletoService;

	@Override
	protected BoletoService service() {
		return boletoService;
	}

	@Override
	protected Class<Boleto> getClazz() {
		return Boleto.class;
	}
	
	@POST
	public Response post(Boleto boleto, @Context UriInfo uriInfo) throws Exception{
		if(boleto == null){
			return responseBadRequest();
		}
		
		Boleto boletoPersistido = service().persist(boleto);
		return responseCreated(boletoPersistido, uriInfo);
	}
	
	@PUT
	@Path("{id}")
	public Response put(@PathParam("id") UUID id, StatusDto status) throws Exception{
		if(status == null || status.getStatus() == null){
			return responseBadRequest();
		}
		
		return processarStatus(id, status);
	}

	private Response processarStatus(UUID id, StatusDto status) throws Exception {
		Boleto boleto = service().getById(id);
		
		if(boleto == null){
			throw new NotFoundException();
		}

		return service().processarStatus(boleto, status.getStatus())
				? responseOk()
				: responseMethodNotAllowed();
	}

}