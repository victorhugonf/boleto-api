package io.github.victorhugonf.boletoapi.ejb.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.entity.StatusEnum;

public class Factory {
	
	private Factory(){
		
	}
	
	public static Boleto createBoletoPendenteFake(){
		return createBoletoFake(Factory.createUuid(),
							StatusEnum.PENDING,
							Factory.createDateNow(),
							"Jose",
							BigDecimal.valueOf(100000));
	}
	
	public static Boleto createBoletoFake(UUID uuid, StatusEnum status, Date dataVencimento, String nomeCliente, BigDecimal valor){
		Boleto boleto = new Boleto();
		boleto.setId(uuid);
		boleto.setStatus(status);
		boleto.setDataVencimento(dataVencimento);
		boleto.setNomeCliente(nomeCliente);
		boleto.setValorTotalEmCentavos(valor);
		return boleto;
	}
	
	public static UUID createUuid(){
		return UUID.fromString("f382f9cf-e8e3-4571-8053-c0374ffacd57");
	}
	
	public static Date createDateNow(){
		return Date.from(createInstantNow());
	}
	
	public static Instant createInstantNow(){
		return LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
	}
    
}