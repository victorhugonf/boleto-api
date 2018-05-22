package io.github.victorhugonf.boletoapi.tools;

import java.math.BigDecimal;
import java.util.UUID;

import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.entity.StatusEnum;

public class Factory {
	
	private Factory(){
		
	}
	
	public static Boleto boleto(){
		return new Boleto();
	}
	
	public static Boleto boleto(StatusEnum status, BigDecimal valor){
		Boleto boleto = new Boleto();
		boleto.setStatus(status);
		boleto.setValor(valor);
		return boleto;
	}
	
	public static UUID uuid(){
		return UUID.fromString("f382f9cf-e8e3-4571-8053-c0374ffacd57");
	}
    
}