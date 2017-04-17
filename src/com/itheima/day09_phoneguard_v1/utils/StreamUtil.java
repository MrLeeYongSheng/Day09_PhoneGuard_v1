package com.itheima.day09_phoneguard_v1.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	public static String parser(InputStream is) throws IOException {
		if(is==null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024];
		while ((len = is.read(buf)) > 0) {
			baos.write(buf, 0, len);
		}
		is.close();
		baos.close();
		return baos.toString("GBK");
	}

}
