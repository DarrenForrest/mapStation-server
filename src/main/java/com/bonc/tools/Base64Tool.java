package com.bonc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;

public class Base64Tool {
    // 将 file 转化为 Base64
   static public String fileToBase64(String path) {
        File file = new File(path);
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();

            return Base64.encodeBase64String(buffer);
        } catch (Exception e) {
            throw new RuntimeException("文件路径无效\n" + e.getMessage());
        }
    }

    // 将 base64 转化为 file
    static public boolean base64ToFile(String base64Str, String path) {
        byte[] buffer;
        try {
        	buffer = Base64.decodeBase64(base64Str);
            FileOutputStream out = new FileOutputStream(path);
            out.write(buffer);
            out.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("base64字符串异常或地址异常\n" + e.getMessage());
        }
    }
}
