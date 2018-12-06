package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapResource  extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	private String firstType;
	private String secondType;
	public String getFirstType() {
		return firstType;
	}
	public void setFirstType(String firstType) {
		this.firstType = firstType;
	}
	public String getSecondType() {
		return secondType;
	}
	public void setSecondType(String secondType) {
		this.secondType = secondType;
	}
	
	

}
