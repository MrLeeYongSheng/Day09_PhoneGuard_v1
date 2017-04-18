package com.itheima.day09_phoneguard_v1.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * @param str
	 * 需要解密的字符串
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
