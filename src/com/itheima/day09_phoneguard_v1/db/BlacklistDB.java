package com.itheima.day09_phoneguard_v1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlacklistDB extends SQLiteOpenHelper {

	public BlacklistDB(Context context) {
		super(context, "Blacklist.db", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table blacklist(_id integer primary key autoincrement,phone text,mode integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table blacklist");
		onCreate(db);
	}

}
