 package com.bonc.tools;
 
 import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
 
 public class ConfigUtil
 {
   private Map<String, Long> lastUpateTime = new HashMap();
   private Map<String, Map<String, String>> constant = new HashMap();
   private static ConfigUtil instance;
   private String filePath = "";
 
   private ConfigUtil() {
     this.filePath = getClassLoaderPath();
   }
 
   public static ConfigUtil getInstance() {
     if (instance == null) {
       instance = new ConfigUtil();
     }
     return instance;
   }
 
   private String initConfig(String key, String fileName)
   {
     File oldfile = new File(this.filePath + fileName);
     long lastModletime = oldfile.lastModified();
     Long lastTime = (Long)this.lastUpateTime.get(fileName);
     if ((lastTime == null) || (lastTime.longValue() < lastModletime)) {
       this.lastUpateTime.put(fileName, Long.valueOf(lastModletime));
       Properties prop = getProperties(this.filePath + fileName);
       if(prop == null) {
    	   prop = getWebLogicProperties(fileName);
    	   System.out.println("ConfigUtil.initConfig.getWebLogicProperties: " + fileName);
       }
       Map map = new HashMap(prop);
       Set propertySet = map.entrySet();
       Map param = new HashMap();
       for (Iterator localIterator = propertySet.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
         Map.Entry entry = (Map.Entry)o;
         param.put(entry.getKey().toString(), entry.getValue().toString());
       }
       this.constant.put(fileName, param);
     }
     if (StrUtil.isNull(key)) {
       return "";
     }
     return StrUtil.doNull((String)((Map)this.constant.get(fileName)).get(key), "");
   }
 
   public String getValueByProperty(String propFilePath, String key)
   {
     return initConfig(key, propFilePath);
   }
 
   public Map<String, String> getAllProperty(String propFilePath)
   {
     initConfig("", propFilePath);
     return (Map)this.constant.get(propFilePath);
   }
 
   public Properties getProperties(String filePath)
   {
     Properties prop = null;
     FileInputStream fileInput = null;
     try {
       File file = new File(filePath);
       if (file.exists()) {
         prop = new Properties();
         fileInput = new FileInputStream(filePath);
         prop.load(fileInput);
       }
     } catch (IOException e) {
       System.out.println("文件读取失败 key=" + e.getMessage());
       try
       {
         if (prop != null)
           fileInput.close();
       } catch (Exception ex) {
         System.out.println("文件流关闭失败 key=" + ex.getMessage());
       }
     }
     finally
     {
       try
       {
         if (prop != null)
           fileInput.close();
       } catch (Exception e) {
         System.out.println("文件流关闭失败 key=" + e.getMessage());
       }
     }
     return prop;
   }
 
   private String getClassLoaderPath()
   {
     String path = "";
     URL classLoaderUrl = Thread.currentThread().getContextClassLoader().getResource("");
     if (classLoaderUrl != null)
       path = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
     else {
       path = classLoaderUrl.getPath();
     }
     if (StrUtil.isNotNull(path)) {
       if ((path.startsWith(File.separator)) || (path.startsWith("/"))) {
         String systemType = getSystemType();
         if ("windows".equals(systemType))
           path = path.substring(1);
       }
       if ((!path.endsWith(File.separator)) && (!path.endsWith("/")))
         path = path + "/";
     }
     return path;
   }
 
   private String getSystemType()
   {
     String result = "windows";
     if (System.getProperty("os.name").equals("Linux"))
       result = "linux";
     else if ("Mac OS X".equals(System.getProperty("os.name"))) {
       result = "Mac OS X";
     }
     return result;
   }
   
	private Properties getWebLogicProperties(String filePath) {
		String jarFilePath = this.getClassLoaderPath() + "../lib/_wl_cls_gen.jar";
		Properties prop = null;
		try {
			JarFile jarFile = new JarFile(jarFilePath);
			JarEntry entry = jarFile.getJarEntry(filePath);
			prop = new Properties();
			prop.load(jarFile.getInputStream(entry));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
 }

