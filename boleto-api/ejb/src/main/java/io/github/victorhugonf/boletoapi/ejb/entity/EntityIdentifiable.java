package io.github.victorhugonf.boletoapi.ejb.entity;

import java.io.Serializable;
import java.util.UUID;

public interface EntityIdentifiable extends Serializable {

	UUID getId();

	void setId(UUID id);
	
	void validate() throws Exception;

}
