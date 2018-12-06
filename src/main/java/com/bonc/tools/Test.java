package com.bonc.tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String key = "abcdefgabcdefg78";
		
		String userName = "dfgx_liyg";//"dfgx_liyg";
		String password = "p2p12345@";// "p2p12345@";
		
		userName =  AESpkcs7paddingUtil.encrypt(userName, key);
		password =  AESpkcs7paddingUtil.encrypt(password, key);
		
		System.out.println("userName=" + userName);
		System.out.println("password=" + password);
		
		HttpTool tool = new HttpTool();
		
		String charSet = "UTF-8";
		String sIndexUrl = "http://10.208.219.22:7001/ams/slm/sso/login.jsp";
        String sLoginUrl = "http://10.208.219.22:7001/ams/loginAction.do?method=login";
		String sLogoutUrl = "http://10.208.219.22:7001/ams/ajaxAction.do?method=json&common=workloginout&classes=workLoginService";
        
		HttpClient httpclient = new DefaultHttpClient();
		CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        
        Map mapHeader = new HashMap();        
        mapHeader.put("Referer", "http://10.208.219.22:7001/ams/loginAction.do?method=login");
        mapHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.4620.400 QQBrowser/9.7.13014.400\nQuery String Parameters\nview source\nview URL encoded");
        
        
        String sHtml = null;
        
        if(false){
        	return;
        }
        
		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("optrid", userName)); 
		params.add(new BasicNameValuePair("password", password)); 
		
		//登录系统
		sHtml = tool.lib_post( localContext, sLoginUrl, params, mapHeader, charSet);
		
		System.out.println("登录结果 = " + sHtml);
		
		Thread.sleep(1000* 3);
		
		//签出系统 
		sHtml = tool.lib_post( localContext, sLogoutUrl, params ,null, charSet);
		
		System.out.println("签出结果 = " + sHtml);
		
	}

}
