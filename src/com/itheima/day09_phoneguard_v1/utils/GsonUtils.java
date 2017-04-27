package com.itheima.day09_phoneguard_v1.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class GsonUtils {
	

	/**
	 * @param t
	 * Json����������ݽṹ��Class����
	 * @return
	 * ��װ�����ݵ�Json������
	 * @throws IOException
	 * ��ȡ�ļ���ʱ������Ĳ���
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
