package com.bonc.core.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class OrgExtend extends BaseEntity {
	private Long orgId;
	private String orgMark;
	private String cashSaveRule;
	private Date cashZeroDate;
	private String chequeSaveRule;
	private Date chequeZeroDate;
	private Long arrivedSpeed;
	private Long noAlarm;
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgMark() {
		return orgMark;
	}
	public void setOrgMark(String orgMark) {
		this.orgMark = orgMark;
	}
	public String getCashSaveRule() {
		return cashSaveRule;
	}
	public void setCashSaveRule(String cashSaveRule) {
		this.cashSaveRule = cashSaveRule;
	}
	public Date getCashZeroDate() {
		return cashZeroDate;
	}
	public void setCashZeroDate(Date cashZeroDate) {
		this.cashZeroDate = cashZeroDate;
	}
	public String getChequeSaveRule() {
		return chequeSaveRule;
	}
	public void setChequeSaveRule(String chequeSaveRule) {
		this.chequeSaveRule = chequeSaveRule;
	}
	public Date getChequeZeroDate() {
		return chequeZeroDate;
	}
	public void setChequeZeroDate(Date chequeZeroDate) {
		this.chequeZeroDate = chequeZeroDate;
	}
	public Long getArrivedSpeed() {
		return arrivedSpeed;
	}
	public void setArrivedSpeed(Long arrivedSpeed) {
		this.arrivedSpeed = arrivedSpeed;
	}
	public Long getNoAlarm() {
		return noAlarm;
	}
	public void setNoAlarm(Long noAlarm) {
		this.noAlarm = noAlarm;
	}

}
