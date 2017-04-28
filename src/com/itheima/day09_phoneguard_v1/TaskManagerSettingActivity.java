package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.itheima.day09_phoneguard_v1.service.LockScreenService;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.ServiceUtil;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

public class TaskManagerSettingActivity extends Activity {

	private CheckBox cb_lockScreenClear;
	private CheckBox cb_displaySysProc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {

		cb_lockScreenClear
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						Intent intent = new Intent(
								TaskManagerSettingActivity.this,
								LockScreenService.class);
						if (isChecked) {
							startService(intent);
						} else {
							stopService(intent);
						}
					}
				});

		cb_displaySysProc
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						SharedPreferencesUtils.putBoolean(
								TaskManagerSettingActivity.this,
								MyConstants.DISPLAY_SYS_PROC, isChecked);
					}
				});
	}

	private void initData() {

		cb_lockScreenClear.setChecked(ServiceUtil.isServiceRunning(
				TaskManagerSettingActivity.this,
				"com.itheima.day09_phoneguard_v1.service.LockScreenService"));

		cb_displaySysProc.setChecked(SharedPreferencesUtils.getBoolean(
				TaskManagerSettingActivity.this, MyConstants.DISPLAY_SYS_PROC,
				false));

	}

	private void initView() {

		setContentView(R.layout.activity_taskman_setting);
		cb_lockScreenClear = (CheckBox) findViewById(R.id.cb_taskman_setting_lockScreenClear);
		cb_displaySysProc = (CheckBox) findViewById(R.id.cb_taskman_setting_displaySysProc);
	}
}
