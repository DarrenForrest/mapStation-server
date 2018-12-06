package com.bonc.common;

import javax.servlet.http.HttpServletRequest;

public class Auth {
	private Long   staffId;
	private String staffName;
	private Long   orgId;
	private String optionOrgId;
	private Long   currentCycleId;
	
	static public Auth getAuth(HttpServletRequest request) {
//		Auth auth = new Auth();
//		auth.setStaffId(10001L);
//		auth.setOrgId(1L);
//		auth.setStaffName("FixTextStaff");
		
		Auth auth =(Auth) request.getSession().getValue("auth");
		
		return auth;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOptionOrgId() {
		return optionOrgId;
	}

	public void setOptionOrgId(String optionOrgId) {
		this.optionOrgId = optionOrgId;
	}

	public Long getCurrentCycleId() {
		return currentCycleId;
	}

	public void setCurrentCycleId(Long currentCycleId) {
		this.currentCycleId = currentCycleId;
	}
	

}
