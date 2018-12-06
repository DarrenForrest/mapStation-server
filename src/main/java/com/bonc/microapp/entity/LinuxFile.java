package com.bonc.microapp.entity;

public class LinuxFile {
	
	private String fileName;
	private String string1;
	private String Buser;//所属用户
	private String Bgroup;//所属组
	private String createTime;
	private String fileType;
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getString1() {
		return string1;
	}
	public void setString1(String string1) {
		this.string1 = string1;
	}
	public String getBuser() {
		return Buser;
	}
	public void setBuser(String buser) {
		Buser = buser;
	}
	public String getBgroup() {
		return Bgroup;
	}
	public void setBgroup(String bgroup) {
		Bgroup = bgroup;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "LinuxFile [fileName=" + fileName + ", string1=" + string1 + ", Buser=" + Buser + ", Bgroup=" + Bgroup
				+ ", createTime=" + createTime + "]";
	}
	
	

}
