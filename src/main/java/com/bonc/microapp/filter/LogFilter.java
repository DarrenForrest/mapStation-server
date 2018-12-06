package com.bonc.microapp.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bonc.common.Auth;
import com.bonc.common.CrontabTask;
import com.bonc.core.service.AuthorityService;
import com.bonc.microapp.service.JobDefineService;


public class LogFilter implements Filter {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private JobDefineService jobDefineService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {		
		System.out.println("LogFilter: init");
		
		//因Weblogc不能从静态函数中取得ApplicationContext，故在此处做初始化
		if(CrontabTask.getApplicationContext() == null) {
			CrontabTask.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()));
		}
		
		System.out.println("定时器不自动启动，由页面按钮触发，需要时再配置");
//		if(!this.jobDefineService.isRunning()) {
//			if(this.jobDefineService.start()) {
//				System.out.println("定时器启动成功！");
//			} else {
//				System.out.println("定时器启动失败！");
//			}
//		} else {
//			System.out.println("定时器已经在运行！");
//		}
	}
	
	@Override
	public void destroy() {
		System.out.println("LogFilter: destroy");
		if(this.jobDefineService.isRunning()) {
			if(this.jobDefineService.stop()) {
				System.out.println("定时器停止成功！");
			} else {
				System.out.println("定时器停止失败！");
			}
		} else {
			System.out.println("定时器已经停止！");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest  req  = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse) response;  
		
		System.out.println("[" + new Date().toString() + "] doFilter: ----------[" + req.getRequestURI() + "]---[" + req.getRequestURL() + "]");
		
		//判断白名单
		String url = req.getServletPath();
		if( isInWhiteList( url))
		{
			System.out.println("在白名单内");
			filterChain.doFilter(request, response);
			return;
		}
		
		//登录会话失效，跳转到登录页面
		if(Auth.getAuth(req) == null) {
			String path = req.getContextPath();  
			String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path;  
			
			resp.setHeader("Cache-Control", "no-store");  
            resp.setDateHeader("Expires", 0);  
            resp.setHeader("Prama", "no-cache");  
            resp.sendRedirect(basePath+"/login.action");
            
            return;
		}
		
		if(this.authorityService.checkAuth(req)) {
			//System.out.println("该员工拥有此权限：" + request.getRequestURI());
			filterChain.doFilter(request, response);
		} else {
			System.out.println("该员工没有此权限：" + req.getRequestURI());
			{
				filterChain.doFilter(request, response);
				if(true)
					return;
			}
			
			String path = req.getContextPath();  
			String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path;  
			
			resp.setHeader("Cache-Control", "no-store");  
            resp.setDateHeader("Expires", 0);  
            resp.setHeader("Prama", "no-cache");  
            resp.sendRedirect(basePath+"/login.action");  
		}
		
	}

	private Boolean isInWhiteList(String url)
	{	
		if(false){
			return true;
		}
		
		List<String> whiteList = new ArrayList<String>();
		whiteList.add("/login.action");
		whiteList.add("/loginJson.action");
		whiteList.add("/logout.action");
		whiteList.add("/main.action"); //main.action 中自行判断是否登录		
		whiteList.add("/testGetJson.action");
		
		return whiteList.contains(url);
	}

}
