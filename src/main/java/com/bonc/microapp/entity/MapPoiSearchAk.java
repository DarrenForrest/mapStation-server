package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapPoiSearchAk extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String cityCode;
	private String cityName;
	private String ak;
	private String quotaNum;
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAk() {
		return ak;
	}
	public void setAk(String ak) {
		this.ak = ak;
	}
	public String getQuotaNum() {
		return quotaNum;
	}
	public void setQuotaNum(String quotaNum) {
		this.quotaNum = quotaNum;
	}
	
}
