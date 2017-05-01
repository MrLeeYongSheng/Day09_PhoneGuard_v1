package com.itheima.day09_phoneguard_v1.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * @param path
	 * 文件路径
	 * @return
	 * 成功:MD5码
	 * 失败:null
	 */
	public static String getFileMD5(String path) {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(path);
			byte[] buf = new byte[10240];
			int len=0;
			while((len=fis.read(buf)) > 0) {
				md.update(buf, 0, len);
			}
			byte[] bs = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<bs.length; i++) {
				sb.append(Integer.toHexString(bs[i] & 0x000000ff));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param str
	 * 需要加密的字符串
	 */
	public static String md5(String str) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digests = md.digest(str.getBytes());
			int b;
			for (int i=0; i<digests.length; i++) {
				b = digests[i] & 0xff;
				String hexString = Integer.toHexString(b);
				if(hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
