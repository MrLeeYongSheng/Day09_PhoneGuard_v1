package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima.day09_phoneguard_v1.service.TelSmsBlackService;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.ServiceUtil;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;
import com.itheima.day09_phoneguard_v1.view.ItemSettingCenterView;

public class SettingCenterActivity extends Activity {

	private ItemSettingCenterView iscv_autoUpdate;
	private ItemSettingCenterView iscv_blacklist;

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
		
		iscv_blacklist.setOnItemClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(ServiceUtil.isServiceRunning(SettingCenterActivity.this, "com.itheima.day09_phoneguard_v1.service.TelSmsBlackService")){
					//拦截服务正在运行
					//停止服务
					Intent intent = new Intent(SettingCenterActivity.this, TelSmsBlackService.class);
					stopService(intent);
					iscv_blacklist.setChecked(false);
				} else {
					//拦截服务停止
					//开启服务
					Intent intent = new Intent(SettingCenterActivity.this, TelSmsBlackService.class);
					startService(intent);
					iscv_blacklist.setChecked(true);
				}
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
		iscv_autoUpdate = (ItemSettingCenterView) findViewById(R.id.iscv_settingcenter_autoupdate);
		iscv_blacklist = (ItemSettingCenterView) findViewById(R.id.iscv_settingcenter_blacklist);
		
	}

	private void initData() {

		if(SharedPreferencesUtils.getBoolean(SettingCenterActivity.this,
				MyConstants.AUTO_UPDATE, false)) {
			iscv_autoUpdate.setChecked(true);
		}
		
		if(ServiceUtil.isServiceRunning(SettingCenterActivity.this,
				"com.itheima.day09_phoneguard_v1.service.TelSmsBlackService")) {
			//拦截服务正在运行
			iscv_blacklist.setChecked(true);
		} else {
			//拦截服务没有运行
			iscv_blacklist.setChecked(false);
		}
	}
}
