package com.itheima.day09_phoneguard_v1.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PhoneLocationEngine {
	
	/**
	 * @param phone
	 * 完整的电话号码
	 * @param context
	 * @return
	 * 查到:电话的归属地
	 * 查不到:""空字符串
	 */
	public static String queryLocationByPhone(String phone, Context context) {
		//匹配手机号码
		Pattern p = Pattern.compile("1{1}[0-9]{10}");
		Matcher m = p.matcher(phone);
		boolean b = m.matches();
		if(b) {
			return getLocationBy7MobiNum(phone.substring(0, 7), context);
		}
		
		//匹配座机电话号码
		if(phone.startsWith("0") && phone.length()>=10) {
			//3为位数情况:010...,020...没有030及其以上...
			if(phone.charAt(1)=='1' || phone.charAt(1)=='2') {
				return getLocationBy3PhoneNum(phone.substring(0, 3), context);
			} else {
				return getLocationBy3PhoneNum(phone.substring(1, 4), context);
			}
		}
		//服务电话
		if(phone.equals("110")) {
			return "警察局";
		} else if(phone.equals("120")) {
			return "医院";
		} else if(phone.equals("119")) {
			return "消防局";
		} else if(phone.equals("12315")) {
			return "消费委员会";
		}
		//匹配不上,返回""
		return "";
		
	}

	/**
	 * @param phone
	 * 前7位手机号码
	 * @param context
	 * @return
	 * 查到:手机归属地 
	 * 否则:""空字符串
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
	 * 010...,020...等前缀为3为的座机电话直接传前三位
	 * 前缀为4位的传第2到第4位,即把第一个0除掉
	 * @param context
	 * @return
	 * 电话归属地
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
