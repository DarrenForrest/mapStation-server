package com.bonc.db;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;

public class LobUtils {

	public static String ClobToString(Clob clob) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(clob.getCharacterStream());
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {
				sb.append(s);
				s = br.readLine();
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] BlobToByteArray(Blob blob) {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = 0;
			int pos = 0;
			while (pos < bytes.length) {
				len = bis.read(bytes, pos, bytes.length - len);
				pos += len;
			}
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static long BlobToFile(FileOutputStream fwriter, Blob blob) {
		long wrote = 0;
		byte[] bytes = new byte[1024 * 10];
		int len = 0;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(blob.getBinaryStream());
			while ((len = bis.read(bytes)) != -1) {
				fwriter.write(bytes, 0, len);
				wrote += len;
			}
		} catch (Exception e) {
			e.printStackTrace();
			wrote = -1;
		} finally {
			try {
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
				wrote = -1;
			}
		}

		return wrote;
	}
}
