package com.bonc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public abstract class FtpUtil {
    private static  FTPClient ftp;      
    /**  
     *   
     * @param path 上传到ftp服务器哪个路径下     
     * @param addr 地址  
     * @param port 端口号  
     * @param username 用户名  
     * @param password 密码  
     * @return  
     * @throws Exception  
     */    
    public static  boolean connect(String path,String addr,int port,String username,String password) throws Exception {      
        boolean result = false;      
        ftp = new FTPClient();      
        int reply;      
        ftp.connect(addr,port);      
        ftp.login(username,password);      
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);      
        reply = ftp.getReplyCode();      
        if (!FTPReply.isPositiveCompletion(reply)) {      
            ftp.disconnect();      
            return result;      
        }
        ftp.changeWorkingDirectory(path);      
        result = true;      
        return result;      
    }      
    /**  
     *   
     * @param file 上传的文件或文件夹  
     * @throws Exception  
     */    
    public static  void upload(File file) throws Exception{      
        if(file.isDirectory()){           
            ftp.makeDirectory(file.getName());                
            ftp.changeWorkingDirectory(file.getName());      
            String[] files = file.list();             
            for (int i = 0; i < files.length; i++) {      
                File file1 = new File(file.getPath()+"\\"+files[i] );      
                if(file1.isDirectory()){      
                    upload(file1);      
                    ftp.changeToParentDirectory();      
                }else{                    
                    File file2 = new File(file.getPath()+"\\"+files[i]);      
                    FileInputStream input = new FileInputStream(file2);      
                    ftp.storeFile(file2.getName(), input);      
                    input.close();                            
                }                 
            }      
        }else{      
            File file2 = new File(file.getPath());      
            FileInputStream input = new FileInputStream(file2);      
            ftp.storeFile(file2.getName(), input);      
            input.close();        
        }      
    }
    
    /**
     * 下载ftp文件
     * @param ftp 
     * @param localPath 本地目录
     * @return
     */
    public static boolean downFiles(String localPath) {  
        boolean success = false;  
        try {  
        	//linux环境放开-begin
        	 ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        	   //设置linux环境
             FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX);
             ftp.configure(conf);
        	//linux环境放开-end
        	  //设置访问被动模式
            ftp.setRemoteVerificationEnabled(false);
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles(); //得到目录的相应文件列表  
            for (FTPFile ff : fs) {  
                    File localFile = new File(localPath + "/" + ff.getName());  
                    OutputStream outputStream = new FileOutputStream(localFile);  
                    //将文件保存到输出流outputStream中  
                    ftp.retrieveFile(new String(ff.getName().getBytes("GBK"), "ISO-8859-1"), outputStream);  
                    outputStream.flush();  
                    outputStream.close();  
                    System.out.println("下载成功");  
            }  
            ftp.logout();  
            success = true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return success;  
    }  
    
}
