package io.github.victorhugonf.boletoapi.ejb.service;

import java.math.BigDecimal;
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

public class BoletoServiceTest extends EasyMockSupport{
	
	@Rule
    public EasyMockRule rule = new EasyMockRule(this);
	
	@TestSubject
	private BoletoService boletoService = new BoletoService();
	
	@Mock
	private BoletoDao boletoDaoMock;
	
	@Test
	public void naoProcessarBoletoPago() throws Exception{
		Boleto boleto = Factory.boleto();
		boleto.setStatus(StatusEnum.PAID);
		
		Assert.assertFalse(boletoService.processarStatus(boleto, StatusEnum.PAID));
	}
	
	@Test
	public void naoProcessarBoletoBoletoCancelado() throws Exception{
		Boleto boleto = Factory.boleto();
		boleto.setStatus(StatusEnum.CANCELED);
		
		Assert.assertFalse(boletoService.processarStatus(boleto, StatusEnum.PAID));
	}
	
	@Test
	public void processarPagamentoBoleto() throws Exception{
		Boleto boleto = Factory.boleto();
		boleto.setStatus(StatusEnum.PENDING);
		
		Assert.assertTrue(boletoService.processarStatus(boleto, StatusEnum.PAID));
	}
	
	@Test
	public void processarCancelamentoBoleto() throws Exception{
		Boleto boleto = Factory.boleto();
		boleto.setStatus(StatusEnum.PENDING);
		
		Assert.assertTrue(boletoService.processarStatus(boleto, StatusEnum.CANCELED));
	}
	
	@Test
	public void retornarBoletoPago() throws Exception{
		Boleto boleto = Factory.boleto(StatusEnum.PAID, BigDecimal.TEN);
		UUID uuid = Factory.uuid();
		
		EasyMock.expect(boletoDaoMock.get(uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoService.get(uuid);
		
		Assert.assertEquals(null, boletoRetornado.getMulta());
		Assert.assertEquals(boleto.getValor(), boletoRetornado.getValor());
		verifyAll();
	}
	
//	@Override
//	public Boleto get(UUID id) throws Exception {
//		Boleto boleto = super.get(id);
//		
//		if(StatusEnum.PENDING.equals(boleto.getStatus())){
//			boleto.setMulta(calcularMulta(boleto));
//		}
//		
//		return boleto;
//	}
//
//	private BigDecimal calcularMulta(Boleto boleto) {
//		Instant hoje = Instant.now().truncatedTo(ChronoUnit.DAYS);
//		Instant vencimento = boleto.getDataVencimento().toInstant();
//		
//		if(hoje.isAfter(vencimento)){
//			return BigDecimal.ZERO;
//		}
//
//		BigDecimal multa = BigDecimal.valueOf(1.01);
//		
//		if(hoje.plus(10, ChronoUnit.DAYS).isAfter(vencimento)){
//			multa = BigDecimal.valueOf(1.005);
//		}
//		
//		return boleto.getValor().multiply(multa);
//	}
    
}