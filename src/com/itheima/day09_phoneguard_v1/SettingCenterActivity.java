package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;
import com.itheima.day09_phoneguard_v1.view.ItemSettingCenterView;

public class SettingCenterActivity extends Activity {

	private ItemSettingCenterView iscv_autoUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initEvent();
		initData();
	}

	private void initEvent() {

		iscv_autoUpdate.setOnItemClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iscv_autoUpdate.setChecked(!iscv_autoUpdate.isChecked());
				SharedPreferencesUtils.putBoolean(SettingCenterActivity.this,
						MyConstants.AUTO_UPDATE, iscv_autoUpdate.isChecked());
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
		iscv_autoUpdate = (ItemSettingCenterView) findViewById(R.id.iscv_settingcenter_autoupdate);
		
	}

	private void initData() {

		if(SharedPreferencesUtils.getBoolean(SettingCenterActivity.this,
				MyConstants.AUTO_UPDATE, false)) {
			iscv_autoUpdate.setChecked(true);
		}
	}
}
