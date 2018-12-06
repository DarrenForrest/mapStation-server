package com.bonc.core.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;

public class File extends BaseEntity {
	private Long id;
	private String fileName;
	private String filePath;
	private Long fileTypeId;
	private String fileHeader;
	private String remark;
	private Long staffId;
	private Long state;
	private Date stateDate;
	private Date createDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Long getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(Long fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	public String getFileHeader() {
		return fileHeader;
	}
	public void setFileHeader(String fileHeader) {
		this.fileHeader = fileHeader;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
