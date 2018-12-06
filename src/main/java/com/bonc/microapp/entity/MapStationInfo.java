package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapStationInfo extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	private String celllac;
	private String cellCi;
	private double lacLong;
	private double lacLat;
	private String lacCode;
	private String lacName;
	private String title;
	private String address;
	private double lng;
	private double  lat;
	private String cityCode;
	private String firstType;
	private String secondType;
	private String createTime;
	private String range;
	private String netType;
	
	
	
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
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
	public String getCelllac() {
		return celllac;
	}
	public void setCelllac(String celllac) {
		this.celllac = celllac;
	}
	public String getCellCi() {
		return cellCi;
	}
	public void setCellCi(String cellCi) {
		this.cellCi = cellCi;
	}
	
	public double getLacLong() {
		return lacLong;
	}
	public void setLacLong(double lacLong) {
		this.lacLong = lacLong;
	}
	public double getLacLat() {
		return lacLat;
	}
	public void setLacLat(double lacLat) {
		this.lacLat = lacLat;
	}
	public String getLacCode() {
		return lacCode;
	}
	public void setLacCode(String lacCode) {
		this.lacCode = lacCode;
	}
	public String getLacName() {
		return lacName;
	}
	public void setLacName(String lacName) {
		this.lacName = lacName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	@Override
	public String toString() {
		return "MapStationInfo [celllac=" + celllac + ", cellCi=" + cellCi + ", lacLong=" + lacLong + ", lacLat="
				+ lacLat + ", lacCode=" + lacCode + ", lacName=" + lacName + ", title=" + title + ", address=" + address
				+ ", lng=" + lng + ", lat=" + lat + ", cityCode=" + cityCode + "]";
	}
	

}
