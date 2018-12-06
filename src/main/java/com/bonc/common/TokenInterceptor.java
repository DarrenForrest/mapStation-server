package com.bonc.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenInterceptor extends HandlerInterceptorAdapter {
	
	private static final String token_field = "token";
	private static final String token_description = "active";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Token annotation = method.getAnnotation(Token.class);
			if (annotation != null) {
				boolean needSaveSession = annotation.save();
				if (needSaveSession) {
					String token = UUID.randomUUID().toString();
					getTokenMap(request).put(token, token_description);
					request.setAttribute("token", token);
				}
				boolean needRemoveSession = annotation.remove();
				if (needRemoveSession) {
					if (isRepeatSubmit(request)) {
						return false;
					}
					getTokenMap(request).remove(getClientToken(request));
				}
			}
			return true;
		} else {
			return super.preHandle(request, response, handler);
		}
	}
	
	public static void restoreToken(HttpServletRequest request) {
		getTokenMap(request).put(getClientToken(request), token_description);
	}
	
	private boolean isRepeatSubmit(HttpServletRequest request) {
		HashMap<String, String> map = getTokenMap(request);
		String token = getClientToken(request);
		String tokenServer = map.get(token);
		if(tokenServer != null && tokenServer.equals(token_description)) {
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static HashMap<String, String> getTokenMap(HttpServletRequest request) {
		HashMap<String, String> hashSet = (HashMap<String, String>)request.getSession(false).getAttribute(token_field);
		if(hashSet == null) {
			hashSet = new HashMap<String, String>();
			request.getSession(false).setAttribute(token_field, hashSet);
		}
		return hashSet;
	}
	
	private static String getClientToken(HttpServletRequest request) {
		return request.getParameter(token_field) == null ? "" : request.getParameter(token_field);
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Token {
		boolean save() default false;
		boolean remove() default false;
	}
}
