package com.itheima.day09_phoneguard_v1.unittest;


import java.util.List;

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
}
