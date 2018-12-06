package com.bonc.microapp.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class JobLog extends BaseEntity {
	private Long exeFlowId;
	private Long jobId;
	private String jobName;
	private Date startDate;
	private Date endDate;
	private Long state;
	private String retMsg;
	private Long triggerType;
	public Long getExeFlowId() {
		return exeFlowId;
	}
	public void setExeFlowId(Long exeFlowId) {
		this.exeFlowId = exeFlowId;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public Long getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(Long triggerType) {
		this.triggerType = triggerType;
	}
}
