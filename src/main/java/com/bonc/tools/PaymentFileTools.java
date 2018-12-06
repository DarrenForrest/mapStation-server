package com.bonc.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PaymentFileTools {

	public static void main(String[] args) {
		//String sFilePath = "D:\\data\\payment_file\\target\\autoterminal\\20150501.txt";
		//String sFilePath = "D:\\data\\payment_file\\target\\bankreceive\\A20150501204224.tol";
		//String sFilePath = "D:\\data\\payment_file\\target\\internetbus\\ECHL_DIYEEYZF_20150501.dz";
		//String sFilePath = "D:\\data\\payment_file\\target\\nshcard\\CARD20150501.txt";
		String sFilePath  = "D:\\data\\payment_file\\target\\bankreceive\\V20150527000000.tol";
		
		PaymentFileTools pfTools = new PaymentFileTools();		
		List<String> list = pfTools.readFile(sFilePath);
		
		for(int i =0; i < list.size(); i++)
		{
			System.out.println("index = " + i + " str= " + list.get(i));
		}
		
		//Long amount = pfTools.calAmount( "autoterminal" ,list);
		Long amount = pfTools.calAmount( "bankreceive" ,list);
		//Long amount = pfTools.calAmount( "internetbus" ,list);
		//Long amount = pfTools.calAmount( "nshcard" ,list);
		
		System.out.println("Amount = " + amount );
		
	}
	
	public Long calAmount( String fileType, List<String> list)
	{	
		Long lAmount = 0L;
		// 418110248140093|14183008|6222080200010246984 |283891 |150|2015-05-01|132847 |消费
		// 分隔符为"|"；单位为元； 金额字段为第五个字段；一毛五分钱记录未 .15 需要注意；
		if(fileType.equals("autoterminal"))
		{
			for(int i=0; i < list.size(); i++)
			{
				String sLine = list.get(i);
				String[] aField = sLine.split("\\|");
				String sMoney = "";
				sMoney = aField[4].trim();
				if(sMoney.startsWith(".")){sMoney = "0" + sMoney;} 				
				Double dMoney = Arith.mul( new Double(sMoney), 100d, 2);
				Long   lMoney = dMoney.longValue();
				lAmount += lMoney;				
				System.out.println("autoterminal index =" + i + " ; lMoney = " + lMoney + "  lAmount = " + lAmount);
			}
		}	
		
		//268|268|28648.56           |2015050106703170|
		if(fileType.equals("bankreceive"))
		{
			for(int i=0; i < list.size(); i++)
			{
				String sLine = list.get(i);
				System.out.println("sLine = " + "[" + sLine + "]");
				
				String[] aField = sLine.split("\\|");
				String sMoney = "";				
				
				if(aField.length >= 3)
				{
					sMoney = aField[2].trim();
				}else{
					sMoney = "0";
				}
				
				
				if(sMoney.startsWith(".")){sMoney = "0" + sMoney;} 				
				Double dMoney = Arith.mul( new Double(sMoney), 100d, 2);
				Long   lMoney = dMoney.longValue();
				lAmount += lMoney;				
				System.out.println("bankreceive index =" + i + " ; lMoney = " + lMoney + "  lAmount = " + lAmount);
			}
		}
		
		// internetbus 从首行中读取 总金额；单位认为是分
		if(fileType.equals("internetbus"))
		{
			for(int i=0; i < 1; i++)
			{
				String sLine = list.get(i);
				String[] aField = sLine.split("\\|");
				String sMoney = "";
				sMoney = aField[2].trim();
				if(sMoney.startsWith(".")){sMoney = "0" + sMoney;} 				
				Double dMoney = Arith.mul( new Double(sMoney), 1d, 2); //单位认为是分
				Long   lMoney = dMoney.longValue();
				lAmount += lMoney;				
				System.out.println("internetbus index =" + i + " ; lMoney = " + lMoney + "  lAmount = " + lAmount);
			}
		}
		// nshcard 从首行中读取 总金额; 单位认为是分
		if(fileType.equals("nshcard"))
		{
			for(int i=0; i < 1; i++)
			{
				String sLine = list.get(i);
				String[] aField = sLine.split(" +");
				String sMoney = "";
				
				System.out.println("aField size = " + aField.length );
				
				sMoney = aField[2].trim();
				if(sMoney.startsWith(".")){sMoney = "0" + sMoney;} 				
				Double dMoney = Arith.mul( new Double(sMoney), 1d, 2); //单位认为是分
				Long   lMoney = dMoney.longValue();
				lAmount += lMoney;				
				System.out.println("nshcard index =" + i + " ; lMoney = " + lMoney + "  lAmount = " + lAmount);
			}
		}
		
		return lAmount;
	}

	public List<String> readFile(String sFilePath)
	{
		List<String> list = new ArrayList<String>();		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		try {
			String str = "";
			fis = new FileInputStream( sFilePath );// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			while ((str = br.readLine()) != null) {
				list.add(str);
			}
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件" + sFilePath);
		} catch (IOException e) {
			System.out.println("读取文件失败" + sFilePath);
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// end of finally
		
		return list;
	}
}
