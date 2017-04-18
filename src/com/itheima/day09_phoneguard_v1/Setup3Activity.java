package com.itheima.day09_phoneguard_v1;

import android.content.Context;

public class Setup3Activity extends BaseSetupActivity {

	@Override
	public void preActivity() {
		startActivity(Setup2Activity.class);

	}

	@Override
	public void nextActivity() {
		startActivity(Setup4Activity.class);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);

	}


}
