package io.github.victorhugonf.boletoapi.rest;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.github.victorhugonf.boletoapi.dto.StatusDto;
import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.entity.StatusEnum;
import io.github.victorhugonf.boletoapi.ejb.useful.Factory;
import io.github.victorhugonf.boletoapi.rest.useful.RestClient;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BoletoIntegrationTesting {

	private static final String LOCATION = "Location";
	private static final String REST_BANKSLIPS = "rest/bankslips/";
	private static final String URL = "http://localhost:8080/rest/bankslips/";
	
	private static String idBoleto1;
	private static String idBoleto2;

	private static RestClient client;
	
	@BeforeClass
	public static void setUpClass(){
		client = RestClient.create("localhost", 8080);		
	}
	
	@Test
	public void it01_getAll_listagemVazia_noContent(){
		Response response = client.get(REST_BANKSLIPS);
		
		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
	}
	
	@Test
	public void it02_get_registroNaoEncontrado_notFound(){		
		Response response = client.get(getPath(Factory.createUuid().toString()));
		
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
	}
	
	@Test
	public void it03_post_semPayload_badRequest(){
		Response response = client.post(REST_BANKSLIPS, null);

		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void it04_post_persistido_created(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setId(null);
		
		Response response = client.post(REST_BANKSLIPS, boleto);
		String urlBoleto = response.getHeaderString(LOCATION);
		
		Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		Assert.assertNotNull(urlBoleto);
		Assert.assertTrue(urlBoleto.contains(URL));
		Assert.assertFalse(response.hasEntity());
		
		idBoleto1 = urlBoleto.replace(URL, "");
		
		response = client.get(getPath(idBoleto1));
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		
		Boleto boletoPersistido = response.readEntity(Boleto.class);
		
		Assert.assertEquals(boleto.getNomeCliente(), boletoPersistido.getNomeCliente());
		Assert.assertEquals(boleto.getDataVencimento(), boletoPersistido.getDataVencimento());
		Assert.assertEquals(boleto.getStatus(), boletoPersistido.getStatus());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoPersistido.getValorTotalEmCentavos());
	}
	
	@Test
	public void it05_getAll_listagemPreenchida_ok(){
		Response response = client.get(REST_BANKSLIPS);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
	}
	
	@Test
	public void it06_get_registroEncontrado_ok(){
		Response response = client.get(getPath(idBoleto1));

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		
		Boleto boleto = response.readEntity(Boleto.class);
		
		Assert.assertEquals(idBoleto1, boleto.getId().toString());
	}
	
	@Test
	public void it07_post_persistido_created(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setId(null);
		
		Response response = client.post(REST_BANKSLIPS, boleto);
		String urlBoleto = response.getHeaderString(LOCATION);
		
		Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		Assert.assertNotNull(urlBoleto);
		Assert.assertTrue(urlBoleto.contains(URL));
		Assert.assertFalse(response.hasEntity());
		
		idBoleto2 = urlBoleto.replace(URL, "");
		
		response = client.get(getPath(idBoleto2));
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		
		Boleto boletoPersistido = response.readEntity(Boleto.class);
		
		Assert.assertEquals(boleto.getNomeCliente(), boletoPersistido.getNomeCliente());
		Assert.assertEquals(boleto.getDataVencimento(), boletoPersistido.getDataVencimento());
		Assert.assertEquals(boleto.getStatus(), boletoPersistido.getStatus());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoPersistido.getValorTotalEmCentavos());
	}
	
	@Test
	public void it08_getAll_listagemPreenchida_ok(){
		Response response = client.get(REST_BANKSLIPS);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
	}
	
	@Test
	public void it09_get_registroEncontrado_ok(){
		Response response = client.get(getPath(idBoleto2));

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		
		Boleto boleto = response.readEntity(Boleto.class);
		
		Assert.assertEquals(idBoleto2, boleto.getId().toString());
	}
	
	@Test
	public void it_10_put_semPayload_badRequest(){
		Response response = client.put(getPath(idBoleto1), null);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
	}
	
	@Test
	public void it_11_put_statusNulo_badRequest(){
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(null);
		
		Response response = client.put(getPath(idBoleto1), statusDto);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
	}
	
	@Test
	public void it_12_put_registroInexistente_notFound(){
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(StatusEnum.PAID);
		
		Response response = client.put(getPath(Factory.createUuid().toString()), statusDto);
		
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
	}
	
	@Test
	public void it13_put_statusPermitido_ok(){
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(StatusEnum.PAID);

		Response response = client.put(getPath(idBoleto1), statusDto);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		
		response = client.get(getPath(idBoleto1));
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		
		Boleto boleto = response.readEntity(Boleto.class);
		
		Assert.assertEquals(StatusEnum.PAID, boleto.getStatus());
	}
	
	@Test
	public void it14_put_statusPermitido_ok(){
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(StatusEnum.CANCELED);

		Response response = client.put(getPath(idBoleto2), statusDto);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		
		response = client.get(getPath(idBoleto2));
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		
		Boleto boleto = response.readEntity(Boleto.class);
		
		Assert.assertEquals(StatusEnum.CANCELED, boleto.getStatus());
	}
	
	@Test
	public void it15_put_statusNaoPermitido_methodNotAllowed(){
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(StatusEnum.PAID);

		Response response = client.put(getPath(idBoleto1), statusDto);
		
		Assert.assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		
		response = client.get(getPath(idBoleto1));
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		
		Boleto boleto = response.readEntity(Boleto.class);
		
		Assert.assertEquals(StatusEnum.PAID, boleto.getStatus());
	}
	
	@Test
	public void it16_put_statusNaoPermitido_methodNotAllowed(){
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(StatusEnum.PAID);

		Response response = client.put(getPath(idBoleto2), statusDto);
		
		Assert.assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		
		response = client.get(getPath(idBoleto2));
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		
		Boleto boleto = response.readEntity(Boleto.class);
		
		Assert.assertEquals(StatusEnum.CANCELED, boleto.getStatus());
	}

	private String getPath(String id){
		return String.format("%s%s", REST_BANKSLIPS, id);
	}

}