package io.github.victorhugonf.boletoapi.ejb.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import io.github.victorhugonf.boletoapi.ejb.dao.BoletoDao;
import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.entity.StatusEnum;
import io.github.victorhugonf.boletoapi.ejb.useful.Factory;
import junit.framework.AssertionFailedError;

public class BoletoServiceTest extends EasyMockSupport{
	
	@TestSubject
	private BoletoService boletoService = new BoletoService();
	
	@Mock
	private BoletoDao boletoDaoMock;
	
	@Rule
	public EasyMockRule rule = new EasyMockRule(this);
	
	@Test
	public void processarStatus_statusPago_naoProcessar() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setStatus(StatusEnum.PAID);
		
		EasyMock.expect(boletoDaoMock.merge(boleto)).andThrow(new AssertionFailedError()).anyTimes();
		replayAll();
		
		boolean processado = boletoService.processarStatus(boleto, StatusEnum.PAID);
		
		Assert.assertFalse(processado);
		verifyAll();
	}
	
	@Test
	public void processarStatus_statusCancelado_naoProcessar() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setStatus(StatusEnum.CANCELED);
		
		EasyMock.expect(boletoDaoMock.merge(boleto)).andThrow(new AssertionFailedError()).anyTimes();
		replayAll();
		
		boolean processado = boletoService.processarStatus(boleto, StatusEnum.PAID);
		
		Assert.assertFalse(processado);
		verifyAll();
	}
	
	@Test
	public void processarStatus_statusPendente_processarPagamento() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setStatus(StatusEnum.PENDING);

		EasyMock.expect(boletoDaoMock.merge(boleto)).andReturn(boleto);
		replayAll();
		
		boolean processado = boletoService.processarStatus(boleto, StatusEnum.PAID);
		
		Assert.assertTrue(processado);
		verifyAll();
	}
	
	@Test
	public void processarStatus_statusPendente_processarCancelamento() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setStatus(StatusEnum.PENDING);
		
		EasyMock.expect(boletoDaoMock.merge(boleto)).andReturn(boleto);
		replayAll();
		
		boolean processado = boletoService.processarStatus(boleto, StatusEnum.CANCELED);
		
		Assert.assertTrue(processado);
		verifyAll();
	}
	
	@Test
	public void getById_uuidInexistente_retornarNulo() throws Exception{
		UUID uuid = Factory.createUuid();
		
		EasyMock.expect(boletoDaoMock.getById(uuid)).andReturn(null);
		replayAll();
		
		Boleto boletoRetornado = boletoService.getById(uuid);
		
		Assert.assertNull(boletoRetornado);
		verifyAll();
	}
	
	@Test
	public void getById_statusPago_retornarSemMulta() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setStatus(StatusEnum.PAID);
		UUID uuid = Factory.createUuid();
		
		EasyMock.expect(boletoDaoMock.getById(uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.getById(uuid);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertNull(boletoRetornado.getMulta());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoRetornado.getValorTotalEmCentavos());
		Assert.assertEquals(boleto.getStatus(), boletoRetornado.getStatus());
		verifyAll();
	}
	
	@Test
	public void getById_statusCancelado_retornarSemMulta() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setStatus(StatusEnum.CANCELED);
		UUID uuid = Factory.createUuid();
		
		EasyMock.expect(boletoDaoMock.getById(uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.getById(uuid);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertNull(boletoRetornado.getMulta());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoRetornado.getValorTotalEmCentavos());
		Assert.assertEquals(boleto.getStatus(), boletoRetornado.getStatus());
		verifyAll();
	}
	
	@Test
	public void getById_statusPendenteEVencimentoAmanha_retornarSemMulta() throws Exception{
		Boleto boleto = Factory.createBoletoFake(Factory.createUuid(), StatusEnum.PENDING,  
							Date.from(Factory.createInstantNow().plus(1, ChronoUnit.DAYS)),
							"Jose", BigDecimal.valueOf(1000));
		UUID uuid = Factory.createUuid();
		
		EasyMock.expect(boletoDaoMock.getById(uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.getById(uuid);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(BigDecimal.ZERO, boletoRetornado.getMulta());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoRetornado.getValorTotalEmCentavos());
		Assert.assertEquals(boleto.getStatus(), boletoRetornado.getStatus());
		verifyAll();
	}
	
	@Test
	public void getById_statusPendenteEVencimentoHoje_retornarSemMulta() throws Exception{
		Boleto boleto = Factory.createBoletoFake(Factory.createUuid(), StatusEnum.PENDING,  
						Factory.createDateNow(),
						"Jose", BigDecimal.valueOf(1000));
		UUID uuid = Factory.createUuid();
		
		EasyMock.expect(boletoDaoMock.getById(uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.getById(uuid);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(BigDecimal.ZERO, boletoRetornado.getMulta());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoRetornado.getValorTotalEmCentavos());
		Assert.assertEquals(boleto.getStatus(), boletoRetornado.getStatus());
		verifyAll();
	}
	
	@Test
	public void getById_statusPendenteEVencimentoOntem_retornarComMulta() throws Exception{
		Boleto boleto = Factory.createBoletoFake(Factory.createUuid(), StatusEnum.PENDING, 
							Date.from(Factory.createInstantNow().minus(1, ChronoUnit.DAYS)),
							"Jose", BigDecimal.valueOf(1000));
		UUID uuid = Factory.createUuid();
		
		EasyMock.expect(boletoDaoMock.getById(uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.getById(uuid);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(BigDecimal.valueOf(5), boletoRetornado.getMulta());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoRetornado.getValorTotalEmCentavos());
		Assert.assertEquals(boleto.getStatus(), boletoRetornado.getStatus());
		verifyAll();
	}
	
	@Test
	public void getById_statusPendenteEVencimentoHa10Dias_retornarComMulta() throws Exception{
		Boleto boleto = Factory.createBoletoFake(Factory.createUuid(), StatusEnum.PENDING, 
							Date.from(Factory.createInstantNow().minus(10, ChronoUnit.DAYS)),
							"Jose", BigDecimal.valueOf(1000));
		UUID uuid = Factory.createUuid();
		
		EasyMock.expect(boletoDaoMock.getById(uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.getById(uuid);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(BigDecimal.valueOf(5), boletoRetornado.getMulta());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoRetornado.getValorTotalEmCentavos());
		Assert.assertEquals(boleto.getStatus(), boletoRetornado.getStatus());
		verifyAll();
	}

	@Test
	public void getById_statusPendenteEVencimentoHa11Dias_retornarComMulta() throws Exception{
		Boleto boleto = Factory.createBoletoFake(Factory.createUuid(), StatusEnum.PENDING, 
							Date.from(Factory.createInstantNow().minus(11, ChronoUnit.DAYS)),
							"Jose", BigDecimal.valueOf(1000));
		UUID uuid = Factory.createUuid();
		
		EasyMock.expect(boletoDaoMock.getById(uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.getById(uuid);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(BigDecimal.TEN, boletoRetornado.getMulta());
		Assert.assertEquals(boleto.getValorTotalEmCentavos(), boletoRetornado.getValorTotalEmCentavos());
		Assert.assertEquals(boleto.getStatus(), boletoRetornado.getStatus());
		verifyAll();
	}
	
	@Test
	public void persist_persistido_retornarObjeto() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();

		EasyMock.expect(boletoDaoMock.persist(boleto)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.persist(boleto);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(boleto, boletoRetornado);
		verifyAll();
	}
	
	@Test
	public void merge_persistido_retornarObjeto() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();

		EasyMock.expect(boletoDaoMock.merge(boleto)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.merge(boleto);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(boleto, boletoRetornado);
		verifyAll();
	}
	
	@Test
	public void remove_removido() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();

		boletoDaoMock.remove(boleto);
		EasyMock.expectLastCall();
		replayAll();
		
		boletoService.remove(boleto);
		
		verifyAll();
	}
	
	@Test
	public void removeById_removido() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();

		EasyMock.expect(boletoDaoMock.getById(boleto.getId())).andReturn(boleto);
		boletoDaoMock.remove(boleto);
		EasyMock.expectLastCall();
		replayAll();
		
		boletoService.remove(boleto.getId());
		
		verifyAll();
	}
	
	@Test
	public void getAll_retornarLista() throws Exception{
		List<Boleto> boletos = new ArrayList<>();
		boletos.add(Factory.createBoletoPendenteFake());

		EasyMock.expect(boletoDaoMock.getAll()).andReturn(boletos);
		replayAll();
		
		List<Boleto> boletosRetornados = boletoService.getAll();
		
		Assert.assertNotNull(boletosRetornados);
		Assert.assertEquals(boletos.size(), boletosRetornados.size());
		
		verifyAll();
	}
	
	@Test
	public void get_retornarObjeto() throws Exception{
		Boleto boleto = Factory.createBoletoPendenteFake();

		EasyMock.expect(boletoDaoMock.get(boleto)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.get(boleto);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(boleto, boletoRetornado);
		
		verifyAll();
	}
    
}