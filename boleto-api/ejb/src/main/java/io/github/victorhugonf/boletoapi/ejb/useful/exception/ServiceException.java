package io.github.victorhugonf.boletoapi.ejb.useful.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public abstract class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -9175271990963868606L;
	
	public ServiceException() {
		super();
	}
	
	public ServiceException(String message){
		super(message);
	}
	
}