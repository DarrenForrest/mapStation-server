package com.bonc.microapp.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;


public class MailList extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String mailTo;
	private String mailCc;
	private String mailSubject;
	private String mailTxt;
	private Long state;
	private String tag;
	private Long sendCnt;
	private Date createDate;
	private Date updateDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getMailCc() {
		return mailCc;
	}
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getMailTxt() {
		return mailTxt;
	}
	public void setMailTxt(String mailTxt) {
		this.mailTxt = mailTxt;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Long getSendCnt() {
		return sendCnt;
	}
	public void setSendCnt(Long sendCnt) {
		this.sendCnt = sendCnt;
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
