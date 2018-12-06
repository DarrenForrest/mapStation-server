package com.bonc.tools;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {
	
	/**
	 * @Title 格式化模板字符串
	 * @param template 模板内容，模板变量格式为${Variable}
	 * @param args     外部参数，在解析变量时要用到的外部参数，可选
	 * @return String  经过替换后的完整内容
	 */
	public static String format(String template, Object...args) {
		Pattern pattern = Pattern.compile("\\$\\{\\w+(\\.\\w+)*\\}");
		Matcher matcher = pattern.matcher(template);
		String res = template;
		while(matcher.find()) {
			String str = matcher.group();
			String key = str.substring(2, str.length() - 1);
			//System.out.println(str + " = " + get(key, args));
			Object value = get(key, args);
			res = res.replaceAll("\\$\\{" + key + "\\}", value != null ? value.toString() : "null");
		}
		
		return res;
	}
	
	/**
	 * @Title 模板变量注册函数
	 * @param key         模板中使用的变量名
	 * @param instance    类实例，变量方法所属类定义的实例
	 * @param methodName  变量方法名，必须和所定义的函数名一致
	 * @param description 变量名描述
	 * @return true 注册成功，且可通过listVariables查询到；false：注册失败
	 * @Description
	 *     定义本地变量获取方法时有且只能有一个可变参数，如下示例
	 *     public Object getMyVariable(Object...args) { return "hello" }
	 * 
	 */
	public static boolean regist(String key, Object instance, String methodName, String description) {
		try {
			Class<?>[] parameterTypes = new Class<?>[1];
			parameterTypes[0] = Object[].class;
			Method method = instance.getClass().getMethod(methodName, parameterTypes);
			map.put(key, new Object[]{instance, method, description});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @Title 返回所有已注册的模板变量列表
	 * @Description 格式 [变量名：描述]
	 */
	public static List<String> listVariables() {
		List<String> res = new ArrayList<String>();
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()) {
			String str = it.next().toString();
			str += ": " + ((Object[])map.get(str))[2];
			res.add(str);
		}
		
		return res;
	}
	
	private static Map<String, Object[]> map;
	
	static {
		map = new HashMap<String, Object[]>();
	}
	
	private static Object get(String key, Object... args) {
		
		String[] keys = key.split("\\.");
		Object res = "undefined";
		
		Object[] obj = map.get(keys[0]);
		if(obj != null && obj.length == 3) {
			try {
				//如此处传参args不做Object强转会报出wrong number of arguments错误
				//原因是args为Object[]会解析成不定参数个数，而我们定义函数只需要一个参数
				res = ((Method)obj[1]).invoke(obj[0], (Object)args);
				if(keys.length == 1) {
					return res;
				} else if(keys.length == 2) {
					if(res instanceof Map) {
						return ((Map<?, ?>)res).get(keys[1]);
					} else {
						String methodName = "get" + keys[1].substring(0, 1).toUpperCase() + keys[1].substring(1);
						try {
							Method method = res.getClass().getMethod(methodName);
							return method.invoke(res);
						} catch (Exception e) {
							System.out.println(res.getClass().getName() + "." + methodName + ": Error " + e);
						}
					}
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}
		
		return "undefined";
	}
}
