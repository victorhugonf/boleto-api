package io.github.victorhugonf.boletoapi.dto;

import java.io.Serializable;

import io.github.victorhugonf.boletoapi.ejb.entity.StatusEnum;

public class StatusDto implements Serializable{
	
	private static final long serialVersionUID = -1299654911670396939L;

	private StatusEnum status;

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

}
