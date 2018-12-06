package com.bonc.microapp.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class AmsActionLog extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long amsStaffId;
	private String amsStaffName;
	private String workDate;
	private String action;
	private Date planTime;
	private Date exeTime;
	private String exeCode;
	private String exeMsg;
	private String exeErrMsg;
	private Date createDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAmsStaffId() {
		return amsStaffId;
	}
	public void setAmsStaffId(Long amsStaffId) {
		this.amsStaffId = amsStaffId;
	}
	public String getAmsStaffName() {
		return amsStaffName;
	}
	public void setAmsStaffName(String amsStaffName) {
		this.amsStaffName = amsStaffName;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	public Date getExeTime() {
		return exeTime;
	}
	public void setExeTime(Date exeTime) {
		this.exeTime = exeTime;
	}
	public String getExeCode() {
		return exeCode;
	}
	public void setExeCode(String exeCode) {
		this.exeCode = exeCode;
	}
	public String getExeMsg() {
		return exeMsg;
	}
	public void setExeMsg(String exeMsg) {
		this.exeMsg = exeMsg;
	}
	public String getExeErrMsg() {
		return exeErrMsg;
	}
	public void setExeErrMsg(String exeErrMsg) {
		this.exeErrMsg = exeErrMsg;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
