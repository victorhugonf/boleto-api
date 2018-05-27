package io.github.victorhugonf.boletoapi.ejb.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
	public Boleto getById(UUID id) throws Exception {
		Boleto boleto = super.getById(id);
		
		if(boleto != null
			&& StatusEnum.PENDING.equals(boleto.getStatus())){
			
			boleto.setMulta(calcularMulta(boleto));
		}
		
		return boleto;
	}

	private BigDecimal calcularMulta(Boleto boleto) {
		Instant hoje = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
		Instant vencimento = boleto.getDataVencimento().toInstant();
		
		if(!hoje.isAfter(vencimento)){
			return BigDecimal.ZERO;
		}

		BigDecimal multa = BigDecimal.valueOf(0.01);
		
		if(!hoje.isAfter(vencimento.plus(10, ChronoUnit.DAYS))){
			multa = BigDecimal.valueOf(0.005);
		}
		
		return boleto.getValorTotalEmCentavos().multiply(multa).setScale(0, RoundingMode.HALF_EVEN);
	}
    
}