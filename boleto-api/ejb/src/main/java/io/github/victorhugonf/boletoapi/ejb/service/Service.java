package io.github.victorhugonf.boletoapi.ejb.service;

import java.util.List;
import java.util.UUID;

import io.github.victorhugonf.boletoapi.ejb.entity.EntityIdentifiable;

public interface Service <I extends EntityIdentifiable> {
	
    I persist(I object) throws Exception;

	I merge(I object) throws Exception;
    
	void remove(UUID id) throws Exception;
    
    void remove(I object) throws Exception;
    
    List<I> getAll() throws Exception;
    
    I getById(UUID id) throws Exception;
    
    I get(I object) throws Exception;
    
}