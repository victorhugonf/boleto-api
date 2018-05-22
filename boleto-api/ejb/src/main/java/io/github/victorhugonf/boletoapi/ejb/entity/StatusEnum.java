package io.github.victorhugonf.boletoapi.ejb.entity;

import java.util.Arrays;

public enum StatusEnum {
	
	PENDING("PENDING"),
	PAID("PAID"),
	CANCELED("CANCELED");
	
	private String status;
	
	private StatusEnum (String status){
		this.status = status;
	}
	
	public String getStatus(){
		return status;
	}
	
	public static StatusEnum getStatus(String status){
		return Arrays.asList(StatusEnum.values()).stream()
				.filter(s -> s.getStatus().equals(status)).findFirst().orElse(null);
	}

}
