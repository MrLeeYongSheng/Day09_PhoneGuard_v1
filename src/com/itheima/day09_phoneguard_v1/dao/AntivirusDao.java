package com.itheima.day09_phoneguard_v1.dao;

import java.io.File;

import com.itheima.day09_phoneguard_v1.db.AntiVirus;
import com.itheima.day09_phoneguard_v1.utils.MD5Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {
	
	private Context context;

	public AntivirusDao(Context context) {
		this.context = context;
	}

	public boolean isVirus(String filePath) {
		if(filePath == null) {
			return false;
		}
		String md5 = MD5Utils.getFileMD5(filePath);
		File file = new File(context.getFilesDir(), "antivirus.db");
		if(!file.exists()) {
			return false;
		}
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		if(db == null) {
			return false;
		}
		
		Cursor cursor = db.query(AntiVirus.TABLENAME, new String[]{"1"}, AntiVirus.MD5+"=?", new String[]{md5}, null, null, null);
		if(cursor.moveToNext()) {
			return true;
		} else {
			return false;
		}
		
	}
}
