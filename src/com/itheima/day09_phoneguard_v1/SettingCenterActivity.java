package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.os.Bundle;

public class SettingCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
	}
}
