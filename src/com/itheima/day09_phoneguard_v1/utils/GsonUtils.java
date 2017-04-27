package com.itheima.day09_phoneguard_v1.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class GsonUtils {
	

	/**
	 * @param t
	 * Json根对象的数据结构的Class对象
	 * @return
	 * 封装好数据的Json根对象
	 * @throws IOException
	 * 读取文件的时候对流的操作
	 */
	public static  <T> T getRootJsonParserObj(Class<T> t, File file) throws IOException{

			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str;
			while((str = br.readLine())!=null) {
				sb.append(str);
			}
			br.close();
			Gson gson = new Gson();
			return gson.fromJson(sb.toString(), t);
	}
}
