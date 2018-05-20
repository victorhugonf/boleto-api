package io.github.victorhugonf.boletoapi.ejb.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import io.github.victorhugonf.boletoapi.ejb.dao.BoletoDao;
import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.entity.StatusEnum;

@Stateless
@LocalBean
public class BoletoService extends GenericService<Boleto, BoletoDao> {
	   
	@EJB
	private BoletoDao boletoDao;
	
	@Override
	protected BoletoDao dao() {
		return boletoDao;
	}
	
	@Override
    protected void validatePersist(Boleto object) throws Exception{
		
    }
    
    @Override
    protected void validateMerge(Boleto object) throws Exception{
    	
    }
    
    @Override
	protected void validateRemove(Boleto object) throws Exception {
				
	}

	@Override
	protected Class<Boleto> getClazz() {
		return Boleto.class;
	}
	
	public boolean processarStatus(Boleto boleto, StatusEnum status) throws Exception {
		if(!StatusEnum.PENDING.equals(boleto.getStatus())){
			return false;
		}

		boleto.setStatus(status);
		merge(boleto);
		return true;
	}
	
	@Override
	public Boleto get(UUID id) throws Exception {
		Boleto boleto = super.get(id);
		
		if(StatusEnum.PENDING.equals(boleto.getStatus())){
			boleto.setMulta(calcularMulta(boleto));
		}
		
		return boleto;
	}

	private BigDecimal calcularMulta(Boleto boleto) {
		Instant hoje = Instant.now().truncatedTo(ChronoUnit.DAYS);
		Instant vencimento = boleto.getDataVencimento().toInstant();
		
		if(hoje.isAfter(vencimento)){
			return BigDecimal.ZERO;
		}

		BigDecimal multa = BigDecimal.valueOf(1.01);
		
		if(hoje.plus(10, ChronoUnit.DAYS).isAfter(vencimento)){
			multa = BigDecimal.valueOf(1.005);
		}
		
		return boleto.getValor().multiply(multa);
	}
    
}