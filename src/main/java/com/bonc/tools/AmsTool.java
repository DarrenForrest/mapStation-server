package com.bonc.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.cxf.common.util.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.bonc.db.TxException;
import com.bonc.microapp.dao.F;
import com.show.api.ShowApiRequest;

public class AmsTool {
	
	//static public String C_Key 			= "abcdefgabcdefg78"; //第一版密码
	static public String C_Key 			= "abcdefg123defg9q";//第二版密码 2018-7-29
	static private String C_CharSet 		= "UTF-8";
	static private String C_IndexUrl 		= "http://10.208.230.98:7001/ams/slm/sso/login.jsp";
	static private String C_AuthCodeUrl 	= "http://10.208.230.98:7001/ams/servlet/VerificationCodeUtil";
	static private String C_LoginUrl 		= "http://10.208.230.98:7001/ams/loginAction.do?method=login";
	
	static private String C_SwitchUserUrl 		= "http://10.208.230.98:7001/ams/callAction.do?method=call&nextPage=/demo/switchUser.jsp";
	static private String C_SwitchLoginUrl 		= "http://10.208.230.98:7001/ams/ajaxAction.do?method=json&common=login&classes=loginServiceImpl&logintype=switch";
	static private String C_LogoutUrl 		= "http://10.208.230.98:7001/ams/ajaxAction.do?method=json&common=workloginout&classes=workLoginService";
	static private String C_GetworktimeUrl 	= "http://10.208.230.98:7001/ams/ajaxAction.do?method=json&common=getworktime&classes=workLoginService&workdatesub=";
	
	static private String C_MinIndexUrl 	= "http://10.208.230.98:7001/ams/callAction.do?method=call&nextPage=/ams/index.jsp";
	static private String C_queryhistoryinfo = "http://10.208.230.98:7001/ams/ajaxAction.do?method=json&classes=workLoginService&common=queryhistoryinfo";
	static private String C_queryLoginInfo = "http://10.208.230.98:7001/ams/ajaxAction.do?method=json&classes=workLoginService&common=queryLoginInfo";
	static private String C_querySigUserForLogin = "http://10.208.230.98:7001/ams/ajaxAction.do?method=json&common=querySigUserForLogin&classes=ljCoreService&userid=%s&type=loginAdd";
	
	static private String C_UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.4620.400 QQBrowser/9.7.13014.400\nQuery String Parameters\nview source\nview URL encoded";

	private HttpClient httpclient = null;
	private CookieStore cookieStore = null;
	private HttpContext localContext = null;
	private boolean isLogin = false;
	
	public AmsTool(){
		this.httpclient = new DefaultHttpClient();
		this.cookieStore = new BasicCookieStore();
		this.localContext = new BasicHttpContext();
		this.localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);		
		isLogin = false;
	}
	
	public void cleanCookie(){
		this.cookieStore.clear();
	}
	
	
	public static void main(String[] args) throws TxException, InterruptedException {
		AmsTool amsTool = new AmsTool();
		String userName = "";
		String password = "";

		
		String[] userNameArr = {"dfgx_liyg"};
		String[] passwordArr = {"p2p12345@"};
		//String[] userNameArr = {"dfgx_zhangjw","zhangjw","dh_wuhao","dh_subin","dfgx_wuhao","dfgx_subin"};
		//String[] passwordArr = {"bonc123!!","bonc123!!","bonc123!!","bonc123!!","Bonc123!!","Bonc123!!",};
		
		//String[] userNameArr = {"dfgx_liyg","dh_liyd","dh_jingchungu","dfgx_jingchungu","dh_wangna","dfgx_wangna"};
		//String[] passwordArr = {"p2p12345@","p2p12345@","bonc123!!","Bonc123!!","bonc123!!","Bonc123!!"};
		
		float workTime = 0;
		for(int i=0; i < userNameArr.length; i++){
			userName = userNameArr[i];
			password = passwordArr[i];
			
			boolean ret = false;
			ret = amsTool.login(userName, password);
			//System.out.println("login ret = " +ret);
			
			//Thread.sleep(1000*2);
			
			//ret = amsTool.workloginout(userName, password);
			//System.out.println("workloginout ret = " +ret);
			
			//Thread.sleep(1000*2);
			
			//workTime = amsTool.queryWorkTime(userName, password );
			System.out.println("queryWorkTime ret = " + workTime );
			
		}
	}
	
	//切换用户
	public boolean swithLogin(String userName, String password) throws TxException{
		String sHtml = null;
		Map mapHeader = new HashMap();
		
		mapHeader.put("Referer", 	AmsTool.C_SwitchUserUrl);
        mapHeader.put("User-Agent", AmsTool.C_UserAgent);
        
        
        
        
        //伪装IP，测试发现对端可以识别真实IP
        //mapHeader.put("X-FORWARDED-FOR", 	"10.208.234.146");
        //mapHeader.put("CLIENT-IP", 		"10.208.234.146");
        
        String encryptUserName="";
        String encryptPassowrd="";
        
		try {
			encryptUserName =  AESpkcs7paddingUtil.encrypt(userName, AmsTool.C_Key);
			encryptPassowrd =  AESpkcs7paddingUtil.encrypt(password, AmsTool.C_Key);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TxException(e.getMessage());
		}
        
		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("optrid",   encryptUserName)); 
		params.add(new BasicNameValuePair("password", encryptPassowrd));
		
		
		//登录系统
		showCookie("before switchLogin");
		sHtml = httpPost( localContext, AmsTool.C_SwitchLoginUrl, params, mapHeader, AmsTool.C_CharSet);
		showCookie("after switchLogin");
		
		System.out.println("switchLogin = \n" +sHtml);
		
		
		
		if(StringUtils.isEmpty(sHtml)){
			return false;
		}
		
		if(sHtml.endsWith("error.jsp") || sHtml.endsWith("sessionInvalid.jsp")){
			return false;
		}
		
		/* 
		if(sHtml.indexOf("nextPage=/ams/index.jsp")>0){
			this.isLogin = true;
			return true;
		}
		*/
		
		sHtml = httpPost(localContext, this.C_queryhistoryinfo , params, mapHeader, AmsTool.C_CharSet);
		System.out.println("C_queryhistoryinfo = " + sHtml);
		
		sHtml = httpPost(localContext, this.C_queryLoginInfo , params, mapHeader, AmsTool.C_CharSet);
		
		String queryUrl = String.format(this.C_querySigUserForLogin, userName);
		sHtml = httpPost(localContext, queryUrl , params, mapHeader, AmsTool.C_CharSet);		
		System.out.println("querySigUserForLogin =" + sHtml);
		
		if(sHtml.indexOf(userName) >0){
			this.isLogin = true;
			return true;
		}
	
		return true;
	}
	
	
	//登录
	public boolean login(String userName, String password) throws TxException{
		String sHtml = null;
		Map mapHeader = new HashMap();        
        
		
		showCookie("before index");
		sHtml = httpGet(localContext, AmsTool.C_IndexUrl, mapHeader, AmsTool.C_CharSet);
		showCookie("after index");
		
		
		mapHeader.put("Referer", 	AmsTool.C_IndexUrl);
        mapHeader.put("User-Agent", AmsTool.C_UserAgent);
        
        String authCodePath = ConfigUtil.getInstance().getValueByProperty("config/microapp.properties", "authCodePath");
        if(StringUtils.isEmpty(authCodePath)) {
        	throw new TxException("找不到验证码保存路径");
        }
        authCodePath += (OsUtils.getFileSeparator() + DateUtils.getNow("yyyy-MM-dd"));
        
        String path = authCodePath;
        java.io.File uploadPath = new java.io.File(path);
        System.out.println("uploadPath: " + uploadPath.getAbsolutePath());
        if ((!uploadPath.exists()) && (!uploadPath.isDirectory())) {
            uploadPath.mkdirs();
        }
        
        String fileName = DateUtils.getNow("HHmmss_SSS") + ".jpg";
        
        if(!httpGetFile(localContext, AmsTool.C_AuthCodeUrl,mapHeader, AmsTool.C_CharSet, fileName,path)) {
        	throw new TxException("下载验证码图片失败");
        }
        
        String authCodeImgPath = path + OsUtils.getFileSeparator() + fileName;        
        String authCodeBase64 = Base64Tool.fileToBase64(authCodeImgPath);
        
        System.out.println("authCodePath=" + authCodeImgPath);
        System.out.println("authCodeBase64=\n" + authCodeBase64);
        
    	String res=new ShowApiRequest("http://route.showapi.com/184-5",F.SHOWAPI_APP_ID,F.SHOWAPI_APP_KEY)
    			.addTextPara("img_base64",authCodeBase64)
    			.addTextPara("typeId","34")
    			.addTextPara("convert_to_jpg","0")
    			.addTextPara("needMorePrecise","0")
    		  .post();
    	
    	System.out.println("authCode = " + res);

/*    	//验证码识别服务返回的消息模板
    	{
    	    "showapi_res_error": "",
    	    "showapi_res_id": "96e58762bedf486fac588495e7980a7e",
    	    "showapi_res_code": 0,
    	    "showapi_res_body": {
    	        "Id": "show2719-68dc-d6ad-7e35-89b3-20fa39db",
    	        "Result": "hlpp",
    	        "ret_code": "0"
    	    }
    	}
*/    	
    	JSONObject json = JSONObject.fromObject(res);
    	if(json.getInt("showapi_res_code") != 0) {
    		throw new TxException( "验证码识别失败：" + json.getString("showapi_res_error"));
    	}
    	
    	String verificationcode=json.getJSONObject("showapi_res_body").getString("Result");//验证码
    	
    	System.out.println("验证码=" + verificationcode);
        
        
        
      //伪装IP，测试发现对端可以识别真实IP
        //mapHeader.put("X-FORWARDED-FOR", 	"10.208.234.146");
        //mapHeader.put("CLIENT-IP", 		"10.208.234.146");
        
        String encryptUserName="";
        String encryptPassowrd="";
        
		try {
			encryptUserName =  AESpkcs7paddingUtil.encrypt(userName, AmsTool.C_Key);
			encryptPassowrd =  AESpkcs7paddingUtil.encrypt(password, AmsTool.C_Key);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TxException(e.getMessage());
		}
        
		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("optrid",   encryptUserName)); 
		params.add(new BasicNameValuePair("password", encryptPassowrd));
		params.add(new BasicNameValuePair("verificationcode", verificationcode));
		params.add(new BasicNameValuePair("imageField.x", "37"));
		params.add(new BasicNameValuePair("imageField.y", "16"));
		
		
		//登录系统
		showCookie("before Login");
		sHtml = httpPost( localContext, AmsTool.C_LoginUrl, params, mapHeader, AmsTool.C_CharSet);
		showCookie("after Login");
		
		
		
		if(StringUtils.isEmpty(sHtml)){
			return false;
		}
		
		if(sHtml.endsWith("error.jsp") ){
			return false;
		}
		
		/* 
		if(sHtml.indexOf("nextPage=/ams/index.jsp")>0){
			this.isLogin = true;
			return true;
		}
		*/
		
		sHtml = httpPost(localContext, this.C_queryhistoryinfo , params, mapHeader, AmsTool.C_CharSet);
		System.out.println("C_queryhistoryinfo = " + sHtml);
		
		sHtml = httpPost(localContext, this.C_queryLoginInfo , params, mapHeader, AmsTool.C_CharSet);
		
		String queryUrl = String.format(this.C_querySigUserForLogin, userName);
		sHtml = httpPost(localContext, queryUrl , params, mapHeader, AmsTool.C_CharSet);		
		System.out.println("querySigUserForLogin =" + sHtml);
		
		if(sHtml.indexOf(userName) >0){
			this.isLogin = true;
			return true;
		}
		

		return false;
	}
	
	//签出
	public boolean workloginout(String userName,String password)throws TxException{
		this.cleanCookie();
		String sHtml = null;

		Map mapHeader = new HashMap(); 
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		
		if(!this.login(userName, password)){
			throw new TxException("登录失败");
		}
		
		showCookie("before Loginworkout x");
		sHtml = httpPost( localContext, AmsTool.C_LogoutUrl, params, null, AmsTool.C_CharSet);
		showCookie("after Loginworkout x");
		
		if(StringUtils.isEmpty(sHtml)){
			throw new TxException("签出失败，可能用户名密码错误 或 用户被锁定");
		}
		
		if(sHtml.indexOf("签出成功") > 0){
			return true;
		}
		return true;
	}
	
	//签出，通过切换用户方式
	public boolean workloginoutBySwitch(String userName,String password)throws TxException{
		
		String sHtml = null;

		Map mapHeader = new HashMap(); 
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		
		if(!this.swithLogin(userName, password)){
			throw new TxException("登录失败");
		}
		
		showCookie("before Loginworkout x");
		sHtml = httpPost( localContext, AmsTool.C_LogoutUrl, params, null, AmsTool.C_CharSet);
		showCookie("after Loginworkout x");
		
		if(StringUtils.isEmpty(sHtml)){
			throw new TxException("签出失败，可能用户名密码错误 或 用户被锁定");
		}
		
		if(sHtml.indexOf("签出成功") > 0){
			return true;
		}
		return true;
	}
	
	//查询工时
	public float queryWorkTime(String userName, String password)throws TxException{
		this.cleanCookie();
		
		String sHtml = null;
		String getWorkTimeUrl = C_GetworktimeUrl + DateUtils.getNow("yyyy-MM-dd");
			
		Map mapHeader = new HashMap();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		if(!this.workloginout(userName, password)){
			throw new TxException("签出操作失败");
		}

		sHtml = httpPost( localContext, getWorkTimeUrl, params, null, AmsTool.C_CharSet);
		
		if(StringUtils.isEmpty(sHtml)){
			throw new TxException("查询工时失败");
		}
		
		String workTime = "";
		if(sHtml.indexOf("jsondata") > 0){
			JSONObject json =  JSONObject.fromObject(sHtml);
			System.out.println("json = " + json);
			
			workTime = json.getJSONObject("jsondata").getString("content");
			System.out.println("workTime = " + workTime);
			
			String retDesc = "";
			retDesc = json.getJSONObject("jsondata").getString("retDesc");
			System.out.println("retDesc = " + retDesc);
			
			if(StringUtils.isEmpty(workTime) && !StringUtils.isEmpty(retDesc)){
				throw new TxException("查询工时失败," + retDesc);
			}else{
				return Float.valueOf(workTime);
			}
		}		
		return 0;
	}
	
	//查询工时，通过切换用户方式，避免验证码
	public float queryWorkTimeBySwitch(String userName, String password)throws TxException{
		
		String sHtml = null;
		String getWorkTimeUrl = C_GetworktimeUrl + DateUtils.getNow("yyyy-MM-dd");
			
		Map mapHeader = new HashMap();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		if(!this.workloginoutBySwitch(userName, password)){
			throw new TxException("签出操作失败");
		}

		sHtml = httpPost( localContext, getWorkTimeUrl, params, null, AmsTool.C_CharSet);
		
		if(StringUtils.isEmpty(sHtml)){
			throw new TxException("查询工时失败");
		}
		
		String workTime = "";
		if(sHtml.indexOf("jsondata") > 0){
			JSONObject json =  JSONObject.fromObject(sHtml);
			System.out.println("json = " + json);
			
			workTime = json.getJSONObject("jsondata").getString("content");
			System.out.println("workTime = " + workTime);
			
			String retDesc = "";
			retDesc = json.getJSONObject("jsondata").getString("retDesc");
			System.out.println("retDesc = " + retDesc);
			
			if(StringUtils.isEmpty(workTime) && !StringUtils.isEmpty(retDesc)){
				throw new TxException("查询工时失败," + retDesc);
			}else{
				return Float.valueOf(workTime);
			}
		}		
		return 0;
	}
	
	private String httpPost( HttpContext localContext, String url, List<NameValuePair> formparams, 	Map mapHeader, String sCharSet) throws TxException
	{
		long beginTime = System.currentTimeMillis();		
		System.out.println("lib_post begin : url=" + url);
		
		HttpClient client = new DefaultHttpClient();
		
	    try {
	        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, sCharSet );

	        HttpPost post = new HttpPost(url);
	        post.setEntity(entity);
	        
	       // post.setRequestHeader("Content-type", "text/xml; charset=GBK");
	        if(mapHeader != null )
	        {
	        	Set set =mapHeader.entrySet();       
	        	Iterator it=set.iterator();      
	        	while(it.hasNext())
	        	{          
	        		Map.Entry<String, String>  entry=(Map.Entry<String,String>)it.next();
	        		//System.out.println(entry.getKey()+":"+entry.getValue()); 
	        		post.addHeader(entry.getKey(), entry.getValue());
	        	}
	        }
	        
	        client.getParams().setIntParameter("http.socket.timeout",1000*10); 
	        
	        HttpResponse response = client.execute(post, localContext);
	        int iStatusCode = response.getStatusLine().getStatusCode();
	        
	        long netTime = System.currentTimeMillis() -   beginTime ;	        
	       
	        System.out.println("lib_post 耗时 : " +  netTime + " ; url=" + url);
	        
	        System.out.println("iStatusCode = " +  iStatusCode);
	        
	        if( iStatusCode != HttpStatus.SC_OK  &&
	            iStatusCode != HttpStatus.SC_MOVED_PERMANENTLY &&
	            iStatusCode != HttpStatus.SC_MOVED_TEMPORARILY  &&
	            iStatusCode != HttpStatus.SC_INTERNAL_SERVER_ERROR &&
	            iStatusCode != HttpStatus.SC_NOT_FOUND ) 
	        {
	        	System.out.println(response.getStatusLine().getStatusCode());
	            throw new RuntimeException(" http post error");
	        }
	        
	        if( iStatusCode == HttpStatus.SC_MOVED_TEMPORARILY  )
	        {
	        	System.out.println("302");
	        	HttpEntity resEntity =  response.getEntity();	               
	 	        String ret= (resEntity == null) ? null : EntityUtils.toString(resEntity, HTTP.UTF_8  );	        	
	 	        System.out.println("302 ret  = " + ret);
	 	       
	 	       Header header = response.getFirstHeader("location"); 
	 	       String newUri= header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
	 	       
	 	       System.out.println("new Uri = " + newUri);
	 	        
	 	        return newUri;
	        }
	        
	        if(  iStatusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR || 
	        	 iStatusCode == HttpStatus.SC_NOT_FOUND)
	        {
	        	return "服务器正在升级，请过段时间再查询 " + iStatusCode;
	        }
	        
	        HttpEntity resEntity =  response.getEntity();	               
	        return (resEntity == null) ? null : EntityUtils.toString(resEntity, HTTP.UTF_8  );	        
	        
	    } catch(SocketTimeoutException e){
	    	throw new TxException("网络超时");
	    }
	    catch(Exception e){
	    	throw new TxException(e.getMessage());
	    }
	}
	
	private String httpGet( HttpContext localContext, String url, Map mapHeader, String sCharSet) throws TxException
	{
		long beginTime = System.currentTimeMillis();		
		System.out.println("lib_post begin : url=" + url);
		
		HttpClient client = new DefaultHttpClient();
		
	    try {

	        HttpGet get = new HttpGet(url);
	       // post.setRequestHeader("Content-type", "text/xml; charset=GBK");
	        if(mapHeader != null )
	        {
	        	Set set =mapHeader.entrySet();       
	        	Iterator it=set.iterator();      
	        	while(it.hasNext())
	        	{          
	        		Map.Entry<String, String>  entry=(Map.Entry<String,String>)it.next();
	        		System.out.println(entry.getKey()+":"+entry.getValue()); 
	        		get.addHeader(entry.getKey(), entry.getValue());
	        	}
	        }
	        
	        client.getParams().setIntParameter("http.socket.timeout",1300*3); 	        
	        HttpResponse response = client.execute(get, localContext);
	        int iStatusCode = response.getStatusLine().getStatusCode(); 
	        
	        long netTime = System.currentTimeMillis() -   beginTime ;	        
	        //log.debug("lib_post 耗时 : " +  netTime + " ; url=" + url);
	        
	        if( iStatusCode != HttpStatus.SC_OK  &&
	            iStatusCode != HttpStatus.SC_MOVED_PERMANENTLY &&
	            iStatusCode != HttpStatus.SC_MOVED_TEMPORARILY  &&
	            iStatusCode != HttpStatus.SC_INTERNAL_SERVER_ERROR &&
	            iStatusCode != HttpStatus.SC_NOT_FOUND ) 
	        {
	        	System.out.println(response.getStatusLine().getStatusCode());
	        	throw new TxException("异常 返回码 " + iStatusCode);
	        }
	        
	        if( iStatusCode == HttpStatus.SC_MOVED_TEMPORARILY  )
	        {
	        	return " 跳转。。。。  " + iStatusCode;
	        }
	        
	        if(  iStatusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR || 
	        	 iStatusCode == HttpStatus.SC_NOT_FOUND)
	        {
	        	return "服务器正在升级，请过段时间再查询 " + iStatusCode;
	        }
	        
	        HttpEntity resEntity =  response.getEntity();	               
	        return (resEntity == null) ? null : EntityUtils.toString(resEntity, HTTP.UTF_8  );	        
	        
	    }catch(Exception e){
	    	throw new TxException(e.getMessage());
	    }
	}
	
	public void showCookie(String tip){
		System.out.println("\n---------begin ShowCookie " + tip);
		
		List list = cookieStore.getCookies();
		for(int i=0; list!=null && i<list.size(); i++){
			Cookie cookie = (Cookie) list.get(i);
			System.out.println("cookie [" + i + "]="  + cookie);
		}
	}
	
	public Boolean httpGetFile( HttpContext localContext, String url, Map mapHeader, String sCharSet,String fileName,String filePath) throws TxException
	{
		long beginTime = System.currentTimeMillis();		
		System.out.println("lib_getFile begin : url=" + url);
		
		HttpClient client = new DefaultHttpClient();
		
	    try {

	        HttpGet get = new HttpGet(url);
	       // post.setRequestHeader("Content-type", "text/xml; charset=GBK");
	        if(mapHeader != null )
	        {
	        	Set set =mapHeader.entrySet();       
	        	Iterator it=set.iterator();      
	        	while(it.hasNext())
	        	{          
	        		Map.Entry<String, String>  entry=(Map.Entry<String,String>)it.next();
	        		System.out.println(entry.getKey()+":"+entry.getValue()); 
	        		get.addHeader(entry.getKey(), entry.getValue());
	        	}
	        }
	        
	        client.getParams().setIntParameter("http.socket.timeout",1300*3); 	        
	        HttpResponse response = client.execute(get, localContext);
	        int iStatusCode = response.getStatusLine().getStatusCode(); 
	        
	        long netTime = System.currentTimeMillis() -   beginTime ;	        
	        System.out.println("getFile 耗时 : " +  netTime + " ; url=" + url);
	        
	        if( iStatusCode != HttpStatus.SC_OK  &&
	            iStatusCode != HttpStatus.SC_MOVED_PERMANENTLY &&
	            iStatusCode != HttpStatus.SC_MOVED_TEMPORARILY  &&
	            iStatusCode != HttpStatus.SC_INTERNAL_SERVER_ERROR &&
	            iStatusCode != HttpStatus.SC_NOT_FOUND ) 
	        {
	        	System.out.println(response.getStatusLine().getStatusCode());
	            throw new RuntimeException(" http post error");
	        }
	        
	        if( iStatusCode == HttpStatus.SC_MOVED_TEMPORARILY  )
	        {
	        	throw new TxException(" 跳转。。。。  " + iStatusCode);
	        }
	        
	        if(  iStatusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR || 
	        	 iStatusCode == HttpStatus.SC_NOT_FOUND)
	        {
	        	throw new TxException( "服务器正在升级，请过段时间再查询 " + iStatusCode);
	        }
	        
	        HttpEntity resEntity =  response.getEntity();
	        
	        File storeFile = new File( filePath + OsUtils.getFileSeparator() + fileName);     
            FileOutputStream output = new FileOutputStream(storeFile);     
            
            byte buf[] = EntityUtils.toByteArray(  resEntity);
            output.write(buf);	            
            output.close();   
            
            EntityUtils.consume(resEntity);
	        return true;       
	        
	    }catch(Exception e){
	    	e.printStackTrace();
	    	System.out.println(e.toString());
	        return null;
	    }
	}
}
