package com.bonc.core.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class Rule extends BaseEntity {
	private Long ruleId;
	private String ruleName;
	private String url;
	private String icon;
	private Long orderNum;
	private Long visible;
	private Long nodeType;
	private String nodeCode;
	private Long parentId;
	private Long state;
	private Date stateDate;
	private Date createDate;
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Long getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}
	public Long getVisible() {
		return visible;
	}
	public void setVisible(Long visible) {
		this.visible = visible;
	}
	public Long getNodeType() {
		return nodeType;
	}
	public void setNodeType(Long nodeType) {
		this.nodeType = nodeType;
	}
	public String getNodeCode() {
		return nodeCode;
	}
	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
}
