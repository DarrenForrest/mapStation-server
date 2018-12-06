package com.bonc.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;


public class TheadUtil {
	private static ApplicationContext applicationContext;
	
	static {
		TheadUtil.applicationContext = ContextLoader.getCurrentWebApplicationContext();
	}
	
	public static Object getBean(String name){
		return applicationContext == null ? null : applicationContext.getBean(name);
	}
	
}
