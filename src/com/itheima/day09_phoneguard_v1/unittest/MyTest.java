package com.itheima.day09_phoneguard_v1.unittest;


import java.util.List;

import com.itheima.day09_phoneguard_v1.dao.BlacklistDao;
import com.itheima.day09_phoneguard_v1.db.BlacklistTable;
import com.itheima.day09_phoneguard_v1.domain.BlackBean;
import com.itheima.day09_phoneguard_v1.domain.ContactBean;
import com.itheima.day09_phoneguard_v1.engine.PhoneLocationEngine;
import com.itheima.day09_phoneguard_v1.engine.ReadContactsEngine;
import com.itheima.day09_phoneguard_v1.engine.SmsEngine;

import android.test.AndroidTestCase;

public class MyTest extends AndroidTestCase {
	
	public void testPhoneLocation() {
		System.out.println(PhoneLocationEngine.queryLocationByPhone("13202613645", getContext()));
		System.out.println(PhoneLocationEngine.queryLocationByPhone("13538235385", getContext()));
		System.out.println(PhoneLocationEngine.queryLocationByPhone("07588541027", getContext()));
		System.out.println(PhoneLocationEngine.queryLocationByPhone("0108541079", getContext()));
		System.out.println(PhoneLocationEngine.queryLocationByPhone("110", getContext()));
		System.out.println(PhoneLocationEngine.queryLocationByPhone("0208541079", getContext()));
	}
	
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
