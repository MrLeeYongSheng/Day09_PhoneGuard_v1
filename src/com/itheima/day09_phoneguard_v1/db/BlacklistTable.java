package com.itheima.day09_phoneguard_v1.db;

public interface BlacklistTable {

	String PHONE = "phone";
	String MODE = "mode";
	String TABLENAME = "blacklist";
	
	int SMS = 1 << 0;
	int TEL = 1 << 1;
	int ALL = SMS | TEL;
}
