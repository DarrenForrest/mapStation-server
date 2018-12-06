package com.bonc.tools;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bonc.db.TxException;

public class HttpTool {
	Log log=LogFactory.getLog(this.getClass());
	
	public String lib_post( HttpContext localContext, String url, 
			List<NameValuePair> formparams, 	Map mapHeader, String sCharSet) 
	{
		long beginTime = System.currentTimeMillis();		
		log.debug("lib_post begin : url=" + url);
		
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
	        		System.out.println(entry.getKey()+":"+entry.getValue()); 
	        		post.addHeader(entry.getKey(), entry.getValue());
	        	}
	        }
	        
	        client.getParams().setIntParameter("http.socket.timeout",1300*3); 
	        
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
	 	        
	 	        return ret;
	        }
	        
	        if(  iStatusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR || 
	        	 iStatusCode == HttpStatus.SC_NOT_FOUND)
	        {
	        	return "服务器正在升级，请过段时间再查询 " + iStatusCode;
	        }
	        
	        HttpEntity resEntity =  response.getEntity();	               
	        return (resEntity == null) ? null : EntityUtils.toString(resEntity, HTTP.UTF_8  );	        
	        
	    } catch (UnsupportedEncodingException e) {
	    	System.out.println(e.toString());
	        return null;
	    } catch (ClientProtocolException e) {
	    	System.out.println(e.toString());
	        return null;
	    } catch (IOException e) {
	    	System.out.println(e.toString());
	        return null;
	    }catch(Exception e){
	    	System.out.println(e.toString());
	        return null;
	    }
	}
	
	public String lib_get( HttpContext localContext, String url, Map mapHeader, String sCharSet) 
	{
		long beginTime = System.currentTimeMillis();		
		log.debug("lib_get begin : url=" + url);
		
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
	        log.debug("lib_post 耗时 : " +  netTime + " ; url=" + url);
	        
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
	    	System.out.println(e.toString());
	        return null;
	    }
	}

	public Boolean lib_getFile( HttpContext localContext, String url, Map mapHeader, String sCharSet,String fileName,String filePath) throws TxException
	{
		long beginTime = System.currentTimeMillis();		
		log.debug("lib_getFile begin : url=" + url);
		
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
	        log.debug("lib_post 耗时 : " +  netTime + " ; url=" + url);
	        
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
	        
	        File storeFile = new File( filePath + fileName);     
            FileOutputStream output = new FileOutputStream(storeFile);     
            
            byte buf[] = EntityUtils.toByteArray(  resEntity);
            output.write(buf);	            
            output.close();   
            
            EntityUtils.consume(resEntity);
	        return true;       
	        
	    }catch(Exception e){
	    	System.out.println(e.toString());
	        return null;
	    }
	}

}
