package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapResourceState extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	private String firstType;
	private String secondType;
	private String cityCode;
	private String state;
	private String count;
	private String pass;
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
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	

}
