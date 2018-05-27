package io.github.victorhugonf.boletoapi.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import io.github.victorhugonf.boletoapi.dto.StatusDto;
import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.entity.StatusEnum;
import io.github.victorhugonf.boletoapi.ejb.service.BoletoService;
import io.github.victorhugonf.boletoapi.ejb.useful.Factory;
import io.github.victorhugonf.boletoapi.rest.utils.RestServer;
import junit.framework.AssertionFailedError;

public class BoletoEndPointTest extends EasyMockSupport{

	private static final String LOCATION = "Location";
	private static final String BANKSLIPS = "bankslips";
	private static final String BANKSLIPS_UUID = "bankslips/" + Factory.createUuid().toString();

	private static RestServer server;
	
	@TestSubject
	private static BoletoEndPoint boletoEndPoint = new BoletoEndPoint();

	@Mock
	private BoletoService boletoServiceMock;

	@Rule
	public EasyMockRule rule = new EasyMockRule(this);
	
	@BeforeClass
	public static void setUpClass(){
		server = RestServer.create(boletoEndPoint);		
	}
	
	@AfterClass
	public static void tearDownClass(){
		server.stop();
	}
	
	@Test
	public void getAll_listagemVazia_noContent() throws Exception{		
		EasyMock.expect(boletoServiceMock.getAll()).andReturn(new ArrayList<>());
		replayAll();
		
		Response response = server.get(BANKSLIPS);
		
		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		verifyAll();
	}
	
	@Test
	public void getAll_listagemNula_noContent() throws Exception{		
		EasyMock.expect(boletoServiceMock.getAll()).andReturn(null);
		replayAll();
		
		Response response = server.get(BANKSLIPS);
		
		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		verifyAll();
	}
	
	@Test
	public void getAll_listagemPreenchida_ok() throws Exception{
		List<Boleto> boletos = new ArrayList<>();
		boletos.add(Factory.createBoletoPendenteFake());
		
		EasyMock.expect(boletoServiceMock.getAll()).andReturn(boletos);
		replayAll();
		
		Response response = server.get(BANKSLIPS);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		verifyAll();
	}
	
	@Test
	public void get_registroNaoEncontrado_notFound() throws Exception{		
		EasyMock.expect(boletoServiceMock.getById(Factory.createUuid())).andReturn(null);
		replayAll();
		
		Response response = server.get(BANKSLIPS_UUID);
		
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		verifyAll();
	}
	
	@Test
	public void get_registroEncontrado_ok() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();
		
		EasyMock.expect(boletoServiceMock.getById(Factory.createUuid())).andReturn(boleto);
		replayAll();
		
		Response response = server.get(BANKSLIPS_UUID);

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(boleto, response.readEntity(Boleto.class));
		verifyAll();
	}
	
	@Test
	public void post_semPayload_badRequest() throws Exception{
		EasyMock.expect(boletoServiceMock.persist(null)).andThrow(new AssertionFailedError()).anyTimes();
		replayAll();

		Response response = server.post(BANKSLIPS, null);

		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		verifyAll();
	}
	
	@Test
	public void post_persistido_created() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();
		
		EasyMock.expect(boletoServiceMock.persist(boleto)).andReturn(boleto);
		replayAll();
		
		Response response = server.post(BANKSLIPS, boleto);
		
		Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.getHeaderString(LOCATION).contains(BANKSLIPS_UUID));
		Assert.assertFalse(response.hasEntity());
		verifyAll();
	}
	
	@Test
	public void put_semPayload_badRequest() throws Exception{
		Response response = server.put(BANKSLIPS_UUID, null);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
	}
	
	@Test
	public void put_statusNulo_badRequest() throws Exception{
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(null);
		
		Response response = server.put(BANKSLIPS_UUID, statusDto);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
	}
	
	@Test
	public void put_registroInexistente_notFound() throws Exception{
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(StatusEnum.PAID);
		
		EasyMock.expect(boletoServiceMock.getById(Factory.createUuid())).andReturn(null);
		EasyMock.expect(boletoServiceMock.processarStatus(null, statusDto.getStatus())).andThrow(new AssertionFailedError()).anyTimes();
		replayAll();
		
		Response response = server.put(BANKSLIPS_UUID, statusDto);
		
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		verifyAll();
	}
	
	@Test
	public void put_statusNaoPermitido_methodNotAllowed() throws Exception{
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(StatusEnum.PAID);
		Boleto boleto = Factory.createBoletoPendenteFake();
		
		EasyMock.expect(boletoServiceMock.getById(Factory.createUuid())).andReturn(boleto);
		EasyMock.expect(boletoServiceMock.processarStatus(boleto, statusDto.getStatus())).andReturn(false);
		replayAll();
		
		Response response = server.put(BANKSLIPS_UUID, statusDto);
		
		Assert.assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		verifyAll();
	}
	
	@Test
	public void put_statusPermitido_ok() throws Exception{
		StatusDto statusDto = new StatusDto();
		statusDto.setStatus(StatusEnum.PAID);
		Boleto boleto = Factory.createBoletoPendenteFake();
		
		EasyMock.expect(boletoServiceMock.getById(Factory.createUuid())).andReturn(boleto);
		EasyMock.expect(boletoServiceMock.processarStatus(boleto, statusDto.getStatus())).andReturn(true);
		replayAll();
		
		Response response = server.put(BANKSLIPS_UUID, statusDto);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertFalse(response.hasEntity());
		verifyAll();
	}

}