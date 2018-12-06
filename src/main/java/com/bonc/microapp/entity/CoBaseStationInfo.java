package com.bonc.microapp.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class CoBaseStationInfo extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	private String lacCode;
	private String cellLac;
	private String cellCi;
	private String netType;
	private String lacName;
	private String areaCode;
	private String cityCode;
	private String lacLong;
	private String lacLat;
	private char dateResource;
	private Date createDate;
	private String lacLongUpdate;
	private String lacLatUpdate;
	public String getLacCode() {
		return lacCode;
	}
	public void setLacCode(String lacCode) {
		this.lacCode = lacCode;
	}
	public String getCellLac() {
		return cellLac;
	}
	public void setCellLac(String cellLac) {
		this.cellLac = cellLac;
	}
	public String getCellCi() {
		return cellCi;
	}
	public void setCellCi(String cellCi) {
		this.cellCi = cellCi;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getLacName() {
		return lacName;
	}
	public void setLacName(String lacName) {
		this.lacName = lacName;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getLacLong() {
		return lacLong;
	}
	public void setLacLong(String lacLong) {
		this.lacLong = lacLong;
	}
	public String getLacLat() {
		return lacLat;
	}
	public void setLacLat(String lacLat) {
		this.lacLat = lacLat;
	}
	public char getDateResource() {
		return dateResource;
	}
	public void setDateResource(char dateResource) {
		this.dateResource = dateResource;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getLacLongUpdate() {
		return lacLongUpdate;
	}
	public void setLacLongUpdate(String lacLongUpdate) {
		this.lacLongUpdate = lacLongUpdate;
	}
	public String getLacLatUpdate() {
		return lacLatUpdate;
	}
	public void setLacLatUpdate(String lacLatUpdate) {
		this.lacLatUpdate = lacLatUpdate;
	}
	

}
