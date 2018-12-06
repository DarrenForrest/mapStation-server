package com.bonc.microapp.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.bonc.core.entity.BigData;
import com.bonc.core.service.AuthorityService;
import com.bonc.db.TxException;
import com.bonc.tools.DateUtils;
import com.bonc.tools.ParamVo;

public class WebServiceFilter implements Filter {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		System.out.println("WebServiceFilter: " + request);
		
		{ //写日志
			String requestDetail = getRequestDetails(request);
			int length = request.getContentLength();
			if (length > 0) {
				BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper((HttpServletRequest) request, length);
				requestDetail += "\n" + this.getContent(bufferedRequest, "UTF-8");
				request = bufferedRequest;
			}
			System.out.println(requestDetail);
			
			ParamVo vo = new ParamVo();
			BigData bigData = new BigData();
			bigData.setId(-1L);
			bigData.setType("string");
			bigData.setMsg(requestDetail);
			bigData.setModifyDate(DateUtils.getDate());
			bigData.setCreateDate(DateUtils.getDate());
			vo.setObject(bigData);
			try {
				this.authorityService.txInsertBigData(vo);
			} catch (TxException e) {
				e.printStackTrace();
			}
		}
		
		chain.doFilter(request, response);
		
		System.out.println("WebServiceFilter: " + response);
	}

	@Override
	public void destroy() {
		
	}
	
	private String getRequestDetails(ServletRequest request) {
		StringBuffer msg = new StringBuffer();
		msg.append("\n=============================================");
		msg.append("\nRequest Received at " + new Date());
		msg.append("\n characterEncoding = " + request.getCharacterEncoding());
		msg.append("\n     contentLength = " + request.getContentLength());
		msg.append("\n       contentType = " + request.getContentType());
		msg.append("\n            locale = " + request.getLocale());
		msg.append("\n           locales = ");

		boolean first = true;
		for (Enumeration<?> locales = request.getLocales(); locales
				.hasMoreElements();) {
			Locale locale = (Locale) locales.nextElement();
			if (first)
				first = false;
			else
				msg.append(", ");
			msg.append(locale.toString());
		}

		for (Enumeration<?> names = request.getParameterNames(); names
				.hasMoreElements();) {
			String name = (String) names.nextElement();
			msg.append("         parameter = " + name + " = ");
			String values[] = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++) {
				if (i > 0)
					msg.append(", ");
				msg.append(values[i]);
			}
		}
		msg.append("\n          protocol = " + request.getProtocol());
		msg.append("\n        remoteAddr = " + request.getRemoteAddr());
		msg.append("\n        remoteHost = " + request.getRemoteHost());
		msg.append("\n            scheme = " + request.getScheme());
		msg.append("\n        serverName = " + request.getServerName());
		msg.append("\n        serverPort = " + request.getServerPort());
		msg.append("\n          isSecure = " + request.isSecure());

		// Render the HTTP servlet request properties
		if (request instanceof HttpServletRequest) {
			msg.append("\n---------------------------------------------");
			HttpServletRequest hrequest = (HttpServletRequest) request;
			msg.append("\n       contextPath = " + hrequest.getContextPath());
			Cookie cookies[] = hrequest.getCookies();
			if (cookies == null)
				cookies = new Cookie[0];
			for (int i = 0; i < cookies.length; i++) {
				msg.append("\n            cookie = " + cookies[i].getName()
						+ " = " + cookies[i].getValue());
			}
			for (Enumeration<?> names = hrequest.getHeaderNames(); names
					.hasMoreElements();) {
				String name = (String) names.nextElement();
				String value = hrequest.getHeader(name);
				msg.append("\n            header = " + name + " = " + value);
			}
			msg.append("\n            method = " + hrequest.getMethod());
			msg.append("\n          pathInfo = " + hrequest.getPathInfo());
			msg.append("\n       queryString = " + hrequest.getQueryString());
			msg.append("\n        remoteUser = " + hrequest.getRemoteUser());
			msg.append("\nrequestedSessionId = "
					+ hrequest.getRequestedSessionId());
			msg.append("\n        requestURI = " + hrequest.getRequestURI());
			msg.append("\n       servletPath = " + hrequest.getServletPath());
		}
		msg.append("\n=============================================");

		return msg.toString();
	}
	
	private String getContent(BufferedRequestWrapper bufferedRequest, String charsetName) throws IOException {
		InputStream is = bufferedRequest.getInputStream();
		byte[] content = new byte[bufferedRequest.getContentLength()];

		int pad = 0;
		while (pad < bufferedRequest.getContentLength()) {
			pad += is.read(content, pad, bufferedRequest.getContentLength());
		}
		return new String(content, charsetName);
	}
}
