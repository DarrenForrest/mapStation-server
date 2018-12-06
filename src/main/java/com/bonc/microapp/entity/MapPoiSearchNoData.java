package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapPoiSearchNoData extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String smallBounds;
	private String regionName;
	private String stepNum;
	private String pageNum;
	private String keyWord;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSmallBounds() {
		return smallBounds;
	}
	public void setSmallBounds(String smallBounds) {
		this.smallBounds = smallBounds;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getStepNum() {
		return stepNum;
	}
	public void setStepNum(String stepNum) {
		this.stepNum = stepNum;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	
}
