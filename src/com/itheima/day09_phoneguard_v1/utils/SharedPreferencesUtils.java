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
}
