package com.bonc.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/*
 * 通用的JSON输入参数实体，不需要为每个请求定义特定的请求实体
 * 
 * 日期转换默认"yyyy-MM-dd"，如果要使用"yyyy-MM-dd hh:mm:ss"或其他格式，显示调用setDataFormat
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JsonBody extends HashMap<String, Object> implements Map<String, Object> {
	private static final long serialVersionUID = 1L;
	
	private String dateFormat;
	
	/*
	 * 默认构造函数
	 */
	public JsonBody() {
		
	}

	/*
	 * 通过Map<String, Object>对象构造函数
	 * 增加此函数主要是不能直接用在@RequestParameter从FORM中取值，如以后改进可不用此方法
	 */
	public JsonBody(Map<String, Object> map) {
		if(map != null)
			this.putAll(map);
	}

	/*
	 * 获取key指定的值，key的格式为"param.unit1.key1"
	 * 返回值：HashMap<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public Object getValue(String key) {
		Object temp = this;
		if(key == null || "".equals(key)) {
			return temp;
		}
		String [] keys = key.split("\\.");
		for(int i = 0; i < keys.length; i++) {
			if(keys[i].isEmpty() || temp == null) {
				return null;
			} else {
				//temp's type must be Map(using temp.get)
				if(!(temp instanceof Map)) {
					System.out.println("Error: JsonBody.getValue: " + "object" + "'s type is not Map");
					return null;
				}
				if(keys[i].endsWith("]")) {
					String [] strs = keys[i].split("\\[");
					if(strs.length != 2) {
						System.out.println("Error: JsonBody.getValue: " + keys[i] + " is invalid for List");
						return null;
					}
					String name = strs[0];
					String index = strs[1].substring(0, strs[1].length() - 1);
					Object object = ((HashMap<String, Object>)temp).get(name);
					if(!(object instanceof List)) {
						System.out.println("Error: JsonBody.getValue: " + name + "'s type is not List.");
						return null;
					}
					temp = ((List<?>)object).get(Integer.valueOf(index));
				} else {
					temp = ((HashMap<String, Object>)temp).get(keys[i]);
				}
				//if temp's type is List report to Error
				if(temp instanceof List) {
					System.out.println("Error: JsonBody.getValue: " + keys[i] + "'s type is List");
					return null;
				}
			}
		}
		return temp;
	}
	
	/*
	 * 设置key指定的值，key的格式为"param.unit1.key1"
	 * 没有返回值
	 */
	public void setValue(String key, Object obj){
		this.put(key, obj);
		return;
	}
	
	/*
	 * 将key指定的值转换为JSON字符串
	 * 返回值：标准的JSON字符串
	 */
	public String toJson(String key) {
		Object obj = getValue(key);
		if(obj == null)
			return null;
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		//objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*
	 * 将key指定的值转换为JAVA对象，cls为Object.class
	 * 返回值：外部指定的Object类型
	 */
	public Object toJava(String key, Class<? extends Object> cls) {
		return toJava(key, cls, false);
	}
	public Object toJava(String key, Class<? extends Object> cls, boolean needTrans) {
		Object obj = getValue(key);
		if(obj == null)
			return null;
		if(needTrans) {
			Map map = new HashMap<String, Object>();
			for(String k : (Set<String>)((Map)obj).keySet()){
				Object v = ((Map)obj).get(k);
				map.put(columntoproperty(k), v);
			}
			obj = map;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		//objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if(StringUtils.isBlank(this.dateFormat)) {
			//this.setDataFormat("yyyy-MM-dd"); //如果使用默认的保存后会默认为当日8:00:00，先变通处理
		}
		if(!StringUtils.isBlank(this.dateFormat)) {
			java.text.DateFormat myFormat = new java.text.SimpleDateFormat(dateFormat);
			DeserializationConfig cfg = objectMapper.getDeserializationConfig();
			cfg.setDateFormat(myFormat);
			objectMapper = objectMapper.setDeserializationConfig(cfg);
		}
		try {
			if(obj instanceof String)
				return objectMapper.readValue((String)obj, cls);
			
			return objectMapper.readValue(objectMapper.writeValueAsString(obj), cls);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*
	 * 根据指定的格式判断参数是否合格
	 * 1. 判断指定的字段是否存在；
	 * 2. 判断字段的格式是否合格：长度、大小、等；
	 */
	public boolean isValid(String format) {
		if(format == null || "".equals(format))
			return true;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		try {
			
			Map<String, Object> map = mapper.readValue(format, Map.class);
			if(recursive_process("", map) == null)
				return false;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	public boolean isInvalid(String format) {
		return !isValid(format);
	}
	
	public String getDateFormat() {
		return this.dateFormat;
	}
	
	public void setDataFormat(String dataFormat) {
		this.dateFormat = dataFormat;
	}
	
	public int getCode() {
		return (Integer)(this.get("code") == null ? -1 : this.get("code"));
	}
	
	public void setCode(int code) {
		this.put("code", code);
	}
	
	public String getMessage() {
		return (String)(this.get("message") == null ? "" : this.get("message"));
	}
	
	public void setMessage(String message) {
		this.put("message", message);
	}
	
	private Object recursive_process(String namespace, Object obj) {
		
		if(obj == null){
			return "";
		}
		
		if(obj instanceof HashMap){
			HashMap map = (HashMap)obj;
			for(String key : (Set<String>)map.keySet()){
				Object value = map.get(key);
				String newkey = ("".equals(namespace) ? namespace : namespace + ".") + key;
				if(recursive_process(newkey, value) == null){
					return null;
				}
			}
		} else if(obj instanceof String){
			String value = (String)obj;
			System.out.println("recursive_process: " + namespace + " = " + this.getValue(namespace));
			return this.getValue(namespace);
		} else {
			System.out.println("recursive_process: obj is unknown");
			return null;
		}
		
		return obj;
	}
	
	private String columntoproperty(String src) {
		if(src == null) {
			System.out.println("columntoproperty src is null");
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(String i : src.toLowerCase().split("_")) {
			sb.append(i.substring(0,1).toUpperCase() + i.substring(1, i.length()));
		}
		return sb.substring(0,1).toLowerCase() + sb.substring(1, sb.length());
	}
}
