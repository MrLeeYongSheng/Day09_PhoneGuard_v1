package com.itheima.day09_phoneguard_v1;

import android.content.Context;

public class Setup4Activity extends BaseSetupActivity {

	@Override
	public void preActivity() {

		startActivity(Setup3Activity.class);
	}

	@Override
	public void nextActivity() {

		startActivity(LostFindActivity.class);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup4);

	}

}
