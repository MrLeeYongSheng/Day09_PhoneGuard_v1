package com.itheima.day09_phoneguard_v1.dao;

import java.util.ArrayList;
import java.util.List;

import com.itheima.day09_phoneguard_v1.db.LockedDB;
import com.itheima.day09_phoneguard_v1.db.LockedTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

public class LockedDao {

	private LockedDB helper;
	private Context context;

	public LockedDao(Context context) {
		this.context = context;
		helper = new LockedDB(context);
	}

	/**
	 * @param packName
	 * 向数据库插入信息
	 */
	public void add(String packName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(LockedTable.PACKNAME, packName);
		db.insert(LockedTable.TABLENAME, null, values );
		db.close();
		context.getContentResolver().notifyChange(LockedTable.URI, null);
	}
	
	/**
	 * @param packName
	 * 要删除的数据
	 */
	public void remove(String packName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(LockedTable.TABLENAME, LockedTable.PACKNAME+"=?", new String[]{packName});
		db.close();
		context.getContentResolver().notifyChange(LockedTable.URI, null);
	}
	
	/**
	 * @return
	 * 获取数据库的所有packName数据
	 */
	public List<String> getAllDatas() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(LockedTable.TABLENAME, new String[]{LockedTable.PACKNAME}, null, null, null, null, null);
		while(cursor.moveToNext()){
			list.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * @param packName
	 * 需要查询的应用是否上锁
	 * @return
	 * 是否已经上锁
	 */
	public boolean isLocked(String packName) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(LockedTable.TABLENAME, new String[]{"1"}, LockedTable.PACKNAME+"=?", new String[]{packName}, null, null, null);
		if(cursor.moveToNext()) {
			cursor.close();
			db.close();
			return true;
		} else {
			cursor.close();
			db.close();
			return false;
		}
	}
}
