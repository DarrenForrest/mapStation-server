package com.bonc.microapp.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class AmsStaff extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String loginName;
	private String password;
	private String staffName;
	private String remark;
	private Long state;
	private String adminName;
	private String email;
	private String lockState;
	private String autoLoginFlag;
	private String autoLoginoutFlag;
	private String emailNoticeFlag;
	private Date createDate;
	private Date updateDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLockState() {
		return lockState;
	}
	public void setLockState(String lockState) {
		this.lockState = lockState;
	}
	public String getAutoLoginFlag() {
		return autoLoginFlag;
	}
	public void setAutoLoginFlag(String autoLoginFlag) {
		this.autoLoginFlag = autoLoginFlag;
	}
	public String getAutoLoginoutFlag() {
		return autoLoginoutFlag;
	}
	public void setAutoLoginoutFlag(String autoLoginoutFlag) {
		this.autoLoginoutFlag = autoLoginoutFlag;
	}
	public String getEmailNoticeFlag() {
		return emailNoticeFlag;
	}
	public void setEmailNoticeFlag(String emailNoticeFlag) {
		this.emailNoticeFlag = emailNoticeFlag;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
