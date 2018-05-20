package io.github.victorhugonf.boletoapi.ejb.service;

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
    
}