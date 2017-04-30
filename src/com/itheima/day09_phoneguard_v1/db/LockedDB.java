package com.itheima.day09_phoneguard_v1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LockedDB extends SQLiteOpenHelper {

	public LockedDB(Context context) {
		super(context, "locked", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table locked(_id integer primary key autoincrement,packname text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table locked");
		onCreate(db);
	}

}
