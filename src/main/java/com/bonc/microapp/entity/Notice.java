package com.bonc.microapp.entity;

import java.util.Date;

public class Notice {
	private Long id;
	private Long noticeType;
	private String title;
	private String content;
	private Long topFlag;
	private Long hotFlag;
	private Date createDate;
	private Date updateDate;
	private Long staffId;
	private Long state;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(Long noticeType) {
		this.noticeType = noticeType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTopFlag() {
		return topFlag;
	}
	public void setTopFlag(Long topFlag) {
		this.topFlag = topFlag;
	}
	public Long getHotFlag() {
		return hotFlag;
	}
	public void setHotFlag(Long hotFlag) {
		this.hotFlag = hotFlag;
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
	public Long getStaffId() {
		return staffId;
	}
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
}
