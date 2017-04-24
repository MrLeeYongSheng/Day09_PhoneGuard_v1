package com.itheima.day09_phoneguard_v1.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PhoneLocationEngine {
	
	/**
	 * @param phone
	 * �����ĵ绰����
	 * @param context
	 * @return
	 * �鵽:�绰�Ĺ�����
	 * �鲻��:""���ַ���
	 */
	public static String queryLocationByPhone(String phone, Context context) {
		//ƥ���ֻ�����
		Pattern p = Pattern.compile("1{1}[0-9]{10}");
		Matcher m = p.matcher(phone);
		boolean b = m.matches();
		if(b) {
			return getLocationBy7MobiNum(phone.substring(0, 7), context);
		}
		
		//ƥ�������绰����
		if(phone.startsWith("0") && phone.length()>=10) {
			//3Ϊλ�����:010...,020...û��030��������...
			if(phone.charAt(1)=='1' || phone.charAt(1)=='2') {
				return getLocationBy3PhoneNum(phone.substring(0, 3), context);
			} else {
				return getLocationBy3PhoneNum(phone.substring(1, 4), context);
			}
		}
		//����绰
		if(phone.equals("110")) {
			return "�����";
		} else if(phone.equals("120")) {
			return "ҽԺ";
		} else if(phone.equals("119")) {
			return "������";
		} else if(phone.equals("12315")) {
			return "����ίԱ��";
		}
		//ƥ�䲻��,����""
		return "";
		
	}

	/**
	 * @param phone
	 * ǰ7λ�ֻ�����
	 * @param context
	 * @return
	 * �鵽:�ֻ������� 
	 * ����:""���ַ���
	 */
	private static String getLocationBy7MobiNum(String phone, Context context) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath()+"/address.db",
				null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("data1", new String[]{"outkey"}, "id=?", new String[]{phone}, null, null, null);
		if(cursor.moveToNext()) {
			 String outkey = cursor.getString(cursor.getColumnIndex("outkey"));
			 Cursor cursor2 = db.query("data2", new String[]{"location"}, "id=?", new String[]{outkey}, null, null, null);
			 if(cursor2.moveToNext()) {
				 return cursor2.getString(0);
			 }
		}
		return "";
		
	}
	
	/**
	 * @param phone
	 * 010...,020...��ǰ׺Ϊ3Ϊ�������绰ֱ�Ӵ�ǰ��λ
	 * ǰ׺Ϊ4λ�Ĵ���2����4λ,���ѵ�һ��0����
	 * @param context
	 * @return
	 * �绰������
	 */
	private static String getLocationBy3PhoneNum(String phone, Context context){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath()+"/address.db",
				null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor;
		if(phone.charAt(1)=='1' || phone.charAt(1)=='2') {
			cursor = db.query("data2", new String[]{"location"}, "area=?", new String[]{phone.substring(1)},
					null, null, null);
		} else {
			cursor = db.query("data2", new String[]{"location"}, "area=?", new String[]{phone},
					null, null, null);
		}
		if(cursor.moveToNext()) {
			 String location = cursor.getString(0);
			 return location.substring(0, location.length()-2);
		}
		return "";
	}
	
}
