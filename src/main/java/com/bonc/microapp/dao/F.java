package com.bonc.microapp.dao;

public final class F {
	
	public static String NAMESPACE_MICROAPP = "com.bonc.microapp.entity";
	
	public static String ACTION_LOGIN = "login";
	public static String ACTION_WORKLOGINOUT = "workloginout";
	

	/*
	 * 数据范围
	 * 1：仅自身	2：仅子部门	3：自身及子部门
	 */
	public interface RangeType {
		long self             = 1L;
		long subsector        = 2L;
		long selfAndSubsector = 3L;
	}
	public static String SHOWAPI_APP_ID = "70748";
	public static String SHOWAPI_APP_KEY = "68289b9c45774f9d9b0c5ae1c1422b16";

	



}

