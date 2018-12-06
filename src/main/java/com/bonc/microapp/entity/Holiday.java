package com.bonc.microapp.entity;

import java.util.Date;

import com.bonc.tools.BaseEntity;


public class Holiday extends BaseEntity {
	private Long id;
	private Long holidayType;
	private Date holiday;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getHolidayType() {
		return holidayType;
	}
	public void setHolidayType(Long holidayType) {
		this.holidayType = holidayType;
	}
	public Date getHoliday() {
		return holiday;
	}
	public void setHoliday(Date holiday) {
		this.holiday = holiday;
	}
}
