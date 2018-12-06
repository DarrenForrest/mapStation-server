package com.bonc.microapp.entity;

import com.bonc.tools.BaseEntity;

public class MapInfo extends BaseEntity  implements Comparable<MapInfo>{

	private String celllac;
	private String cellCi;
	private double lacLong;
	private double lacLat;
	private String lacCode;
	private String lacName;
	private double distance;
	private String cityCode;
	private String netType;
	
	
	public double getLacLat() {
		return lacLat;
	}
	public void setLacLat(double lacLat) {
		this.lacLat = lacLat;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getLacCode() {
		return lacCode;
	}
	public void setLacCode(String lacCode) {
		this.lacCode = lacCode;
	}
	public String getLacName() {
		return lacName;
	}
	public void setLacName(String lacName) {
		this.lacName = lacName;
	}
	public String getCelllac() {
		return celllac;
	}
	public void setCelllac(String celllac) {
		this.celllac = celllac;
	}
	public String getCellCi() {
		return cellCi;
	}
	public void setCellCi(String cellCi) {
		this.cellCi = cellCi;
	}
	public double getLacLong() {
		return lacLong;
	}
	public void setLacLong(double lacLong) {
		this.lacLong = lacLong;
	}
	public double getLaclLat() {
		return lacLat;
	}
	public void setLaclLat(double laclLat) {
		this.lacLat = laclLat;
	}
	@Override
	public String toString() {
		return "MapInfo [celllac=" + celllac + ", cellCi=" + cellCi + ", lacLong=" + lacLong + ", laclLat=" + lacLat
				+ ", lacCode=" + lacCode + ", lacName=" + lacName + ", distance=" + distance + ", cityCode=" + cityCode
				+ "]";
	}
	@Override
	public int compareTo(MapInfo o) {
		int i = (int) (this.getDistance() - o.getDistance());//先按照年龄排序  
	        if(i == 0){  
	            return (int) (this.getLacLat() - o.getLacLat());//如果年龄相等了再用分数进行排序  
	        }  
	        return i; 
	}

}
