package com.bonc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
 
 public class Validator
 {
   public static boolean isNull(Object[] object)
   {
     return (object == null) || (object.length == 0);
   }
 
   public static String isNull(String str, String defaultStr)
   {
     if (!isNull(str))
       return defaultStr;
     return str;
   }
 
   public static boolean isNull(String str)
   {
     if (str == null) {
       return true;
     }
     str = str.trim();
 
     return (str.equals("null")) || (str.equals(""));
   }
 
   public static boolean isNotNull(String str)
   {
     return !isNull(str);
   }
 
   public static String getDefaultStr(String text, String defaultStr)
   {
     if (isNull(text))
       return defaultStr;
     return text;
   }
 
   public static boolean isEmpty(Object obj)
   {
     return obj == null;
   }
 
   public static boolean isEmpty(List list)
   {
     return (list == null) || (list.size() == 0);
   }
 
   public static boolean isEmpty(Collection collection)
   {
     return (collection == null) || (collection.isEmpty());
   }
 
   public static int getStrByteLength(String str)
   {
     if (isNull(str)) {
       return 0;
     }
     return str.getBytes().length;
   }
 
   public static Properties getProperties(String filePath)
   {
     Properties prop = null;
     FileInputStream fileInput = null;
     try {
       File file = new File(filePath);
       if (file.exists()) {
         prop = new Properties();
         fileInput = new FileInputStream(filePath); 
         InputStreamReader isr=new InputStreamReader(fileInput,"UTF-8"); //字节流和字符流的桥梁，可以指定指定字符格式
         prop.load(isr);
       }
     } catch (IOException e) {
       System.out.println("文件读取失败 key=" + e.getMessage());
       try
       {
         if (!isEmpty(prop))
           fileInput.close();
       } catch (Exception ex) {
         System.out.println("文件流关闭失败 key=" + ex.getMessage());
       }
     }
     finally
     {
       try
       {
         if (!isEmpty(prop))
           fileInput.close();
       } catch (Exception e) {
         System.out.println("文件流关闭失败 key=" + e.getMessage());
       }
     }
     return prop;
   }
 
   public static String getClassLoaderPath()
   {
     String path = "";
     URL classLoaderUrl = Thread.currentThread().getContextClassLoader().getResource("");
     if (isEmpty(classLoaderUrl))
       path = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
     else {
       path = classLoaderUrl.getPath();
     }
     if (isNotNull(path)) {
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
 
   public static String getSystemType()
   {
     String result = "windows";
     if (System.getProperty("os.name").equals("Linux"))
       result = "linux";
     else if ("Mac OS X".equals(System.getProperty("os.name"))) {
       result = "Mac OS X";
     }
     return result;
   }
 }
