package com.bonc.core.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class EnumCfg extends BaseEntity {
	private Long id;
	private Long enumKey;
	private String enumValue;
	private Long catalogId;
	private Long orderNum;
	private Long pid;
	private Date updateDate;
	private Long state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEnumKey() {
		return enumKey;
	}
	public void setEnumKey(Long enumKey) {
		this.enumKey = enumKey;
	}
	public String getEnumValue() {
		return enumValue;
	}
	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}
	public Long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}
	public Long getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
}
