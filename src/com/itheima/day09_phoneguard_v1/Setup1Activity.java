package com.itheima.day09_phoneguard_v1;


public class Setup1Activity extends BaseSetupActivity {

	@Override
	public void preActivity() {

	}

	@Override
	public void nextActivity() {
		startActivity(Setup2Activity.class);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup1);

	}
}
