package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class JobDefine extends BaseEntity {
	private Long id;
	private String jobName;
	private String jobClass;
	private String storedProcedure;
	private String cronExpression;
	private String jobDesc;
	private Long status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public String getStoredProcedure() {
		return storedProcedure;
	}
	public void setStoredProcedure(String storedProcedure) {
		this.storedProcedure = storedProcedure;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
}
