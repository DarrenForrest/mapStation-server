package com.bonc.tools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
 
 public class DateUtils
 {
   private static String defaultDatePattern = "yyyy-MM-dd";
   public static final long oneDayMillSeconds = 86400000L;
   
   /**
    * 返回上月最后一天，返回格式为字符串 yyyy-MM-dd
    * @return
    */
   public static String getPreviousMonthLastDay(){
	   String strToday = getNow("yyyy-MM-dd");
	   String strFirstDayOfMonth = strToday.substring(0,8) + "01";
		
	   Date d = strToDate(strFirstDayOfMonth, DateUtils.FmtStr.yyyyMMdd);
	   Date y = before(d, 86400000L);
	   
	   String strY= format(y, "yyyy-MM-dd");
	   return strY;
   }
 
   public static String getDatePattern()
   {
     return defaultDatePattern;
   }
 
   public static String getToday()
   {
     java.util.Date today = new java.util.Date();
     return format(today);
   }
   
   public static Calendar getDateOfLastMonth(Calendar date) {  
	    Calendar lastDate = (Calendar) date.clone();  
	    lastDate.add(Calendar.MONTH, -1);  
	    return lastDate;  
	}  
	  
	public static Calendar getDateOfLastMonth(String dateStr) {  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
       try {
			Date date = sdf.parse(dateStr);  
			Calendar c = Calendar.getInstance();  
			c.setTime(date);  
			return getDateOfLastMonth(c);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Invalid date format(yyyy-MM-dd): " + dateStr);  
		} 
	        
	}  
 
   public static String format(java.util.Date date)
   {
     return date == null ? "" : format(date, getDatePattern());
   }
 
   public static String format(java.util.Date date, String pattern)
   {
     return date == null ? "" : new SimpleDateFormat(pattern).format(date);
   }
 
   public static java.util.Date parse(String strDate)
     throws ParseException
   {
     return StringUtils.isBlank(strDate) ? null : parse(strDate, 
       getDatePattern());
   }
 
   public static java.util.Date parse(String strDate, String pattern)
     throws ParseException
   {
     return StringUtils.isBlank(strDate) ? null : 
       new SimpleDateFormat(pattern).parse(strDate);
   }
 
   public static java.util.Date getDate(int year, int month, int day)
   {
     Calendar cal = Calendar.getInstance();
     cal.set(year, month - 1, day, 0, 0, 0);
     return cal.getTime();
   }
 
   public static boolean isEndOfTheMonth(java.util.Date date)
   {
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
     int maxDay = cal.getActualMaximum(5);
     return cal.get(5) == maxDay;
   }
 
   public static boolean isEndOfTheYear(java.util.Date date)
   {
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
 
     return (11 == cal.get(2)) && 
       (31 == cal.get(5));
   }
 
   public static int getLastDayOfTheMonth(java.util.Date date)
   {
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
     return cal.getActualMaximum(5);
   }
 
   public static boolean isStartBeforeEndTime(java.util.Date startTime, java.util.Date endTime)
   {
     Assert.notNull(startTime, "StartTime is null");
     Assert.notNull(endTime, "EndTime is null");
     return startTime.getTime() < endTime.getTime();
   }
 
   public static long comparisonDifferenceDays(java.util.Date startTime, java.util.Date endTime)
     throws ParseException
   {
     Assert.notNull(startTime, "StartTime is null");
     Assert.notNull(endTime, "EndTime is null");
 
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(startTime);
     long timethis = calendar.getTimeInMillis();
     calendar.setTime(endTime);
     long timeend = calendar.getTimeInMillis();
     long theday = (timeend - timethis) / 86400000L;
     return theday;
   }
 
   public static boolean isStartOfTheMonth(java.util.Date date)
   {
     Assert.notNull(date);
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
     return 1 == cal.get(5);
   }
 
   public static boolean isStartOfTheYear(java.util.Date date)
   {
     Assert.notNull(date);
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
     return (1 == cal.get(2)) && (1 == cal.get(5));
   }
 
   public static int getMonth(java.util.Date date)
   {
     Assert.notNull(date);
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
     return cal.get(2);
   }
 
   public static int getYear(java.util.Date date)
   {
     Assert.notNull(date);
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
     return cal.get(1);
   }
 
   public static java.util.Date getSystemDate()
   {
     Calendar cal = Calendar.getInstance();
     cal.set(11, 0);
     cal.set(12, 0);
     cal.set(13, 0);
     cal.set(14, 0);
     return new java.sql.Date(cal.getTime().getTime());
   }
 
   public static Timestamp getSystemTimestamp()
   {
     return new Timestamp(System.currentTimeMillis());
   }
 
   public static java.util.Date before(java.util.Date date, long millSeconds)
   {
     return fromLong(date.getTime() - millSeconds);
   }
 
   public static java.util.Date after(java.util.Date date, long millSeconds)
   {
     return fromLong(date.getTime() + millSeconds);
   }
 
   public static java.util.Date after(java.util.Date date, int nday)
   {
     return fromLong(date.getTime() + nday * 86400000L);
   }
 
   public static java.util.Date afterNDays(int n)
   {
     return after(getDate(), n * 86400000L);
   }
 
   public static java.util.Date beforeNDays(int n)
   {
     return beforeNDays(getDate(), n);
   }
 
   public static java.util.Date beforeNDays(java.util.Date date, int n)
   {
     return fromLong(date.getTime() - n * 86400000L);
   }
 
   public static java.util.Date yesterday()
   {
     return before(getDate(), 86400000L);
   }
 
   public static java.util.Date tomorrow()
   {
     return after(getDate(), 86400000L);
   }
 
   public static long getA_B(java.util.Date dateA, java.util.Date dateB) {
     return dateA.getTime() - dateB.getTime();
   }
 
   public static java.util.Date getDate()
   {
     return Calendar.getInstance().getTime();
   }
 
   public static long toLong(java.util.Date date)
   {
     return date.getTime();
   }
 
   public static java.util.Date fromLong(long time)
   {
     java.util.Date date = getDate();
     date.setTime(time);
 
     return date;
   }
 
   public static java.util.Date strToDate(String dateStr, FmtStr fmtStr)
   {
     DateFormat df = new SimpleDateFormat(fmtStr.toString());
     try {
       return df.parse(dateStr);
     } catch (ParseException e) {
       
     }return null;
   }
 
   public static java.util.Date strToDate(String dateStr, String fmtStr)
   {
     DateFormat df = new SimpleDateFormat(fmtStr);
     try {
       return df.parse(dateStr);
     } catch (ParseException e) {
       
     }return null;
   }
 
   public static String longToStr(long time, FmtStr fmtStr)
   {
     return format(fromLong(time), fmtStr.toString());
   }
 
   public static long strToLong(String dateStr, FmtStr fmtStr)
   {
     return strToDate(dateStr, fmtStr).getTime();
   }
 
   public static String getTimeZoneOfSystem()
   {
     Properties sysProp = new Properties(System.getProperties());
     String sysTimeZone = sysProp.getProperty("user.timezone");
     return sysTimeZone;
   }
 
   public static String getTimeZoneOfJVM()
   {
     String jvmTimeZone = TimeZone.getDefault().getID();
     return jvmTimeZone;
   }
 
   public static boolean checkTimeZone()
   {
     String sysTimeZone = getTimeZoneOfSystem();
     String jvmTimeZone = getTimeZoneOfJVM();
     return sysTimeZone == null ? false : sysTimeZone.equals(jvmTimeZone);
   }
 
   public static String getNow()
   {
     SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
     return simp.format(new java.util.Date());
   }
   
   public static String getNow(String pattern)
   {
     SimpleDateFormat simp = new SimpleDateFormat(pattern);
     return simp.format(new java.util.Date());
   }
 
   public static String getDate(String date)
   {
     String result = "";
     if (StringUtils.isNotEmpty(date)) {
       result = date.substring(0, 10);
     }
     return result;
   }
 
   public static String getTime(String date)
   {
     String result = "";
     if (StringUtils.isNotEmpty(date)) {
       result = date.substring(0, 19);
     }
     return result;
   }
 
   public static String getMonth(String date)
   {
     String result = "";
     if (StringUtils.isNotEmpty(date)) {
       result = date.substring(0, 7);
     }
     return result;
   }
 
   public static int getCurrentYear()
   {
     Calendar cal = Calendar.getInstance();
     return cal.get(1);
   }
 
   public static int getCurrentMonth()
   {
     Calendar cal = Calendar.getInstance();
     return cal.get(2) + 1;
   }
 
   public static String getNowMonth()
   {
     int month = getCurrentMonth();
     if (month < 10) {
       return getCurrentYear() + "0" + month;
     }
     return getCurrentYear() + month + "";
   }
 
   public static int getCurrentDay()
   {
     Calendar cal = Calendar.getInstance();
     return cal.get(5);
   }
   public static Long getLongDate(String date) {
     Long result = Long.valueOf(0L);
     try {
       SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
       java.util.Date da = simp.parse(date);
       result = Long.valueOf(da.getTime());
     }
     catch (ParseException e) {
       e.printStackTrace();
     }
     return result;
   }
 
   public static class FmtStr
   {
     private String fmtStr;
     public static FmtStr yyyy = new FmtStr("yyyy");
     public static FmtStr yyyyMM = new FmtStr("yyyy-MM");
     public static FmtStr yyyyMMdd = new FmtStr("yyyy-MM-dd");
     public static FmtStr yyyyMMdd_HH = new FmtStr("yyyy-MM-dd HH");
     public static FmtStr yyyyMMdd_HHmm = new FmtStr("yyyy-MM-dd HH:mm");
     public static FmtStr yyyyMMdd_HHmmss = new FmtStr("yyyy-MM-dd HH:mm:ss");
     public static FmtStr yyyyMMdd_HHmmssSSS = new FmtStr("yyyy-MM-dd HH:mm:ss:SSS");
 
     public static FmtStr HHmm = new FmtStr("HH:mm");
     public static FmtStr HHmmss = new FmtStr("HH:mm:ss");
     public static FmtStr hhmmssSSS = new FmtStr("HH:mm:ss:SSS");
 
     public static FmtStr CN_yyyyMMdd = new FmtStr("yyyy年MM月dd日");
     public static FmtStr CN_HHmmss = new FmtStr("HH时mm分ss秒");
     public static FmtStr CN_yyyyMMdd_HHmmss = new FmtStr("yyyy年MM月dd日 HH时mm分ss秒");
 
     private FmtStr(String str)
     {
       this.fmtStr = str;
     }
 
     public String toString() {
       return this.fmtStr;
     }
   }
 
   public static class Now
   {
     public static String fmtNow(DateUtils.FmtStr fmtStr)
     {
       return DateUtils.format(DateUtils.getDate(), fmtStr.toString());
     }
 
     public static long toLong()
     {
       return DateUtils.getDate().getTime();
     }
 
     public static String fmt_yyyy()
     {
       return fmtNow(DateUtils.FmtStr.yyyy);
     }
 
     public static String fmt_yyyyMM()
     {
       return fmtNow(DateUtils.FmtStr.yyyyMM);
     }
 
     public static String fmt_yyyyMMdd()
     {
       return fmtNow(DateUtils.FmtStr.yyyyMMdd);
     }
 
     public static String fmt_yyyyMMdd_HH()
     {
       return fmtNow(DateUtils.FmtStr.yyyyMMdd_HH);
     }
 
     public static String fmt_yyyyMMdd_HHmm()
     {
       return fmtNow(DateUtils.FmtStr.yyyyMMdd_HHmm);
     }
 
     public static String fmt_yyyyMMdd_HHmmss()
     {
       return fmtNow(DateUtils.FmtStr.yyyyMMdd_HHmmss);
     }
 
     public static String fmt_yyyyMMdd_HHmmssSSS()
     {
       return fmtNow(DateUtils.FmtStr.yyyyMMdd_HHmmssSSS);
     }
 
     public static String fmt_HHmm()
     {
       return fmtNow(DateUtils.FmtStr.HHmm);
     }
 
     public static String fmt_HHmmss()
     {
       return fmtNow(DateUtils.FmtStr.HHmmss);
     }
 
     public static String fmt_HHmmssSSS()
     {
       return fmtNow(DateUtils.FmtStr.hhmmssSSS);
     }
 
     public static String fmt_CN_yyyyMMdd_HHmmss()
     {
       return fmtNow(DateUtils.FmtStr.CN_yyyyMMdd_HHmmss);
     }
 
     public static String fmt_CN_yyyyMMdd()
     {
       return fmtNow(DateUtils.FmtStr.CN_yyyyMMdd);
     }
 
     public static String fmt_CN_HHmmss()
     {
       return fmtNow(DateUtils.FmtStr.CN_HHmmss);
     }
     
     public static String fmt_CN_Week()
     {
    	 String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
         Calendar cal = Calendar.getInstance();
         cal.setTime(DateUtils.getDate());
         int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
         if (w < 0)
             w = 0;
         return weekDays[w];
     }
   }
 }
