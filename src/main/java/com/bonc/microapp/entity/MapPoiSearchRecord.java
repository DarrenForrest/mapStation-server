package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapPoiSearchRecord extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String firstPoi;
	private String secondPoi;
	private String regionName;
	private String regionCode;
	private String areaCode;
	private String bounds;
	private String stepNum;
	private String smallBounds;
	private String pageNum;
	private String searchResult;
	private String searchType;
	private String createTime;
	
	public String getFirstPoi() {
		return firstPoi;
	}
	public void setFirstPoi(String firstPoi) {
		this.firstPoi = firstPoi;
	}
	public String getSecondPoi() {
		return secondPoi;
	}
	public void setSecondPoi(String secondPoi) {
		this.secondPoi = secondPoi;
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
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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
	public String getSmallBounds() {
		return smallBounds;
	}
	public void setSmallBounds(String smallBounds) {
		this.smallBounds = smallBounds;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public String getSearchResult() {
		return searchResult;
	}
	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
