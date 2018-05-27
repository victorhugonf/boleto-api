package io.github.victorhugonf.boletoapi.ejb.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;

@Stateless
@LocalBean
public class BoletoDao extends GenericDao<Boleto> {

	@Override
	protected Class<Boleto> getClazz() {
		return Boleto.class;
	}

}