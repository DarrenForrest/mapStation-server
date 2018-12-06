package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapPoiSearchTotal extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String keyWord;
	private String regionName;
	private String regionCode;
	private String total;
	private String startTime;
	private String endTime;
	private String bounds;
	private String stepNum;
	private String wd2Name;
	private String areaCode;
	
	public String getWd2Name() {
		return wd2Name;
	}
	public void setWd2Name(String wd2Name) {
		this.wd2Name = wd2Name;
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
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	

}
