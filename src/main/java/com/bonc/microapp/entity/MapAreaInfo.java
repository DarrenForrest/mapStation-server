package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapAreaInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String areaCode;
	private String areaName;
	private String areaLevel;
	private String parentAreaCode;
	private String centerLong;
	private String centetLat;
	private String createDate;
	private String bounds;
	private String stepNum;
	private String coorDinates;
	
	public String getCoorDinates() {
		return coorDinates;
	}
	public void setCoorDinates(String coorDinates) {
		this.coorDinates = coorDinates;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaLevel() {
		return areaLevel;
	}
	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}
	public String getParentAreaCode() {
		return parentAreaCode;
	}
	public void setParentAreaCode(String parentAreaCode) {
		this.parentAreaCode = parentAreaCode;
	}
	public String getCenterLong() {
		return centerLong;
	}
	public void setCenterLong(String centerLong) {
		this.centerLong = centerLong;
	}
	public String getCentetLat() {
		return centetLat;
	}
	public void setCentetLat(String centetLat) {
		this.centetLat = centetLat;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getBounds() {
		return bounds;
	}
	public void setBounds(String bounds) {
		this.bounds = bounds;
	}
	public String getStepNum() {
		return stepNum;
	}
	public void setStepNum(String stepNum) {
		this.stepNum = stepNum;
	}
	@Override
	public String toString() {
		return "MapAreaInfo [areaCode=" + areaCode + ", areaName=" + areaName + ", areaLevel=" + areaLevel
				+ ", parentAreaCode=" + parentAreaCode + ", centerLong=" + centerLong + ", centetLat=" + centetLat
				+ ", createDate=" + createDate + ", bounds=" + bounds + ", stepNum=" + stepNum + ", coorDinates="
				+ coorDinates + "]";
	}
	
	
}
