package com.bonc.core.entity;

import com.bonc.tools.BaseEntity;

public class StaffRole extends BaseEntity {
	private Long id;
	private Long staffId;
	private Long roleId;
	private Long orgId;
	private Long rangeType;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStaffId() {
		return staffId;
	}
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getRangeType() {
		return rangeType;
	}
	public void setRangeType(Long rangeType) {
		this.rangeType = rangeType;
	}
}
