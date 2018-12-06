package com.bonc.tools;

import java.util.HashMap;
import java.util.Map;

public class ParamVo {
	private String nameSpace;
	private String method;
	private Page page;
	private Object object;
	private String orderby;
	private String[] groupby;
	private Map<String, Object> param;
	private Class<? extends Object> objectClass;
	
	public ParamVo() {
		
	}
	
	public ParamVo(Class<? extends Object> objectClass) {
		this.objectClass = objectClass;
	}
	
	public void put(String key, Object value) {
		if(this.param == null) {
			this.param = new HashMap<String, Object>();
		}
		this.param.put(key, value);
	}
	
	public Object get(String key) {
		if(this.param == null) {
			return null;
		}
		return this.param.get(key);
	}
	
	public void setOrderby(String sortColumn, String ascOrDesc) {
		if(sortColumn == null) {
			this.orderby = null;
			return;
		}
		this.orderby = sortColumn;
		if(ascOrDesc != null) {
			this.orderby += (" " + ascOrDesc);
		}
	}
	
	public void setGroupby(String aggregateColumn, String partitionColumn) {
		if(aggregateColumn == null || partitionColumn == null || aggregateColumn.isEmpty()) {
			this.groupby = null;
			return;
		}
		this.groupby = new String[]{aggregateColumn, partitionColumn};
	}
	
	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	public String[] getGroupby() {
		return groupby;
	}
	public void setGroupby(String[] groupby) {
		this.groupby = groupby;
	}
	public Map<String, Object> getParam() {
		return param;
	}
	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
	public Class<? extends Object> getObjectClass() {
		return objectClass;
	}
	public void setObjectClass(Class<? extends Object> objectClass) {
		this.objectClass = objectClass;
	}
}
