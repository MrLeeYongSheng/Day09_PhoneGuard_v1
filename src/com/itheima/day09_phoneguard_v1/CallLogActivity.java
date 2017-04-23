package com.itheima.day09_phoneguard_v1;

import java.util.List;

import com.itheima.day09_phoneguard_v1.domain.ContactBean;
import com.itheima.day09_phoneguard_v1.engine.ReadContactsEngine;

public class CallLogActivity extends BaseContactsTelSmsActivity{
 
	@Override
	public List<ContactBean> getDatas() {
		return ReadContactsEngine.getCallLogs(CallLogActivity.this);
	}

}
