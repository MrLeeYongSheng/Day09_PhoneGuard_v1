package com.itheima.day09_phoneguard_v1.unittest;


import java.util.List;

import com.itheima.day09_phoneguard_v1.dao.BlacklistDao;
import com.itheima.day09_phoneguard_v1.db.BlacklistTable;
import com.itheima.day09_phoneguard_v1.domain.BlackBean;
import com.itheima.day09_phoneguard_v1.domain.ContactBean;
import com.itheima.day09_phoneguard_v1.engine.ReadContactsEngine;

import android.test.AndroidTestCase;

public class MyTest extends AndroidTestCase {
	
	public void testTotalRowsAndPages() {
		BlacklistDao dao = new BlacklistDao(getContext());
		int totalRows = dao.getTotalRows();
		int totalPages = dao.getTotalPages(10);
		System.out.println("总页数:" + totalPages + " , 总条数 : " + totalRows);
	}
	
	public void testDeleteAndUpdate() {
		BlacklistDao dao = new BlacklistDao(getContext());
		dao.delete("1234567");
		dao.update("12345678", BlacklistTable.SMS);
		testGetAllDatasDB();
	}
	
	public void testGetAllDatasDB() {
		BlacklistDao dao = new BlacklistDao(getContext());
		List<BlackBean> allDatas = dao.getAllDatas();
		for (BlackBean blackBean : allDatas) {
			System.out.println(blackBean);
		}
	}

	public void test() {
		List<ContactBean> contacts = ReadContactsEngine.getContacts(getContext());
		for (ContactBean contactBean : contacts) {
			System.out.println(contactBean);
		}
	}
	
	public void testInsertDB() {
		BlacklistDao dao = new BlacklistDao(getContext());
		for (int i = 30; i < 35; i++) {
			
			dao.add("12345678" + i, BlacklistTable.SMS);
			dao.add("12345678" + i+5, BlacklistTable.TEL);
		}
	}
}
