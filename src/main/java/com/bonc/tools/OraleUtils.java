package com.bonc.tools;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.bonc.db.TxException;

public class OraleUtils {
	
/*    //写控制文件.ctl        
    String tableName = "test_result";//表名
    String fieldName = "( C01,C02,C03,C04,C05,C06,C07,C08,C09,C10,C11,C12,C13,C14,C15,C16,C17,C18,C19,C20,C21,C22,C23,C24,C25,C26,C27,C28,C29,C30,C31 )";//要写入表的字段
    String ctlfileName = getMssPath(request) + OsUtils.getFileSeparator() + "ctltest.ctl";//控制文件名 
    String splitSign = "X'05'"; //文件分隔符
    if(!OraleUtils.stlFileWriter(destPath,tableName,fieldName,splitSign,ctlfileName)){
    	throw new TxException("创建sqlldr控制文件失败");
    }
    
    //要执行的命令
    String user = ConfigUtil.getInstance().getValueByProperty("config/jdbc.properties", "jdbc.username");
    String psw = ConfigUtil.getInstance().getValueByProperty("config/jdbc.properties", "jdbc.password");
    String Database = ConfigUtil.getInstance().getValueByProperty("config/jdbc.properties", "database");
    String logfileName = getMssPath(request) + OsUtils.getFileSeparator() + "logtext.log";
    if(!OraleUtils.Executive(user,psw,Database,ctlfileName,logfileName)){
    	throw new TxException("sqlldr写入数据失败");
    }*/
	
	/**
     * * sqlldr写控制文件.ctl
     * @param fileName 数据文件名
     * @param tableName 表名
     * @param fieldName 要写入表的字段
     * @param splitSign 数据分隔符
     * @param ctlfileName 控制文件名
     */
    public static boolean stlFileWriter(String fileName,String tableName,String fieldName, String splitSign, String ctlfileName)
    {
		FileWriter fw = null;
		
		String strctl = "OPTIONS (skip=0)" +
		" LOAD DATA INFILE '"+fileName+"'" +
		" APPEND INTO TABLE "+tableName+"" +
		" FIELDS TERMINATED BY " +splitSign +
		" OPTIONALLY  ENCLOSED BY \"'\"" +
		" TRAILING NULLCOLS "+fieldName+"";
		try {
			fw = new FileWriter(ctlfileName);
			fw.write(strctl);
		}catch (IOException e){
			e.printStackTrace();
			return false;
		}finally {
		    try {
		        fw.flush();
		        fw.close();
		    }catch (IOException e){
		        e.printStackTrace();
		        return false;
		    }            
		}
		return true;
    }
    
    /**
     * sqlldr调用系统命令
     * @param user 
     * @param psw
     * @param Database
     * @param ctlfileName 控制文件名
     * @param logfileName 日志文件名
     */
	public static boolean Executive(String user,String psw,String Database,String ctlfileName,String logfileName)
	{
		InputStream ins = null;
		//要执行的DOS命令
		String dos="sqlldr "+user+"/"+psw+"@"+Database+" control="+""+ctlfileName+" log="+""+logfileName;
		//Linux环境下注释掉不需要CMD 直接执行DOS就可以
		String[] cmd = new String[]
		{ "cmd.exe", "/C", dos }; // Windows环境 命令
		try{
            Process process = Runtime.getRuntime().exec(cmd);
            ins = process.getInputStream(); // 获取执行cmd命令后的信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while ((line = reader.readLine()) != null){
                String msg = new String(line.getBytes("ISO-8859-1"), "UTF-8");
                System.out.println(msg); // 输出
            }
            int exitValue = process.waitFor();
            if(exitValue==0){
                System.out.println("返回值：" + exitValue+"\n数据导入成功");
            }else{
                System.out.println("返回值：" + exitValue+"\n数据导入失败");
                return false;
            }
            
            process.getOutputStream().close(); // 关闭
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
