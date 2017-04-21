package com.itheima.day09_phoneguard_v1.unittest;


import java.util.List;

import com.itheima.day09_phoneguard_v1.dao.BlacklistDao;
import com.itheima.day09_phoneguard_v1.db.BlacklistTable;
import com.itheima.day09_phoneguard_v1.domain.ContactBean;
import com.itheima.day09_phoneguard_v1.engine.ReadContactsEngine;

import android.test.AndroidTestCase;

public class MyTest extends AndroidTestCase {

	public void test() {
		List<ContactBean> contacts = ReadContactsEngine.getContacts(getContext());
		for (ContactBean contactBean : contacts) {
			System.out.println(contactBean);
		}
	}
	
	public void testInsertDB() {
		BlacklistDao dao = new BlacklistDao(getContext());
		dao.add("123456", BlacklistTable.ALL);
	}
}
