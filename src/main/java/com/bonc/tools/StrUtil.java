 package com.bonc.tools;
 
 import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
 
 public class StrUtil
 {
   private static final int PAD_LIMIT = 8192;
   public static final String EMPTY = "";
 
   public static String ISO8859ToUTF8(String strIOS8859){
	   String strUTF8="";
	   try {
		   strUTF8 = new String(strIOS8859.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	   return strUTF8;
   }
   
   public static boolean isOneOf(String str, String[] ary)
   {
     if (str == null)
       return false;
     for (int i = 0; i < ary.length; i++) {
       if (str.equals(ary[i])) {
         return true;
       }
     }
     return false;
   }
 
   public static boolean isEmpty(String str)
   {
     return (str == null) || (str.length() == 0);
   }
 
   public static boolean isNotEmpty(String str)
   {
     return !isEmpty(str);
   }
 
   public static boolean isEmptyWithTrim(String str)
   {
     return (isEmpty(str)) || ("".equals(str.trim()));
   }
 
   public static boolean isNotEmptyWithTrim(String str)
   {
     return !isEmptyWithTrim(str);
   }
 
   public static boolean isNull(String str)
   {
     return (isEmptyWithTrim(str)) || ("null".equalsIgnoreCase(str.trim()));
   }
 
   public static boolean isNotNull(String str)
   {
     return !isNull(str);
   }
 
   public static String subRight(String str, int len)
   {
     if (str == null) {
       return null;
     }
     if (len < 0) {
       return "";
     }
     if (str.length() <= len) {
       return str;
     }
     return str.substring(str.length() - len);
   }
   
   public static String rightPadChEn(String str, int size, String padStr){
	   String gbStr = "";
	   try {
		   gbStr=new String(str.getBytes("gb2312"),"iso-8859-1");
	   } catch (UnsupportedEncodingException e) {
		   e.printStackTrace();
	   }
	   int len = gbStr.length();
	   //System.out.println( str + "  gbStr.len=" + len);
	   if(len >=size){
		   return str;
	   }
	   
	   int diff=size - len;
	   StringBuffer sb=new StringBuffer();
	   sb.append(str);
	   for(int i=0; i<diff; i++){
		   sb.append(padStr);
	   }
	   return sb.toString();
   }
 
   public static String rightPad(String str, int size, String padStr)
   {
	 String gbStr = "";
	 try {
		gbStr=new String(str.getBytes("gb2312"),"iso-8859-1");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	   
     if (str == null) {
       return null;
     }
     if (isEmpty(padStr)) {
       padStr = " ";
     }
     int padLen = padStr.length();
     int strLen = gbStr.length();
     int pads = size - strLen;
     if (pads <= 0) {
       return str;
     }
     if ((padLen == 1) && (pads <= 8192)) {
       return rightPad(str, size, padStr.charAt(0));
     }
 
     if (pads == padLen)
       return str.concat(padStr);
     if (pads < padLen) {
       return str.concat(padStr.substring(0, pads));
     }
     char[] padding = new char[pads];
     char[] padChars = padStr.toCharArray();
     for (int i = 0; i < pads; i++) {
       padding[i] = padChars[(i % padLen)];
     }
     return str.concat(new String(padding));
   }
 
   public static String rightPad(String str, int size, char padChar)
   {
     if (str == null) {
       return null;
     }
     int pads = size - str.length();
     if (pads <= 0) {
       return str;
     }
     if (pads > 8192) {
       return rightPad(str, size, String.valueOf(padChar));
     }
     return str.concat(padding(pads, padChar));
   }
 
   private static String padding(int repeat, char padChar)
     throws IndexOutOfBoundsException
   {
     if (repeat < 0) {
       throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
     }
     char[] buf = new char[repeat];
     for (int i = 0; i < buf.length; i++) {
       buf[i] = padChar;
     }
     return new String(buf);
   }
 
   public static String subLeft(String str, int len)
   {
     if (str == null) {
       return null;
     }
     if (len < 0) {
       return "";
     }
     if (str.length() <= len) {
       return str;
     }
     return str.substring(0, len);
   }
 
   public static String leftPad(String str, int size, String padStr)
   {
     if (str == null) {
       return null;
     }
     if (isEmpty(padStr)) {
       padStr = " ";
     }
     int padLen = padStr.length();
     int strLen = str.length();
     int pads = size - strLen;
     if (pads <= 0) {
       return str;
     }
     if ((padLen == 1) && (pads <= 8192)) {
       return leftPad(str, size, padStr.charAt(0));
     }
 
     if (pads == padLen)
       return padStr.concat(str);
     if (pads < padLen) {
       return padStr.substring(0, pads).concat(str);
     }
     char[] padding = new char[pads];
     char[] padChars = padStr.toCharArray();
     for (int i = 0; i < pads; i++) {
       padding[i] = padChars[(i % padLen)];
     }
     return new String(padding).concat(str);
   }
 
   public static String leftPad(String str, int size, char padChar)
   {
     if (str == null) {
       return null;
     }
     int pads = size - str.length();
     if (pads <= 0) {
       return str;
     }
     if (pads > 8192) {
       return leftPad(str, size, String.valueOf(padChar));
     }
     return padding(pads, padChar).concat(str);
   }
 
   public static String doNull(String str)
   {
     return doNull(str, "").trim();
   }
 
   public static String doNull(BigDecimal digital)
   {
     if (digital != null) {
       return digital.toString();
     }
     return "";
   }
 
   public static String doNull(String str, String with)
   {
     return isNull(str) ? with : str;
   }
 
   public static String fromArray(String[] array, String split, String with)
   {
     if (array == null) {
       return null;
     }
     if (split == null) {
       split = "";
     }
 
     if (with == null) {
       with = "";
     }
     StringBuffer str = null;
     for (int i = 0; i < array.length; i++) {
       if (isNotEmpty(array[i])) {
         String temp = with + array[i].trim() + with;
         if (str == null)
           str = new StringBuffer(temp);
         else {
           str.append(split).append(temp);
         }
       }
     }
     return str == null ? null : str.toString();
   }
 
   public static String replaceOnce(String text, String searchString, String replacement)
   {
     return replace(text, searchString, replacement, 1);
   }
 
   public static String replace(String text, String searchString, String replacement)
   {
     return replace(text, searchString, replacement, -1);
   }
 
   public static String replace(String text, String searchString, String replacement, int max)
   {
     if ((isEmpty(text)) || (isEmpty(searchString)) || (replacement == null) || (max == 0)) {
       return text;
     }
     int start = 0;
     int end = text.indexOf(searchString, start);
     if (end == -1) {
       return text;
     }
     int replLength = searchString.length();
     int increase = replacement.length() - replLength;
     increase = increase < 0 ? 0 : increase;
     increase *= (max > 64 ? 64 : max < 0 ? 16 : max);
     StringBuffer buf = new StringBuffer(text.length() + increase);
     while (end != -1) {
       buf.append(text.substring(start, end)).append(replacement);
       start = end + replLength;
       max--; if (max == 0) {
         break;
       }
       end = text.indexOf(searchString, start);
     }
     buf.append(text.substring(start));
     return buf.toString();
   }
 
   public static String replaceEach(String text, String[] searchList, String[] replacementList)
   {
     return replaceEach(text, searchList, replacementList, false, 0);
   }
 
   public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList)
   {
     int timeToLive = searchList == null ? 0 : searchList.length;
     return replaceEach(text, searchList, replacementList, true, timeToLive);
   }
 
   private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive)
   {
     if ((text == null) || (text.length() == 0) || (searchList == null) || (searchList.length == 0) || 
       (replacementList == null) || (replacementList.length == 0)) {
       return text;
     }
 
     if (timeToLive < 0) {
       throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
     }
 
     int searchLength = searchList.length;
     int replacementLength = replacementList.length;
 
     if (searchLength != replacementLength) {
       throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + 
         replacementLength);
     }
 
     boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
 
     int textIndex = -1;
     int replaceIndex = -1;
     int tempIndex = -1;
 
     for (int i = 0; i < searchLength; i++) {
       if ((noMoreMatchesForReplIndex[i] != false) || (searchList[i] == null) || (searchList[i].length() == 0) || 
         (replacementList[i] == null)) {
         continue;
       }
       tempIndex = text.indexOf(searchList[i]);
 
       if (tempIndex == -1) {
         noMoreMatchesForReplIndex[i] = true;
       }
       else if ((textIndex == -1) || (tempIndex < textIndex)) {
         textIndex = tempIndex;
         replaceIndex = i;
       }
 
     }
 
     if (textIndex == -1) {
       return text;
     }
 
     int start = 0;
 
     int increase = 0;
 
     for (int i = 0; i < searchList.length; i++) {
       if ((searchList[i] == null) || (replacementList[i] == null)) {
         continue;
       }
       int greater = replacementList[i].length() - searchList[i].length();
       if (greater > 0) {
         increase += 3 * greater;
       }
     }
 
     increase = Math.min(increase, text.length() / 5);
 
     StringBuffer buf = new StringBuffer(text.length() + increase);
 
     while (textIndex != -1)
     {
       for (int i = start; i < textIndex; i++) {
         buf.append(text.charAt(i));
       }
       buf.append(replacementList[replaceIndex]);
 
       start = textIndex + searchList[replaceIndex].length();
 
       textIndex = -1;
       replaceIndex = -1;
       tempIndex = -1;
 
       for (int i = 0; i < searchLength; i++) {
         if ((noMoreMatchesForReplIndex[i] != false) || (searchList[i] == null) || (searchList[i].length() == 0) || 
           (replacementList[i] == null)) {
           continue;
         }
         tempIndex = text.indexOf(searchList[i], start);
 
         if (tempIndex == -1) {
           noMoreMatchesForReplIndex[i] = true;
         }
         else if ((textIndex == -1) || (tempIndex < textIndex)) {
           textIndex = tempIndex;
           replaceIndex = i;
         }
 
       }
 
     }
 
     int textLength = text.length();
     for (int i = start; i < textLength; i++) {
       buf.append(text.charAt(i));
     }
     String result = buf.toString();
     if (!repeat) {
       return result;
     }
 
     return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
   }
 
   public static String replaceChars(String str, char searchChar, char replaceChar)
   {
     if (str == null) {
       return null;
     }
     return str.replace(searchChar, replaceChar);
   }
 
   public static String replaceChars(String str, String searchChars, String replaceChars)
   {
     if ((isEmpty(str)) || (isEmpty(searchChars))) {
       return str;
     }
     if (replaceChars == null) {
       replaceChars = "";
     }
     boolean modified = false;
     int replaceCharsLength = replaceChars.length();
     int strLength = str.length();
     StringBuffer buf = new StringBuffer(strLength);
     for (int i = 0; i < strLength; i++) {
       char ch = str.charAt(i);
       int index = searchChars.indexOf(ch);
       if (index >= 0) {
         modified = true;
         if (index < replaceCharsLength)
           buf.append(replaceChars.charAt(index));
       }
       else {
         buf.append(ch);
       }
     }
     if (modified) {
       return buf.toString();
     }
     return str;
   }
 
   public static boolean startsWith(String str, String prefix)
   {
     return startsWith(str, prefix, false);
   }
 
   public static boolean startsWithIgnoreCase(String str, String prefix)
   {
     return startsWith(str, prefix, true);
   }
 
   private static boolean startsWith(String str, String prefix, boolean ignoreCase)
   {
     if ((str == null) || (prefix == null)) {
       return (str == null) && (prefix == null);
     }
     if (prefix.length() > str.length()) {
       return false;
     }
     return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
   }
 
   public static boolean endsWith(String str, String suffix)
   {
     return endsWith(str, suffix, false);
   }
 
   public static boolean endsWithIgnoreCase(String str, String suffix)
   {
     return endsWith(str, suffix, true);
   }
 
   private static boolean endsWith(String str, String suffix, boolean ignoreCase)
   {
     if ((str == null) || (suffix == null)) {
       return (str == null) && (suffix == null);
     }
     if (suffix.length() > str.length()) {
       return false;
     }
     int strOffset = str.length() - suffix.length();
     return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
   }
 
   public static int indexOf(String str, String searchStr, int startPos)
   {
     if ((str == null) || (searchStr == null)) {
       return -1;
     }
 
     if ((searchStr.length() == 0) && (startPos >= str.length())) {
       return str.length();
     }
     return str.indexOf(searchStr, startPos);
   }
 
   public static int indexOfIgnoreCase(String str, String searchStr)
   {
     return indexOfIgnoreCase(str, searchStr, 0);
   }
 
   public static int indexOfIgnoreCase(String str, String searchStr, int startPos)
   {
     if ((str == null) || (searchStr == null)) {
       return -1;
     }
     if (startPos < 0) {
       startPos = 0;
     }
     int endLimit = str.length() - searchStr.length() + 1;
     if (startPos > endLimit) {
       return -1;
     }
     if (searchStr.length() == 0) {
       return startPos;
     }
     for (int i = startPos; i < endLimit; i++) {
       if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
         return i;
       }
     }
     return -1;
   }
 
   public static int lastIndexOf(String str, char searchChar)
   {
     if (isEmpty(str)) {
       return -1;
     }
     return str.lastIndexOf(searchChar);
   }
 
   public static int lastIndexOf(String str, char searchChar, int startPos)
   {
     if (isEmpty(str)) {
       return -1;
     }
     return str.lastIndexOf(searchChar, startPos);
   }
 
   public static int lastIndexOf(String str, String searchStr)
   {
     if ((str == null) || (searchStr == null)) {
       return -1;
     }
     return str.lastIndexOf(searchStr);
   }
 
   public static int lastIndexOf(String str, String searchStr, int startPos)
   {
     if ((str == null) || (searchStr == null)) {
       return -1;
     }
     return str.lastIndexOf(searchStr, startPos);
   }
 
   public static int lastIndexOfIgnoreCase(String str, String searchStr)
   {
     if ((str == null) || (searchStr == null)) {
       return -1;
     }
     return lastIndexOfIgnoreCase(str, searchStr, str.length());
   }
 
   public static int lastIndexOfIgnoreCase(String str, String searchStr, int startPos)
   {
     if ((str == null) || (searchStr == null)) {
       return -1;
     }
     if (startPos > str.length() - searchStr.length()) {
       startPos = str.length() - searchStr.length();
     }
     if (startPos < 0) {
       return -1;
     }
     if (searchStr.length() == 0) {
       return startPos;
     }
 
     for (int i = startPos; i >= 0; i--) {
       if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
         return i;
       }
     }
     return -1;
   }
 
   public static String toChineseDigit1(String n)
   {
     String[] num1 = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
     String[] num2 = { "", "拾", "佰", "仟", "万", "亿", "兆", "吉", "太", "拍", "艾" };
     int len = n.length();
 
     if (len <= 5) {
       String ret = "";
       for (int i = 0; i < len; i++)
         if (n.charAt(i) == '0') {
           int j = i + 1;
           while ((j < len) && (n.charAt(j) == '0'))
             j++;
           if (j < len)
             ret = ret + "零";
           i = j - 1;
         } else {
           ret = ret + num1[(n.substring(i, i + 1).charAt(0) - '0')] + num2[(len - i - 1)];
         }
       return ret;
     }if (len <= 8) {
       String ret = toChineseDigit1(n.substring(0, len - 4));
       if (ret.length() != 0)
         ret = ret + num2[4];
       return ret + toChineseDigit1(n.substring(len - 4));
     }
     String ret = toChineseDigit1(n.substring(0, len - 8));
     if (ret.length() != 0)
       ret = ret + num2[5];
     return ret + toChineseDigit1(n.substring(len - 8));
   }
 
   public static String getChineseNum(int num)
   {
     switch (num) {
     case 0:
       return "零";
     case 1:
       return "壹";
     case 2:
       return "贰";
     case 3:
       return "叁";
     case 4:
       return "肆";
     case 5:
       return "伍";
     case 6:
       return "陆";
     case 7:
       return "柒";
     case 8:
       return "捌";
     case 9:
       return "玖";
     }
 
     return "";
   }
 
   public static Date stringToDate(String str)
   {
     String strFormat = "yyyy-MM-dd HH:mm:ss";
     if ((str != null) && (str.length() == 10))
       strFormat = "yyyy-MM-dd";
     SimpleDateFormat sdFormat = new SimpleDateFormat(strFormat);
     Date date = new Date();
     try {
       date = sdFormat.parse(str);
     } catch (Exception e) {
       Date date1 = null;
       return date1;
     }
     return date;
   }
 
   public static String dateFormat(Date tempDate)
   {
     if (tempDate == null) {
       return "";
     }
     SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
     String curentDate = formatDate.format(tempDate);
     return curentDate;
   }
 
   public static String strArrayToStr(String[] ary, String split, String with)
   {
     if (ary == null) {
       return null;
     }
     if (split == null) {
       split = "";
     }
 
     if (with == null) {
       with = "";
     }
     StringBuffer str = null;
     for (int i = 0; i < ary.length; i++) {
       if (isNotEmpty(ary[i])) {
         String temp = with + ary[i].trim() + with;
         if (str == null)
           str = new StringBuffer(temp);
         else {
           str.append(split).append(temp);
         }
       }
     }
     return str == null ? null : str.toString();
   }
 
   public static Object clone(Object o)
   {
     Object ret = null;
     try
     {
       ByteArrayOutputStream bout = new ByteArrayOutputStream();
       ObjectOutputStream out = new ObjectOutputStream(bout);
       out.writeObject(o);
       out.close();
 
       ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
       ObjectInputStream in = new ObjectInputStream(bin);
       ret = in.readObject();
       in.close();
     } catch (Exception e) {
       try {
         ret = BeanUtils.cloneBean(o);
       } catch (Exception e1) {
         ret = null;
       }
     }
     return ret;
   }
   
	/**
	 * 金额(分)转化为带两位小数金额元
	 * @param lg
	 * @return
	 */
	public static String amountChangeLS(Long lg)
	{
		Double dTemp=Double.valueOf(lg);
		Double dAmount=dTemp/100;
	    return getStringFormatDouble(dAmount);
	}
	
	/**
	 * 将Double转化为两位小数
	 * @param doubleTemp
	 * @return
	 */
	public static String getStringFormatDouble(Double doubleTemp)
	{
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("0.00");  
		String str=df.format(doubleTemp);
		return str;
	}
	
	public static String formatAmount(Object obj)
	{
		if(obj == null){
			return "";
		}
		
		Double dAmount = 0.0d;
		dAmount = Arith.div(((BigDecimal)obj).doubleValue(), 100.0, 2);
		
		return getStringFormatDouble(dAmount);
	}
	
 }

