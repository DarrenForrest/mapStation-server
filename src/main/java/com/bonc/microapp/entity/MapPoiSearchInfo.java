package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapPoiSearchInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String address;
	private String regionName;
	private String regionCode;
	private String lat;
	private String lng;
	private String poiName;
	private String bdUid;
	private String stdTag;
	private String keyWord;
	private String cityCode;
	private String range;
	private String searchType;
	private String createTime;
	private String updateTime;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getPoiName() {
		return poiName;
	}
	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}
	
	public String getBdUid() {
		return bdUid;
	}
	public void setBdUid(String bdUid) {
		this.bdUid = bdUid;
	}

	
	public String getStdTag() {
		return stdTag;
	}
	public void setStdTag(String stdTag) {
		this.stdTag = stdTag;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
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
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	

}
