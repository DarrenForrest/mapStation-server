package com.bonc.tools;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MoneyUtils {

	public MoneyUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static List ListForShow(List list, String field1, String field2, String field3) {
		
		for(Object obj : list) {
			if(obj instanceof Map) {
				Map map = (Map)obj;
				if(StringUtils.isNotBlank(field1)) {
					map.put(field1, FormatForShow(map.get(field1)));
				}
				if(StringUtils.isNotBlank(field2)) {
					map.put(field2, FormatForShow(map.get(field2)));
				}
				if(StringUtils.isNotBlank(field3)) {
					map.put(field3, FormatForShow(map.get(field3)));
				}
			}
		}
		
		return list;
	}
	
	public static List ListForSave(List list, String field1, String field2, String field3) {
		
		for(Object obj : list) {
			if(obj instanceof Map) {
				Map map = (Map)obj;
				if(StringUtils.isNotBlank(field1)) {
					map.put(field1, FormatForSave(map.get(field1)));
				}
				if(StringUtils.isNotBlank(field2)) {
					map.put(field2, FormatForSave(map.get(field2)));
				}
				if(StringUtils.isNotBlank(field3)) {
					map.put(field3, FormatForSave(map.get(field3)));
				}
			}
		}
		
		return list;
	}
	
	public static Object FormatForShow(Object value) {
		if(value != null) {
			if(value instanceof String) {
				return Arith.div(Double.valueOf((String)value), 100.0, 2);
			} else if(value instanceof Long) {
				return Arith.div(Double.valueOf((Long)value), 100.0, 2);
			} else if(value instanceof Integer) {
				return Arith.div(Double.valueOf((Integer)value), 100.0, 2);
			} else if(value instanceof Float) {
				return Arith.div(Double.valueOf((Float)value), 100.0, 2);
			} else if(value instanceof Double) {
				return Arith.div(Double.valueOf((Double)value), 100.0, 2);
			} else if(value instanceof BigDecimal) {
				return Arith.div(((BigDecimal)value).doubleValue(), 100.0, 2);
			} else {
				
			}
		}
		return value;
	}
	
	public static Object FormatForSave(Object value) {
		if(value != null) {
			if(value instanceof String) {
				return Double.valueOf(Arith.mul(Double.valueOf((String)value), 100.0, 0)).longValue();
			} else if(value instanceof Long) {
				return Double.valueOf(Arith.mul(Double.valueOf((Long)value), 100.0, 0)).longValue();
			} else if(value instanceof Integer) {
				return Double.valueOf(Arith.mul(Double.valueOf((Integer)value), 100.0, 0)).longValue();
			} else if(value instanceof Float) {
				return Double.valueOf(Arith.mul(Double.valueOf((Float)value), 100.0, 0)).longValue();
			} else if(value instanceof Double) {
				return Double.valueOf(Arith.mul(Double.valueOf((Double)value), 100.0, 0)).longValue();
			} else if(value instanceof BigDecimal) {
				return Double.valueOf(Arith.mul(((BigDecimal)value).doubleValue(), 100.0, 0)).longValue();
			} else {
				
			}
		}
		return value;
	}
}
