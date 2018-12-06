package com.bonc.tools;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page extends BaseEntity {
	
	private final int default_pageSize = 10;
	private final int default_currentPage = 1;

	private long totalCnt;
	private int pageCnt;
	private int pageSize;
	private int currentPage;
	private List<Object> dataList;
	
	public Page() {
		defaultProperties();
	}
	
	public Page(int pageSize, int currentPage) {
		defaultProperties();
		this.pageSize = pageSize > 0 ? pageSize : default_pageSize;
		this.currentPage = currentPage > 0 ? currentPage : default_currentPage;
	}
	
	public Page(Map pageParam) {
		defaultProperties();
		if(pageParam != null) {
			String strPageSize = (String)pageParam.get("pageSize");
			String strCurrentPage = (String)pageParam.get("currentPage");
			if(strPageSize != null) {
				this.pageSize = Integer.valueOf(strPageSize).intValue();
			}
			if(strCurrentPage != null) {
				this.currentPage = Integer.valueOf(strCurrentPage).intValue();
			}
		}
	}
	
	public long getTotalCnt() {
		return totalCnt;
	}
	public int getPageCnt() {
		return pageCnt;
	}
	public void setPageCnt(int pageCnt) {
		this.pageCnt = pageCnt;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public List<Object> getDataList() {
		return dataList;
	}
	//public void setDataList(List<Object> dataList) {
	//	setDataList(dataList, Object.class);
	//}
	public void setDataList(List<Object> dataList, Class<? extends Object> cls, long totalCnt) {
		this.totalCnt = totalCnt < 0 ? 0 : totalCnt;
		this.pageCnt  = (int)Arith.ceil(totalCnt, pageSize);
		this.dataList = toBeanList(dataList, cls);
	}
	
	private void defaultProperties(){
		this.totalCnt = 0;
		this.pageCnt = 0;
		this.pageSize = default_pageSize;
		this.currentPage = default_currentPage;
		this.dataList = null;
	}
	
	/////////////////////////////////
	// Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean  
	public static Object transMap2Bean(Map<String, Object> map, Class<? extends Object> cls) {
		try {
			Object obj = cls.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (map.containsKey(key)) {
					Object value = map.get(key);
					//得到property对应的setter方法
					Method setter = property.getWriteMethod();
					
					//按照参数类型和值类型传换后调用setter方法
					setter.invoke(obj, convertValue(value, setter.getParameterTypes()[0]));
				}
			}
			return obj;
		} catch (Exception e) {
			System.out.println("transMap2Bean Error " + e);
		}
		return null;
	}
	
	private static Object convertValue(Object value, Class<? extends Object> cls) {
		if(cls != null) {
			if(cls.equals(value.getClass())) {
				return value;
			} else if(cls.equals(String.class)) {
				return value.toString();
			} else if(cls.equals(Integer.class)) {
				return intValueOf(value);
			} else if(cls.equals(Long.class)) {
				return longValueOf(value);
			} else if(cls.equals(Float.class)) {
				return floatValueOf(value);
			} else if(cls.equals(Double.class)) {
				return doubleValueOf(value);
			}
		}
		return value;
	}
	private static int intValueOf(Object value) {
		if(value instanceof BigDecimal) {
			return ((BigDecimal)value).intValue();
		} else if(value instanceof Integer) {
			return ((Integer)value).intValue();
		} else if(value instanceof Long) {
			return ((Long)value).intValue();
		} else if(value instanceof Float) {
			return ((Float)value).intValue();
		} else if(value instanceof Double) {
			return ((Double)value).intValue();
		} else if(value instanceof String) {
			return Integer.valueOf((String)value);
		}
		return 0;
	}
	private static long longValueOf(Object value) {
		if(value instanceof BigDecimal) {
			return ((BigDecimal)value).longValue();
		} else if(value instanceof Integer) {
			return ((Integer)value).longValue();
		} else if(value instanceof Long) {
			return ((Long)value).longValue();
		} else if(value instanceof Float) {
			return ((Float)value).longValue();
		} else if(value instanceof Double) {
			return ((Double)value).longValue();
		} else if(value instanceof String) {
			return Long.valueOf((String)value);
		}
		return 0;
	}
	private static float floatValueOf(Object value) {
		if(value instanceof BigDecimal) {
			return ((BigDecimal)value).floatValue();
		} else if(value instanceof Integer) {
			return ((Integer)value).floatValue();
		} else if(value instanceof Long) {
			return ((Long)value).floatValue();
		} else if(value instanceof Float) {
			return ((Float)value).floatValue();
		} else if(value instanceof Double) {
			return ((Double)value).floatValue();
		} else if(value instanceof String) {
			return Float.valueOf((String)value);
		}
		return 0;
	}
	private static double doubleValueOf(Object value) {
		if(value instanceof BigDecimal) {
			return ((BigDecimal)value).doubleValue();
		} else if(value instanceof Integer) {
			return ((Integer)value).doubleValue();
		} else if(value instanceof Long) {
			return ((Long)value).doubleValue();
		} else if(value instanceof Float) {
			return ((Float)value).doubleValue();
		} else if(value instanceof Double) {
			return ((Double)value).doubleValue();
		} else if(value instanceof String) {
			return Double.valueOf((String)value);
		}
		return 0;
	}

	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	public static Map<String, Object> transBean2Map(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}

		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object> toBeanList(List<? extends Object> list, Class<? extends Object> cls) {
		
		if(cls == null || list == null || list.size() == 0 || !(list.get(0) instanceof Map)) {
			return (List<Object>)list;
		}
		
		List<Object> newList = new ArrayList<Object>();
		for(Object item : list) {
			newList.add(transMap2Bean((Map<String, Object>)item, cls));
		}
		
		return newList;
	}

}
