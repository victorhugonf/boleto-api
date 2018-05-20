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

//	public List<Boleto> getByModel(String model) throws Exception {
//		CriteriaQuery<Boleto> cq = createQuery();
//    	Root<Boleto> root = createRoot(cq);
//
//    	cq.where(getCriteriaBuilder()
//    			.like(root.<String>get(Aircraft_.model), "%" + model + "%"));
//
////    	try{
//    	return getEntityManager().createQuery(cq).getResultList();
//
////    	}catch(NoResultException e){
////    		return null;
////    	}
//    }

}