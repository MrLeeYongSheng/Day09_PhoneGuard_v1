package com.itheima.day09_phoneguard_v1.dao;

import com.itheima.day09_phoneguard_v1.db.BlacklistDB;
import com.itheima.day09_phoneguard_v1.db.BlacklistTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BlacklistDao {

	private BlacklistDB dbHelper;

	public BlacklistDao(Context context) {
		dbHelper = new BlacklistDB(context);
	};
	
	public void add(String phone, int mode) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BlacklistTable.PHONE, phone);
		values.put(BlacklistTable.MODE, mode);
		db.insert(BlacklistTable.TABLENAME, null, values);
	}
}
