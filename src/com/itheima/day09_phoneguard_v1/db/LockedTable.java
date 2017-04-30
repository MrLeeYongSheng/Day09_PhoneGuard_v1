package com.itheima.day09_phoneguard_v1.db;

import android.net.Uri;

public interface LockedTable {

	String TABLENAME = "locked";
	String PACKNAME = "packname";
	Uri URI = Uri.parse("content://com.itheima");
}
