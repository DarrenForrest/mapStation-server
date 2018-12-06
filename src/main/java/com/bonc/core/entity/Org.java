package com.bonc.core.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class Org extends BaseEntity {
	private Long orgId;
	private String orgName;
	private Long levelId;
	private Long state;
	private Date stateDate;
	private Date createDate;
	private Long parentId;
	private Long extParentId;
	private Long extTypeId;
	private String orgMark;
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getLevelId() {
		return levelId;
	}
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public Date getStateDate() {
		return stateDate;
	}
	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getExtParentId() {
		return extParentId;
	}
	public void setExtParentId(Long extParentId) {
		this.extParentId = extParentId;
	}
	public Long getExtTypeId() {
		return extTypeId;
	}
	public void setExtTypeId(Long extTypeId) {
		this.extTypeId = extTypeId;
	}
	public String getOrgMark() {
		return orgMark;
	}
	public void setOrgMark(String orgMark) {
		this.orgMark = orgMark;
	}
}
