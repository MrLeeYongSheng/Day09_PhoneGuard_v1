package com.itheima.day09_phoneguard_v1.dao;

import java.util.ArrayList;
import java.util.List;

import com.itheima.day09_phoneguard_v1.db.BlacklistDB;
import com.itheima.day09_phoneguard_v1.db.BlacklistTable;
import com.itheima.day09_phoneguard_v1.domain.BlackBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlacklistDao {

	private BlacklistDB dbHelper;

	public BlacklistDao(Context context) {
		dbHelper = new BlacklistDB(context);
	};
	
	/**
	 * @param startIndex
	 * 	开始查询的位置
	 * @param count
	 * 	所要查询的数目
	 * @return
	 * 	逆序查询的结果List<BlackBean>
	 */
	public List<BlackBean> getMoreDatas(int startIndex,int count) {
		List<BlackBean> blacklist = new ArrayList<BlackBean>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(BlacklistTable.TABLENAME, new String[]{BlacklistTable.PHONE,BlacklistTable.MODE },
				null, null, null, null, "_id desc", startIndex + "," + count);
		while(cursor.moveToNext()) {
			BlackBean bean = new BlackBean();
			bean.setPhone(cursor.getString(cursor.getColumnIndex(BlacklistTable.PHONE)));
			bean.setMode(cursor.getInt(cursor.getColumnIndex(BlacklistTable.MODE)));
			blacklist.add(bean);
		}
		cursor.close();
		db.close();
		return blacklist;
	}
	
	/**
	 * @param currentPage
	 * 	想要查询的页数
	 * @param count
	 * 	该页的条目数
	 * @return
	 * 	该页的数据
	 */
	public List<BlackBean> getCurrentPageDatas(int currentPage,int count) {
		List<BlackBean> blacklist = new ArrayList<BlackBean>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(BlacklistTable.TABLENAME, new String[]{BlacklistTable.PHONE,BlacklistTable.MODE },
				null, null, null, null, null, (currentPage-1)*count + "," + count);
		while(cursor.moveToNext()) {
			BlackBean bean = new BlackBean();
			bean.setPhone(cursor.getString(cursor.getColumnIndex(BlacklistTable.PHONE)));
			bean.setMode(cursor.getInt(cursor.getColumnIndex(BlacklistTable.MODE)));
			blacklist.add(bean);
		}
		cursor.close();
		db.close();
		return blacklist;
	}
	
	/**
	 * @return
	 * 	获取总数据数
	 */
	public int getTotalRows() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(1) from blacklist", null);
		if(cursor.moveToNext()) {
			return cursor.getInt(0);
		}
		return 0;
	}
	
	/**
	 * @param count
	 * 	每页的数据数
	 * @return
	 * 	总页数
	 */
	public int getTotalPages(int count) {
		return (int) Math.ceil(getTotalRows()*1.0/count);
	}
	
	/**
	 * @param phone
	 * 	删除phone的一条数据
	 */
	public void delete(String phone) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(BlacklistTable.TABLENAME, BlacklistTable.PHONE+"=?", new String[]{phone});
		db.close();
	}
	
	/**
	 * @param bean
	 * 	把bean里面的phone的mode修改
	 */
	public void update(BlackBean bean) {
		update(bean.getPhone(), bean.getMode());
	}
	
	public void update(String phone, int mode) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BlacklistTable.MODE, mode);
		db.update(BlacklistTable.TABLENAME, values, BlacklistTable.PHONE+"=?",
				new String[]{phone});
		db.close();
	}
	
	public List<BlackBean> getAllDatas() {
		List<BlackBean> blacklist = new ArrayList<BlackBean>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(BlacklistTable.TABLENAME, 
				new String[]{BlacklistTable.PHONE, BlacklistTable.MODE}, 
				null, null, null, null, null);
		while(cursor.moveToNext()) {
			BlackBean bean = new BlackBean();
			bean.setPhone(cursor.getString(cursor.getColumnIndex(BlacklistTable.PHONE)));
			bean.setMode(cursor.getInt(cursor.getColumnIndex(BlacklistTable.MODE)));
			blacklist.add(bean);
		}
		cursor.close();
		db.close();
		return blacklist;
	}

	/**
	 * @param bean
	 * 	插入的黑名单的bean
	 */
	public void add(BlackBean bean) {
		add(bean.getPhone(), bean.getMode());
	}
	
	/**
	 * @param phone
	 *需要增加的电话
	 * @param mode
	 * 需要增加的模式
	 */
	public void add(String phone, int mode) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BlacklistTable.PHONE, phone);
		values.put(BlacklistTable.MODE, mode);
		db.insert(BlacklistTable.TABLENAME, null, values);
		db.close();
	}
}
