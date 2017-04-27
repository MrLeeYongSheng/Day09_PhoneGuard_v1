package com.itheima.day09_phoneguard_v1.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

	public static void putString(Context context, String SPName, String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String SPName, String key, String defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defaultValue);
	}
	
	public static void putString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstants.SP_CONFIG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstants.SP_CONFIG, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defaultValue);
	}
	
	public static boolean getBoolean(Context context, String key, boolean defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstants.SP_CONFIG, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defaultValue);
	}
	
	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstants.SP_CONFIG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(key, value).commit();
	}
	
	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstants.SP_CONFIG, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, defaultValue);
	}
	
	public static void putInt(Context context, String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstants.SP_CONFIG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putInt(key, value).commit();
	}	
}
