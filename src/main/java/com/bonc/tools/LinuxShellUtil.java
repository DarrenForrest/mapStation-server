package com.bonc.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

public class LinuxShellUtil {
	public static String executeLinuxCmd(String cmd) throws Exception {
		InputStream in  = null;
		BufferedReader br = null;
		try {
			Runtime run = Runtime.getRuntime();
			System.out.println("command:" + cmd);
			Process process = run.exec(cmd);
			process.waitFor(); 
			in = process.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			StringBuffer out = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				out.append(line).append("\n");
			}
			String result = out.toString();  
            System.out.println("result:"+result);
            process.destroy();
            return result;
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(in);
		}
	}
}
