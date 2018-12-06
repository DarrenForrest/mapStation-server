package com.bonc.core.entity;

import com.bonc.tools.BaseEntity;

public class EnumCatalog extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String catalogCode;
	private String catalogName;
	private Long state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCatalogCode() {
		return catalogCode;
	}
	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
}
