package io.github.victorhugonf.boletoapi.ejb.service;

import java.util.List;
import java.util.UUID;

import io.github.victorhugonf.boletoapi.ejb.dao.GenericDao;
import io.github.victorhugonf.boletoapi.ejb.entity.EntityIdentifiable;

public abstract class GenericService <E extends EntityIdentifiable,
									D extends GenericDao<E>>
									implements Service<E>{

	protected abstract D dao();
	protected abstract Class<E> getClazz();

	@Override
    public E persist(E object) throws Exception {
		return dao().persist(object);
    }

    @Override
    public E merge(E object) throws Exception {
    	return dao().merge(object);
    }

    @Override
    public void remove(UUID id) throws Exception {
    	remove(getById(id));
    }

    @Override
    public void remove(E object) throws Exception {
    	dao().remove(object);
    }

    @Override
    public List<E> getAll() throws Exception {
        return dao().getAll();
    }

    @Override
    public E getById(UUID id) throws Exception {
        return dao().getById(id);
    }

    @Override
    public E get(E object) throws Exception {
        return dao().get(object);
    }

}